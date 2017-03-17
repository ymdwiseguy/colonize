package com.ymdwiseguy.servicetests

import com.ymdwiseguy.Colonization
import com.ymdwiseguy.col.Game
import com.ymdwiseguy.col.MapEditorStates
import com.ymdwiseguy.col.mapeditor.FormData
import com.ymdwiseguy.col.worldmap.WorldMap
import com.ymdwiseguy.col.worldmap.tile.Tile
import com.ymdwiseguy.col.worldmap.unit.Unit
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static com.ymdwiseguy.col.worldmap.movement.UnitDirection.RIGHT
import static com.ymdwiseguy.col.worldmap.tile.TileType.LAND_GRASS
import static org.springframework.http.HttpMethod.*
import static org.springframework.http.HttpStatus.OK

@SpringApplicationConfiguration(classes = Colonization.class)
@WebIntegrationTest("server.port:0")
class MapEditorServiceSpec extends Specification implements MapEditorStates {


    RestTemplate restTemplate = new TestRestTemplate()

    @Value('${local.server.port}')
    int PORT = 9090

    String URL_MAP_EDITOR
    String URL_MAP_EDITOR_WITH_UUID
    String URL_MAP_EDITOR_GENERATE
    String URL_SAVE_OR_LOAD_MAP
    String URL_MOVE_CURSOR
    String URL_SET_ACTIVE_TILE_TYPE
    String URL_OVERWRITE_ACTIVE_TILE

    Game GAME

    def setup() {
        URL_MAP_EDITOR = "http://localhost:$PORT/api/mapeditor"
    }

    def createInitialGame() {
        ResponseEntity<String> initialResponse = getResponseEntity(GET, URL_MAP_EDITOR)
        assert initialResponse.statusCode == OK

        GAME = new Game().fromJson(initialResponse.body)
        GAME_UUID = GAME.getGameId()
        assert GAME_UUID != null

        URL_MAP_EDITOR_WITH_UUID = "http://localhost:$PORT/api/mapeditor/$GAME_UUID"
        URL_MAP_EDITOR_GENERATE = "http://localhost:$PORT/api/mapeditor/$GAME_UUID/maps/generate"
        URL_SAVE_OR_LOAD_MAP = "http://localhost:$PORT/api/mapeditor/$GAME_UUID/maps/generatedtestmap"
        URL_MOVE_CURSOR = "http://localhost:$PORT/api/mapeditor/$GAME_UUID/movecursor/"
        URL_SET_ACTIVE_TILE_TYPE = "http://localhost:$PORT/api/mapeditor/$GAME_UUID/selecttiletype/"
        URL_OVERWRITE_ACTIVE_TILE = "http://localhost:$PORT/api/mapeditor/$GAME_UUID/activetile"
    }

    def "User journey for the map editor"() {
        when: "program is started (later initial menu)"
        createInitialGame()

        then: "the retrieved JSON is correct"
        GAME.toJson() == initialMapEditorJson(GAME_UUID)

        when: "the map editor is loaded"
        ResponseEntity<String> responseEntity = getResponseEntity(GET, URL_MAP_EDITOR_WITH_UUID)
        GAME = new Game().fromJson(responseEntity.body)

        then: "menus are set"
        responseEntity.body == initialMapEditorJsonWithMenus(GAME_UUID)

        when: "a world map generation is called"
        FormData formData = formData('generatedtestmap', 'Generated Test Map in User Journey', 2, 2)
        ResponseEntity<String> entityGeneratedMap = getResponseEntity(POST, URL_MAP_EDITOR_GENERATE, formData.toJson())
        GAME = new Game().fromJson(entityGeneratedMap.body)

        then: "a new world map was generated"
        GAME.getWorldMap() != null
        GAME.getWorldMap().getTiles().size() == 4
        entityGeneratedMap.body == mapEditorWithGeneratedMapJson(GAME_UUID, GAME.getWorldMap())

        when: "the menu point 'save map' is called"
        ResponseEntity<String> entityShowSavePopup = getResponseEntity(GET, URL_MAP_EDITOR_WITH_UUID + '?showPopup=SAVE_MAPEDITOR')

        then: "a popup is shown"
        entityShowSavePopup.body == mapEditorWithGeneratedMapJson(GAME_UUID, GAME.getWorldMap(), saveMapPopup(GAME_UUID))

        when: "saving a map is confirmed"
        ResponseEntity<String> entitySavedMap = getResponseEntity(PUT, URL_SAVE_OR_LOAD_MAP, GAME.toJson())

        then: "no error occurs and the map is returned unchanged"
        entitySavedMap.body == GAME.toJson()

        when: "a list of maps is shown"
        ResponseEntity<String> entityShowMapListPopup = getResponseEntity(GET, URL_MAP_EDITOR_WITH_UUID + '?showPopup=SHOW_MAPLIST')

        then: "the new map is contained"
        entityShowMapListPopup.body == mapEditorWithGeneratedMapJson(GAME_UUID, GAME.getWorldMap(), loadMapPopup(GAME_UUID))

        when: "a map should be loaded"
        ResponseEntity<String> entityLoadedMap = getResponseEntity(GET, URL_SAVE_OR_LOAD_MAP)
        Game loadedGame = new Game().fromJson(entityLoadedMap.body)
        WorldMap loadedWorldMap = copyWorldMapWithoutIds(loadedGame.getWorldMap())
        WorldMap oldWorldMap = copyWorldMapWithoutIds(GAME.getWorldMap())

        then: "the former saved map is returned"
        entityLoadedMap.body == mapEditorWithGeneratedMapJson(GAME_UUID, loadedGame.getWorldMap())

        and: "the world maps are identical except for the ids"
        loadedWorldMap.toJson() == oldWorldMap.toJson()

        when: "the cursor is moved"
        GAME = loadedGame
        ResponseEntity<String> entityMovedCursor = getResponseEntity(PUT, URL_MOVE_CURSOR + RIGHT.toString())
        GAME.getCursor().setxPosition(2)

        then: "it has a new position"
        entityMovedCursor.body == GAME.toJson()

        when: "the selected tile type is set"
        ResponseEntity<String> entityChangeSelectedTileType = getResponseEntity(PUT, URL_SET_ACTIVE_TILE_TYPE + LAND_GRASS.toString())
        GAME.setSelectedTileType(LAND_GRASS)

        then: "the updated game state is returned"
        entityChangeSelectedTileType.body == GAME.toJson()

        when: "the active tile is overwritten"
        ResponseEntity<String> entityChangedActiveTile = getResponseEntity(PUT, URL_OVERWRITE_ACTIVE_TILE)
        GAME.getWorldMap().getTileByCoordinates(2,1).setType(LAND_GRASS)

        then: "the tile has changed"
        entityChangedActiveTile.body == GAME.toJson()
    }

    ResponseEntity getResponseEntity(HttpMethod method, String url, String body = "nothing here", String contentType = "application/json; charset=utf-8") {
        HttpHeaders headers = new HttpHeaders()
        headers.add("Content-Type", contentType)
        HttpEntity<String> entity = new HttpEntity<>(body, headers)
        ResponseEntity responseEntity = restTemplate.exchange(url, method, entity, String.class)
        return responseEntity
    }

    private static WorldMap copyWorldMapWithoutIds(WorldMap worldMap) {
        WorldMap worldMapCopy = new WorldMap(null)

        List<Tile> strippedTiles = new ArrayList<>()
        for (Tile tile : worldMap.getTiles()) {
            Tile strippedTile = new Tile()
            strippedTile.setType(tile.getType())
            strippedTile.setxCoordinate(tile.getxCoordinate())
            strippedTile.setyCoordinate(tile.getyCoordinate())
            strippedTile.setAssets(tile.getAssets())
            strippedTiles.add(strippedTile)
        }
        worldMapCopy.setTiles(strippedTiles)

        List<Unit> strippedUnits = new ArrayList<>()
        for (Unit unit : worldMap.getUnits()) {
            Unit strippedUnit = new Unit()
            strippedUnit.setActive(false)
            strippedUnit.setUnitType(unit.getUnitType())
            strippedUnit.setxPosition(unit.getxPosition())
            strippedUnit.setyPosition(unit.getyPosition())
            strippedUnits.add(strippedUnit)
        }
        worldMapCopy.setUnits(strippedUnits)

        worldMapCopy.setHeight(worldMap.getHeight())
        worldMapCopy.setWidth(worldMap.getWidth())
        worldMapCopy.setTitle(worldMap.getTitle())
        worldMapCopy.setWorldMapName(worldMap.getWorldMapName())

        return worldMapCopy
    }


}

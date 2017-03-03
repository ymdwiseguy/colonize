package com.ymdwiseguy.col.mapeditor

import com.ymdwiseguy.col.Game
import com.ymdwiseguy.col.GameRepo
import com.ymdwiseguy.col.GameStateAssertions
import com.ymdwiseguy.col.MapEditorStates
import com.ymdwiseguy.col.cursor.Cursor
import com.ymdwiseguy.col.menu.structure.GameMenu
import com.ymdwiseguy.col.menu.structure.PopupType
import com.ymdwiseguy.col.menu.structure.SideMenu
import com.ymdwiseguy.col.worldmap.WorldMap
import com.ymdwiseguy.col.worldmap.WorldMapService
import com.ymdwiseguy.col.worldmap.tile.Tile
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR
import static com.ymdwiseguy.col.menu.structure.PopupType.*
import static com.ymdwiseguy.col.menu.structure.SideMenuType.DEFAULT
import static com.ymdwiseguy.col.worldmap.tile.TileType.LAND_GRASS

class MapEditorServiceSpec extends Specification implements GameStateAssertions, MapEditorStates {

    @Subject
    MapEditorService mapEditorService

    GameRepo gameRepo
    MapEditorRepo mapEditorRepo
    MapFileRepo mapFileRepo
    WorldMapService worldMapService

    GameMenu gameMenu = Mock(GameMenu)
    SideMenu sideMenu = Mock(SideMenu)

    String MAP_ID = 'mapId'
    String MAP_TITLE = 'a map'
    int MAP_HEIGHT = 5
    int MAP_WIDTH = 5
    WorldMap WORLD_MAP

    Cursor CURSOR = new Cursor(1,1)


    def setup() {
        WORLD_MAP = aWorldMap()

        sideMenu.type >> DEFAULT

        mapFileRepo = Mock(MapFileRepo)
        mapFileRepo.getWorldmap(MAP_NAME) >> WORLD_MAP
        mapFileRepo.fileExists(MAP_NAME) >> true
        mapFileRepo.updateWorldMap(MAP_NAME, WORLD_MAP) >> true

        gameRepo = Mock(GameRepo)
        gameRepo.createGame(_) >> mapEditor()

        worldMapService = Mock(WorldMapService)
        worldMapService.generateMap(MAP_WIDTH, MAP_HEIGHT) >> WORLD_MAP
        worldMapService.getWorldMap(MAP_ID) >> WORLD_MAP

        mapEditorRepo = Mock(MapEditorRepo)
        mapEditorRepo.getMapEditor(GAME_UUID) >> mapEditorWithMenu()
        mapEditorRepo.update(_) >> mapEditorWithMenuAndWorldMap()

        mapEditorService = new MapEditorService(mapFileRepo, gameRepo, worldMapService, mapEditorRepo)
    }

    def "initializing a game just sets a gameId"() {
        given: "nothing"

        when: "the initializing method is called"
        Game mapEditor = mapEditorService.initGame(null, null)

        then: "initial game data is returned"
        assertMinimalGameState(mapEditor)

        and: "no popup is set"
        mapEditor.getPopupMenu() == null
    }

    def "initializing a game with an Id"() {
        given: "a gameId"
        GAME_UUID

        when: "the initialization is called"
        Game mapEditor = mapEditorService.initGame(GAME_UUID, null)

        then: "initial game data is returned"
        assertInitialGameState(mapEditor)

        and: "no popup is set"
        mapEditor.getPopupMenu() == null
    }

    @Unroll("initializing a game with a popup of type #popupType")
    "initializing a game with a popup"() {
        given: "a gameId"
        GAME_UUID

        and: "a popup to be set"
        PopupType showPopup = popupType

        when: "the initialization is called"
        Game mapEditor = mapEditorService.initGame(GAME_UUID, showPopup)

        then: "initial game data is returned"
        assertInitialGameState(mapEditor)

        and: "a popup is set"
        mapEditor.getPopupMenu() != null
        mapEditor.getPopupMenu().getType() == expectedResult

        where:
        popupType    | expectedResult
        GENERATE_MAP | GENERATE_MAP
        SHOW_MAPLIST | SHOW_MAPLIST
    }

    def "initializing a game with a SAVE_MAPEDITOR popup is not possible (since no map exists yet)"() {
        given: "a gameId"
        GAME_UUID

        and: "a popup to be set"
        PopupType showPopup = SAVE_MAPEDITOR

        when: "the initialization is called"
        Game mapEditor = mapEditorService.initGame(GAME_UUID, showPopup)

        then: "initial game data is returned"
        assertInitialGameState(mapEditor)

        and: "a popup is set"
        mapEditor.getPopupMenu() == null
    }

    def "loading a map from file"() {
        given: "a gameId"
        GAME_UUID

        and: "a map name"
        MAP_NAME

        when: "the load method is called"
        Game mapEditor = mapEditorService.loadMap(GAME_UUID, MAP_NAME)

        then: "a world map is loaded from file"
        assertGameStateWithLoadedMap(mapEditor)
    }

    def "saving a map to the filesystem"() {
        given: "a map"
        Game mapEditor = mapEditorService.loadMap(GAME_UUID, MAP_NAME)

        when: "the map is saved"
        Game updatedEditor = mapEditorService.updateMap(mapEditor, MAP_NAME)

        then: "the map editor is returned"
        assertGameStateWithLoadedMap(updatedEditor)
    }

    def "saving under another name fails"() {
        given: "a map"
        Game mapEditor = mapEditorService.loadMap(GAME_UUID, MAP_NAME)

        when: "the map is saved"
        Game updatedEditor = mapEditorService.updateMap(mapEditor, "some other name")

        then: "no map editor is returned"
        updatedEditor == null
    }

    def "updating in repo fails"() {
        given: "a map"
        Game mapEditor = mapEditorService.loadMap(GAME_UUID, MAP_NAME)

        when: "the map is saved"
        Game updatedEditor = mapEditorService.updateMap(mapEditor, MAP_NAME)

        then: "updating file fails"
        1 * mapFileRepo.updateWorldMap(MAP_NAME, WORLD_MAP) >> false

        and: "no map editor is returned"
        updatedEditor == null
    }

    def "generating a map"() {
        when: "the map generation is called"
        Game mapEditorWithGeneratedMap = mapEditorService.generateMap(GAME_UUID, MAP_TITLE, 5, 5, MAP_NAME)

        then: "a map is returned"
        assertGameStateWithLoadedMap(mapEditorWithGeneratedMap)

        and: "the world map is saved"
        1 * worldMapService.saveNewWorldMap(WORLD_MAP) >> WORLD_MAP

        and: "the game state is saved"
        1 * mapEditorRepo.update(_) >> mapEditorWithMenuAndWorldMap()
    }

    def "generating a map requires an initialized game"() {
        when: "the map generation is called"
        Game mapEditorWithGeneratedMap = mapEditorService.generateMap(null, MAP_TITLE, 5, 5, MAP_NAME)

        then: "nothing is returned"
        mapEditorWithGeneratedMap == null
    }


    def "saving uses full map from db and not partial state from json"(){
        given: "a full map in the db"
        Game fullMapEditor = mapEditorWithLoadedMap()

        and: "a partial map json"
        Game partialMapEditor = mapEditorWithPartialMap()

        when: "the map is saved"
        Game updatedEditor = mapEditorService.updateMap(partialMapEditor, MAP_NAME)

        then: "the map editor with the full map is returned"
        1 * mapFileRepo.updateWorldMap(MAP_NAME, WORLD_MAP) >> true
        updatedEditor != null
        updatedEditor.toJson() == fullMapEditor.toJson()
    }

    Game mapEditorWithPartialMap() {
        Game mapEditor = initalMapEditorWithId()
        mapEditor.setWorldMap(aPartialWorldMap())
        mapEditor.setSelectedTileType(LAND_GRASS)
        return mapEditor
    }

    WorldMap aPartialWorldMap() {
        WorldMap worldMap = new WorldMap(MAP_ID, MAP_TITLE, MAP_NAME, MAP_WIDTH, MAP_HEIGHT)
        List<Tile> tiles = new ArrayList<>()
        tiles.add(aTile('1',1,1))
        worldMap.setTiles(tiles)
        return worldMap
    }

    Game mapEditor() {
        new Game(GAME_UUID, MAPEDITOR, null, CURSOR)
    }

    Game mapEditorWithMenu() {
        Game mapEditor = mapEditor()
        mapEditor.setGameMenu(gameMenu)
        mapEditor.setSideMenu(sideMenu)
        return mapEditor
    }

    Game mapEditorWithMenuAndWorldMap() {
        Game mapEditor = mapEditorWithMenu()
        mapEditor.setWorldMap(WORLD_MAP)
        mapEditor.setSelectedTileType(LAND_GRASS)
        return mapEditor
    }

}

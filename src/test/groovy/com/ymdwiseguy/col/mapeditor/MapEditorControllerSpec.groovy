package com.ymdwiseguy.col.mapeditor

import com.ymdwiseguy.col.Game
import com.ymdwiseguy.col.MapEditorStates
import com.ymdwiseguy.col.cursor.CursorMovementService
import com.ymdwiseguy.col.menu.structure.PopupType
import com.ymdwiseguy.col.worldmap.tile.TileRepo
import spock.lang.Specification
import spock.lang.Subject

import static com.ymdwiseguy.col.menu.structure.PopupType.GENERATE_MAP
import static com.ymdwiseguy.col.worldmap.movement.UnitDirection.RIGHT
import static com.ymdwiseguy.col.worldmap.tile.TileType.LAND_GRASS
import static org.springframework.http.HttpStatus.*

class MapEditorControllerSpec extends Specification implements MapEditorStates {

    @Subject
    MapEditorController mapEditorController

    MapEditorView mapEditorView
    MapEditorService mapEditorService
    CursorMovementService cursorMovementService
    MapEditorRepo mapEditorRepo
    TileRepo tileRepo

    Game expectedUpdateResult
    String ANOTER_GAME_UUID

    def setup() {
        ANOTER_GAME_UUID = UUID.randomUUID().toString()
        mapEditorService = Mock(MapEditorService)

        mapEditorService.initGame(null, null) >> initialMapEditor()
        mapEditorService.initGame(GAME_UUID, null) >> initalMapEditorWithId()
        mapEditorService.initGame(ANOTER_GAME_UUID, null) >> mapEditorWithLoadedMap()
        mapEditorService.initGame(null, GENERATE_MAP) >> initialMapEditorWithPopup()
        mapEditorService.initGame(GAME_UUID, GENERATE_MAP) >> initalMapEditorWithIdWithPopup()

        mapEditorService.loadMap(GAME_UUID, MAP_NAME) >> mapEditorWithLoadedMap()
        mapEditorService.generateMap(GAME_UUID, MAP_TITLE, MAP_WIDTH, MAP_HEIGHT, MAP_NAME) >> mapEditorWithLoadedMap()
        mapEditorService.generateMap(GAME_UUID, null, 0, 0, null) >> { throw new IOException() }

        mapEditorView = Mock(MapEditorView)
        cursorMovementService = Mock(CursorMovementService)

        mapEditorRepo = Mock(MapEditorRepo)
        mapEditorRepo.update(_) >> mapEditorWithLoadedMapAndCursor()

        tileRepo = Mock(TileRepo)

        mapEditorController = new MapEditorController(mapEditorView, mapEditorService, cursorMovementService, mapEditorRepo, tileRepo)
    }

    def "initializing a map editor"() {
        when: "the initializing endpoint is called"
        def result = mapEditorController.startMapEditorJson(null)

        then: "a map json is returned"
        result.body == initialMapEditorJson()
    }

    def "initializing a map editor with popup"() {
        given: "a popup type to be shown"
        PopupType popupType = GENERATE_MAP

        when: "the initializing endpoint is called"
        def result = mapEditorController.startMapEditorJson(popupType)

        then: "a map json is returned"
        result.body == initialMapEditorJsonWithPopup()
    }

    def "initializing a map editor with ID"() {
        when: "the initializing endpoint is called"
        def result = mapEditorController.getMapEditorDataJson(GAME_UUID, null)

        then: "a map json is returned"
        result.body == initalMapEditorWithIdJson()
    }

    def "initializing a map editor with ID with popup"() {
        given: "a popup type to be shown"
        PopupType popupType = GENERATE_MAP

        when: "the initializing endpoint is called"
        def result = mapEditorController.getMapEditorDataJson(GAME_UUID, popupType)

        then: "a map json is returned"
        result.body == initalMapEditorWithIdWithPopupJson()
    }

    def "get map returns a map from file"() {
        given: "a map name"
        MAP_NAME

        when: "the loading endpoint is called"
        def result = mapEditorController.loadMap(GAME_UUID, MAP_NAME)

        then: "a map is returned"
        result.body == mapEditorWithLoadedMapJson()
    }

    def "generating a map"() {
        given: "form data for a new map"
        formData()

        when: "the generation endpoint is called"
        def result = mapEditorController.generateMap(GAME_UUID, formData().toJson())

        then: "a game state including a new map is returned"
        result.body == mapEditorWithLoadedMapJson()
    }

    def "trying to generate a map with invalid json data"() {
        when: "the generation endpoint is called"
        def result = mapEditorController.generateMap(GAME_UUID, '{}')

        then: "status OK is returned"
        result.statusCode == OK

        and: "the body is empty"
        1 * mapEditorView.render(_) >> 'some html'
    }

    def "updating a map"() {
        when: "the update method is called"
        def result = mapEditorController.updateMap(mapEditorWithLoadedMapJson(), MAP_NAME)

        then: "the service has updated the map successfully"
        1 * mapEditorService.updateMap(_, MAP_NAME) >> mapEditorWithLoadedMap()

        and: "the map is updated and returned"
        result.body == mapEditorWithLoadedMapJson()
    }

    def "trying to update a map with invalid json"() {
        when: "the update method is called"
        def result = mapEditorController.updateMap('{ bla }', MAP_NAME)

        then: "status BAD_REQUEST is returned"
        result.statusCode == BAD_REQUEST

        and: "the body is empty"
        result.body == '{}'
    }

    def "failing update if map is not found"() {
        when: "the update method is called for a unknown map"
        def result = mapEditorController.updateMap(mapEditorWithLoadedMapJson(), 'unknown map')

        then: "the map editor service returns null"
        1 * mapEditorService.updateMap(_, 'unknown map') >> null

        and: "status NOT_FOUND is returned"
        result.statusCode == NOT_FOUND
    }

    def "moving the cursor"() {
        given: "an existing game with a cursor position"
        mapEditorWithLoadedMapJson()

        when: "the cursor movement method is called"
        def result = mapEditorController.moveCursor(GAME_UUID, RIGHT, null)

        then: "the movement service does its job"
        1 * cursorMovementService.moveCursor(_, RIGHT) >> mapEditorWithLoadedMapAndCursor()

        and: "the cursor gets a new position"
        result.body == mapEditorWithLoadedMapAndCursorJson()
    }

    def "setting the active tile type"() {
        given: "prepared mock data"
        expectedUpdateResult = mapEditorWithLoadedMapAndCursor()
        expectedUpdateResult.setSelectedTileType(LAND_GRASS)

        when: "the tileType is set"
        def result = mapEditorController.selectTileType(GAME_UUID, LAND_GRASS, null)

        then: "the gamestate is updated"
        1 * mapEditorRepo.update(_) >> expectedUpdateResult

        and: "a new selected tile type was set"
        result.body == expectedUpdateResult.toJson()
    }

    def "updating a tile with a new type"() {
        given: "a preparated mock"
        Game expectedResult = mapEditorWithLoadedMap()
        expectedResult.setSelectedTileType(LAND_GRASS)
        expectedResult.getWorldMap().getTileByCoordinates(1,1).setType(LAND_GRASS)

        when: "the activetile endpoint is called"
        def result = mapEditorController.setActiveTile(ANOTER_GAME_UUID, null)

        then: "the tile is updated in the DB"
        1 * tileRepo.updateTile(_)

        and: "a modified map is returned"
        result.body == expectedResult.toJson()
    }
}












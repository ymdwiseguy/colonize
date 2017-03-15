package com.ymdwiseguy.col.mapeditor

import com.ymdwiseguy.col.GameRepo
import com.ymdwiseguy.col.MapEditorStates
import com.ymdwiseguy.col.menu.implementation.EditorMainMenu
import com.ymdwiseguy.col.menu.implementation.SideMenuSelectTiles
import com.ymdwiseguy.col.menu.structure.GameMenu
import com.ymdwiseguy.col.menu.structure.SideMenu
import com.ymdwiseguy.col.worldmap.WorldMapService
import spock.lang.Specification
import spock.lang.Subject

class MapEditorRepoSpec extends Specification implements MapEditorStates {

    @Subject
    MapEditorRepo mapEditorRepo

    GameRepo gameRepo
    EditorMainMenu editorMainMenu
    WorldMapService worldMapService
    SideMenuSelectTiles sideMenuSelectTiles

    def setup() {
        gameRepo = Mock(GameRepo)
        gameRepo.getGame(GAME_UUID) >> Optional.of(mapEditorLoadedFromDb())
        gameRepo.updateGame(_) >> Optional.of(updatedMapEditor())

        editorMainMenu = Mock(EditorMainMenu)
        editorMainMenu.create(_) >> new GameMenu()

        worldMapService = Mock(WorldMapService)
        worldMapService.getWorldMapLimited(_, _, _) >> aWorldMap()

        sideMenuSelectTiles = Mock(SideMenuSelectTiles)
        sideMenuSelectTiles.create(_) >> new SideMenu()

        mapEditorRepo = new MapEditorRepo(gameRepo, editorMainMenu, worldMapService, sideMenuSelectTiles)
    }

    def "a saved state can be loaded"() {
        when: "the get method is called"
        def result = mapEditorRepo.getMapEditor(GAME_UUID)

        then: "the expected mapeditor is returned"
        result.toJson() == mapEditorWithLoadedMapJson(GAME_UUID)
    }

    def "a state can be updated"() {
        when: "the update is called"
        def result = mapEditorRepo.update(updatedMapEditor())

        then: "the updated map editor state is returned"
        result.toJson() == mapEditorWithLoadedMapJson(GAME_UUID)
    }
}

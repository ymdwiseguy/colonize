package com.ymdwiseguy.col

import com.ymdwiseguy.col.cursor.Cursor
import com.ymdwiseguy.col.mapeditor.FormData
import com.ymdwiseguy.col.menu.structure.GameMenu
import com.ymdwiseguy.col.menu.structure.PopupMenu
import com.ymdwiseguy.col.menu.structure.SideMenu
import com.ymdwiseguy.col.worldmap.WorldMap

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR
import static com.ymdwiseguy.col.worldmap.tile.TileType.LAND_GRASS

trait MapEditorStates implements MapEditorJsonStates, WorldMaps {

    String GAME_UUID = "4ad885e1-50a1-4c3f-914d-4e12b099dbf0"
    Cursor CURSOR = new Cursor(1, 1)

    Game initialMapEditor() {
        Game mapEditor = new Game()
        mapEditor.setGameId(GAME_UUID)
        mapEditor.setGameScreen(MAPEDITOR)
        mapEditor.setCursor(CURSOR)
        return mapEditor
    }

    Game initialMapEditorWithPopup() {
        Game mapEditor = new Game()
        mapEditor.setGameId(GAME_UUID)
        mapEditor.setGameScreen(MAPEDITOR)
        mapEditor.setCursor(CURSOR)
        mapEditor.setPopupMenu(new PopupMenu())
        return mapEditor
    }

    Game initalMapEditorWithId() {
        Game mapEditor = initialMapEditor()
        mapEditor.setGameMenu(new GameMenu());
        mapEditor.setSideMenu(new SideMenu());
        return mapEditor
    }

    Game initalMapEditorWithIdWithPopup() {
        Game mapEditor = initialMapEditor()
        mapEditor.setGameMenu(new GameMenu());
        mapEditor.setSideMenu(new SideMenu());
        mapEditor.setPopupMenu(new PopupMenu())
        return mapEditor
    }

    Game mapEditorWithLoadedMap() {
        Game mapEditor = initalMapEditorWithId()
        mapEditor.setWorldMap(aWorldMap())
        mapEditor.setSelectedTileType(LAND_GRASS)
        return mapEditor
    }

    Game mapEditorWithLoadedMapAndCursor() {
        Game mapEditor = initalMapEditorWithId()
        mapEditor.setWorldMap(aWorldMap())
        mapEditor.setSelectedTileType(LAND_GRASS)
        mapEditor.setCursor(new Cursor(2, 1))
        return mapEditor
    }


    Game mapEditorLoadedFromDb(String gameUuid = GAME_UUID) {
        Game mapEditor = new Game()
        mapEditor.setGameId(gameUuid)
        mapEditor.setGameScreen(MAPEDITOR)
        mapEditor.setWorldMap(new WorldMap(MAP_ID))
        mapEditor.setCursor(CURSOR)
        mapEditor.setSelectedTileType(LAND_GRASS)
        return mapEditor
    }

    Game updatedMapEditor() {
        Game mapEditor = mapEditorLoadedFromDb()
        mapEditor.getWorldMap().setTiles(someTiles())
        mapEditor.getWorldMap().setWorldMapName(MAP_NAME)
        return mapEditor
    }

    FormData formData() {
        FormData formData = new FormData()
        formData.setName(MAP_NAME)
        formData.setTitle(MAP_TITLE)
        formData.setWidth(5)
        formData.setHeight(5)

        return formData
    }
}

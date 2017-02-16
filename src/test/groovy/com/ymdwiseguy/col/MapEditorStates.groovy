package com.ymdwiseguy.col

import com.ymdwiseguy.col.cursor.Cursor
import com.ymdwiseguy.col.mapeditor.FormData
import com.ymdwiseguy.col.menu.structure.GameMenu
import com.ymdwiseguy.col.menu.structure.PopupMenu
import com.ymdwiseguy.col.menu.structure.SideMenu

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR
import static com.ymdwiseguy.col.worldmap.tile.TileType.LAND_GRASS

trait MapEditorStates implements WorldMaps {

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

    FormData formData() {
        FormData formData = new FormData()
        formData.setName(MAP_NAME)
        formData.setTitle(MAP_TITLE)
        formData.setWidth(5)
        formData.setHeight(5)

        return formData
    }

    String initialMapEditorJson() {
        '{\n' +
            '  "gameId" : "' + GAME_UUID + '",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : null,\n' +
            '  "worldMap" : null,\n' +
            '  "sideMenu" : null,\n' +
            '  "popupMenu" : null,\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : null\n' +
            '}'
    }

    String initialMapEditorJsonWithPopup() {
        '{\n' +
            '  "gameId" : "' + GAME_UUID + '",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : null,\n' +
            '  "worldMap" : null,\n' +
            '  "sideMenu" : null,\n' +
            '  "popupMenu" : ' + popupJson() + ',\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : null\n' +
            '}'
    }

    String initalMapEditorWithIdJson() {
        '{\n' +
            '  "gameId" : "4ad885e1-50a1-4c3f-914d-4e12b099dbf0",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : ' + mainMenuJson() + ',\n' +
            '  "worldMap" : null,\n' +
            '  "sideMenu" : ' + sideMenuJson() + ',\n' +
            '  "popupMenu" : null,\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : null\n' +
            '}'
    }

    String initalMapEditorWithIdWithPopupJson() {
        '{\n' +
            '  "gameId" : "4ad885e1-50a1-4c3f-914d-4e12b099dbf0",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : ' + mainMenuJson() + ',\n' +
            '  "worldMap" : null,\n' +
            '  "sideMenu" : ' + sideMenuJson() + ',\n' +
            '  "popupMenu" : ' + popupJson() + ',\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : null\n' +
            '}'
    }

    String mapEditorWithLoadedMapJson() {
        '{\n' +
            '  "gameId" : "4ad885e1-50a1-4c3f-914d-4e12b099dbf0",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : ' + mainMenuJson() + ',\n' +
            '  "worldMap" : ' + worldMapJson() + ',\n' +
            '  "sideMenu" : ' + sideMenuJson() + ',\n' +
            '  "popupMenu" : null,\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : "LAND_GRASS"\n' +
            '}'
    }

    String mapEditorWithLoadedMapAndCursorJson() {
        '{\n' +
            '  "gameId" : "4ad885e1-50a1-4c3f-914d-4e12b099dbf0",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : ' + mainMenuJson() + ',\n' +
            '  "worldMap" : ' + worldMapJson() + ',\n' +
            '  "sideMenu" : ' + sideMenuJson() + ',\n' +
            '  "popupMenu" : null,\n' +
            '  "cursor" : ' + '{\n' +
            '    "active" : true,\n' +
            '    "xPosition" : 2,\n' +
            '    "yPosition" : 1\n' +
            '  },\n' +
            '  "selectedTileType" : "LAND_GRASS"\n' +
            '}'
    }

    String mainMenuJson() {
        '{\n' +
            '    "submenus" : null\n' +
            '  }'
    }

    String cursorJson() {
        '{\n' +
            '    "active" : true,\n' +
            '    "xPosition" : 1,\n' +
            '    "yPosition" : 1\n' +
            '  }'
    }

    String sideMenuJson() {
        '{\n' +
            '    "type" : "DEFAULT",\n' +
            '    "header" : null,\n' +
            '    "entries" : null\n' +
            '  }'
    }

    String popupJson() {
        '{\n' +
            '    "header" : null,\n' +
            '    "entries" : null,\n' +
            '    "type" : null\n' +
            '  }'
    }
}

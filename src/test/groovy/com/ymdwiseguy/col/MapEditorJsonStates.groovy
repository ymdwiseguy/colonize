package com.ymdwiseguy.col

trait MapEditorJsonStates {

    String initialMapEditorJson(String gameUuid) {
        '{\n' +
            '  "gameId" : "' + gameUuid + '",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : null,\n' +
            '  "worldMap" : null,\n' +
            '  "sideMenu" : null,\n' +
            '  "popupMenu" : null,\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : null\n' +
            '}'
    }

    String initialMapEditorJsonWithMenus(String gameUuid) {
        '{\n' +
            '  "gameId" : "' + gameUuid + '",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : ' + gameMainMenuJson(gameUuid) + ',\n' +
            '  "worldMap" : null,\n' +
            '  "sideMenu" : ' + sideMenuJson(gameUuid) + ',\n' +
            '  "popupMenu" : null,\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : null\n' +
            '}'
    }

    String initialMapEditorJsonWithPopup(String gameUuid) {
        '{\n' +
            '  "gameId" : "' + gameUuid + '",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : null,\n' +
            '  "worldMap" : null,\n' +
            '  "sideMenu" : null,\n' +
            '  "popupMenu" : ' + mockedPopupJson() + ',\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : null\n' +
            '}'
    }

    String initalMapEditorWithIdJson(String gameUuid) {
        '{\n' +
            '  "gameId" : "' + gameUuid + '",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : ' + mockedMainMenuJson() + ',\n' +
            '  "worldMap" : null,\n' +
            '  "sideMenu" : ' + mockedSideMenuJson() + ',\n' +
            '  "popupMenu" : null,\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : null\n' +
            '}'
    }

    String initalMapEditorWithIdWithPopupJson(String gameUuid) {
        '{\n' +
            '  "gameId" : "' + gameUuid + '",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : ' + mockedMainMenuJson() + ',\n' +
            '  "worldMap" : null,\n' +
            '  "sideMenu" : ' + mockedSideMenuJson() + ',\n' +
            '  "popupMenu" : ' + mockedPopupJson() + ',\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : null\n' +
            '}'
    }

    String mapEditorWithLoadedMapJson(String gameUuid) {
        '{\n' +
            '  "gameId" : "' + gameUuid + '",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : ' + mockedMainMenuJson() + ',\n' +
            '  "worldMap" : ' + worldMapJson() + ',\n' +
            '  "sideMenu" : ' + mockedSideMenuJson() + ',\n' +
            '  "popupMenu" : null,\n' +
            '  "cursor" : ' + cursorJson() + ',\n' +
            '  "selectedTileType" : "LAND_GRASS"\n' +
            '}'
    }

    String mapEditorWithLoadedMapAndCursorJson(String gameUuid) {
        '{\n' +
            '  "gameId" : "' + gameUuid + '",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : ' + mockedMainMenuJson() + ',\n' +
            '  "worldMap" : ' + worldMapJson() + ',\n' +
            '  "sideMenu" : ' + mockedSideMenuJson() + ',\n' +
            '  "popupMenu" : null,\n' +
            '  "cursor" : ' + cursorJson("2","1","true") + ',\n' +
            '  "selectedTileType" : "LAND_GRASS"\n' +
            '}'
    }

    String gameMainMenuJson(String gameUuid) {
        '{\n' +
            '    "submenus" : [ {\n' +
            '      "entryName" : "Editor",\n' +
            '      "entries" : [ {\n' +
            '        "entryName" : "Generate Map ...",\n' +
            '        "endpointUrl" : "/api/mapeditor/' + gameUuid + '?showPopup=GENERATE_MAP",\n' +
            '        "method" : "GET",\n' +
            '        "active" : false\n' +
            '      }, {\n' +
            '        "entryName" : "Load Map ...",\n' +
            '        "endpointUrl" : "/api/mapeditor/' + gameUuid + '?showPopup=SHOW_MAPLIST",\n' +
            '        "method" : "GET",\n' +
            '        "active" : false\n' +
            '      }, {\n' +
            '        "entryName" : "Save map ...",\n' +
            '        "endpointUrl" : "/api/mapeditor/' + gameUuid + '?showPopup=SAVE_MAPEDITOR",\n' +
            '        "method" : "GET",\n' +
            '        "active" : false\n' +
            '      } ]\n' +
            '    } ]\n' +
            '  }'
    }

    String mockedMainMenuJson() {
        '{\n' +
            '    "submenus" : null\n' +
            '  }'
    }

    String sideMenuJson(String gameUuid) {
        '{\n' +
            '    "type" : "EDITOR_SELECT_TILES",\n' +
            '    "header" : "Select Tiles",\n' +
            '    "entries" : [ ' +
            selectTileMenuEntryJson(gameUuid, "LAND_ARCTIC") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_BOREAL_FOREST") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_BROADLEAF_FOREST") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_CONIFER_FOREST") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_DESERT") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_GRASS") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_HILLS") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_MARSH") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_MIXED_FOREST") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_MOUNTAINS") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_PLAINS") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_PRAIRIE") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_RAIN_FOREST") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_SAVANNAH") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_SCRUB_FOREST") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_SWAMP") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_TROPICAL_FOREST") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_TUNDRA") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_WETLAND_FOREST") + ', ' +
            selectTileMenuEntryJson(gameUuid, "OCEAN_DEEP") + ', ' +
            selectTileMenuEntryJson(gameUuid, "OCEAN_SHALLOW") +
            ' ]\n' +
            '  }'
    }

    String mockedSideMenuJson() {
        '{\n' +
            '    "type" : "DEFAULT",\n' +
            '    "header" : null,\n' +
            '    "entries" : null\n' +
            '  }'
    }

    String cursorJson(String x = "1", String y = "1", String active = "true") {
        '{\n' +
            '    "active" : ' + active + ',\n' +
            '    "xPosition" : ' + x + ',\n' +
            '    "yPosition" : ' + y + '\n' +
            '  }'
    }

    String mockedPopupJson() {
        '{\n' +
            '    "header" : null,\n' +
            '    "entries" : null,\n' +
            '    "type" : null\n' +
            '  }'
    }

    String selectTileMenuEntryJson(String gameUuid, String tileType) {
        '{\n' +
            '      "entryName" : "' + tileType + '",\n' +
            '      "endpointUrl" : "/api/mapeditor/' + gameUuid + '/selecttiletype/' + tileType + '",\n' +
            '      "method" : "PUT",\n' +
            '      "active" : false\n' +
            '    }'
    }
}

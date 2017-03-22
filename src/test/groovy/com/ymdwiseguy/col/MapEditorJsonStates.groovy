package com.ymdwiseguy.col

import com.ymdwiseguy.col.worldmap.WorldMap
import com.ymdwiseguy.col.worldmap.tile.Tile

trait MapEditorJsonStates implements WorldMaps {

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

    String wolrdMapJson(WorldMap worldMap) {
        String worldMapId = worldMap.getWorldMapId()
        assert (worldMapId != null)
        List<Tile> tiles = worldMap.getTiles()

        '{\n' +
            '    "worldMapId" : "' + worldMapId + '",\n' +
            '    "title" : "Generated Test Map in User Journey",\n' +
            '    "worldMapName" : "generatedtestmap",\n' +
            '    "tiles" : [ ' + tilesJson(worldMapId, tiles) + ' ],\n' +
            '    "units" : [ ],\n' +
            '    "width" : 2,\n' +
            '    "height" : 2\n' +
            '  }'
    }

    String tilesJson(String worldMapId, List<Tile> tiles) {

        String tilesJson = '';
        int tileCount = 0
        for (Tile tile : tiles) {
            tilesJson += '{\n' +
                '      "tileId" : "' + tile.getTileId() + '",\n' +
                '      "worldMapId" : "' + worldMapId + '",\n' +
                '      "xCoordinate" : ' + tile.getxCoordinate() + ',\n' +
                '      "yCoordinate" : ' + tile.getyCoordinate() + ',\n' +
                '      "type" : "OCEAN_DEEP",\n' +
                '      "assets" : {\n' +
                '        "forest" : false,\n' +
                '        "hill" : false,\n' +
                '        "river" : false\n' +
                '      }\n' +
                '    }'
            tileCount++
            if (tileCount < tiles.size()) {
                tilesJson += ', '
            }
        }
        return tilesJson
    }

    String mapEditorWithGeneratedMapJson(String gameUuid, WorldMap worldMap, String popup = 'null') {
        '{\n' +
            '  "gameId" : "' + gameUuid + '",\n' +
            '  "gameScreen" : "MAPEDITOR",\n' +
            '  "gameMenu" : ' + gameMainMenuJson(gameUuid) + ',\n' +
            '  "worldMap" : ' + wolrdMapJson(worldMap) + ',\n' +
            '  "sideMenu" : ' + sideMenuJson(gameUuid) + ',\n' +
            '  "popupMenu" : ' + popup + ',\n' +
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
            '  "cursor" : ' + cursorJson("2", "1", "true") + ',\n' +
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
            selectTileMenuEntryJson(gameUuid, "LAND_DESERT") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_GRASS") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_HILLS") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_MARSH") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_MOUNTAINS") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_PLAINS") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_PRAIRIE") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_SAVANNAH") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_SWAMP") + ', ' +
            selectTileMenuEntryJson(gameUuid, "LAND_TUNDRA") + ', ' +
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

    String saveMapPopup(String gameUuid) {
        '{\n' +
            '    "header" : "Overwrite map \'generatedtestmap\'?",\n' +
            '    "entries" : [ {\n' +
            '      "entryName" : "Save",\n' +
            '      "endpointUrl" : "/api/mapeditor/' + gameUuid + '/maps/generatedtestmap",\n' +
            '      "method" : "PUT",\n' +
            '      "active" : false\n' +
            '    }, {\n' +
            '      "entryName" : "Abort",\n' +
            '      "endpointUrl" : "/api/mapeditor/' + gameUuid + '",\n' +
            '      "method" : "GET",\n' +
            '      "active" : false\n' +
            '    } ],\n' +
            '    "type" : "SAVE_MAPEDITOR"\n' +
            '  }'
    }

    String loadMapPopup(String gameUuid) {
        '{\n' +
            '    "header" : "Load map...",\n' +
            '    "entries" : [ {\n' +
            '      "entryName" : "generatedtestmap",\n' +
            '      "endpointUrl" : "/api/mapeditor/' + gameUuid + '/maps/generatedtestmap",\n' +
            '      "method" : "GET",\n' +
            '      "active" : false\n' +
            '    }, {\n' +
            '      "entryName" : "testSandbox",\n' +
            '      "endpointUrl" : "/api/mapeditor/' + gameUuid + '/maps/testSandbox",\n' +
            '      "method" : "GET",\n' +
            '      "active" : false\n' +
            '    } ],\n' +
            '    "type" : "SHOW_MAPLIST"\n' +
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

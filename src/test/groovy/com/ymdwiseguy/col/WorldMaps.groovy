package com.ymdwiseguy.col

import com.ymdwiseguy.col.worldmap.WorldMap
import com.ymdwiseguy.col.worldmap.tile.Tile

import static com.ymdwiseguy.col.worldmap.tile.TileType.OCEAN_DEEP

trait WorldMaps {

    String MAP_NAME = 'testSandbox'
    String MAP_ID = 'mapId'
    String MAP_TITLE = 'a map'

    int MAP_WIDTH = 5
    int MAP_HEIGHT = 5

    WorldMap aWorldMap() {
        WorldMap worldMap = new WorldMap(MAP_ID, MAP_TITLE, MAP_NAME, MAP_WIDTH, MAP_HEIGHT)
        worldMap.setTiles(someTiles())
        return worldMap
    }

    List<Tile> someTiles(){
        List<Tile> tiles = new ArrayList<>()
        tiles.add(aTile('1', 1, 1))
        tiles.add(aTile('2', 2, 1))
        return tiles
    }

    Tile aTile(String id, int x, int y) {
        Tile tile = new Tile(id, MAP_ID, x, y, OCEAN_DEEP)
        return tile
    }

    String worldMapJson() {
        '{\n' +
            '    "worldMapId" : "mapId",\n' +
            '    "title" : "a map",\n' +
            '    "worldMapName" : "testSandbox",\n' +
            '    "tiles" : ' + tilesJson() + ',\n' +
            '    "units" : null,\n' +
            '    "width" : 5,\n' +
            '    "height" : 5\n' +
            '  }'
    }

    String tilesJson() {
        '[ {\n' +
            '      "tileId" : "1",\n' +
            '      "worldMapId" : "mapId",\n' +
            '      "xCoordinate" : 1,\n' +
            '      "yCoordinate" : 1,\n' +
            '      "type" : "OCEAN_DEEP"\n' +
            '    }, {\n' +
            '      "tileId" : "2",\n' +
            '      "worldMapId" : "mapId",\n' +
            '      "xCoordinate" : 2,\n' +
            '      "yCoordinate" : 1,\n' +
            '      "type" : "OCEAN_DEEP"\n' +
            '    } ]'
    }
}

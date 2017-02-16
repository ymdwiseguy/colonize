package com.ymdwiseguy.col.worldmap

import com.ymdwiseguy.col.WorldMaps
import com.ymdwiseguy.col.worldmap.tile.Tile
import com.ymdwiseguy.col.worldmap.tile.TileRepo
import com.ymdwiseguy.col.worldmap.unit.UnitRepo
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class WorldMapServiceSpec extends Specification implements WorldMaps {

    @Subject
    WorldMapService worldMapService

    WorldMapRepo worldMapRepo
    TileRepo tileRepo
    UnitRepo unitRepo

    WorldMap WORLD_MAP
    int LIMIT_RADIUS = 2

    def setup() {
        WORLD_MAP = aWorldMap()
        WORLD_MAP.setHeight(10)
        WORLD_MAP.setWidth(10)

        worldMapRepo = Mock(WorldMapRepo)
        worldMapRepo.getWorldmap(MAP_ID) >> Optional.of(WORLD_MAP)

        tileRepo = Mock(TileRepo)
        tileRepo.getTiles(MAP_ID) >> someTiles()
        tileRepo.getTilesLimited(MAP_ID, 1, 5, 1, 5) >> tiles_1To5_1To5()

        unitRepo = Mock(UnitRepo)
//        unitRepo.getUnits(MAP_ID) >> new ArrayList<Unit>()

        worldMapService = new WorldMapService(worldMapRepo, tileRepo, unitRepo, LIMIT_RADIUS)
    }

    def "getting a world map"() {
        when: "the getting method is called"
        def result = worldMapService.getWorldMap(MAP_ID)

        then: "a world map is returned"
        result == WORLD_MAP
    }

    def "getting a world map fails"() {
        given: "a non existent map"
        String ANOTHER_MAP_ID = 'bla'
        1 * worldMapRepo.getWorldmap(ANOTHER_MAP_ID) >> Optional.empty()

        when: "the getting method is called"
        def result = worldMapService.getWorldMap(ANOTHER_MAP_ID)

        then: "no map is returned"
        result == null
    }

    @Unroll
    "getting limits for cursor #cursorPosition"() {
        given: "a radius"
        WorldMapService limitedWorldMapService = new WorldMapService(worldMapRepo, tileRepo, unitRepo, 2)

        and: "a map width"
        int width = 10

        when: "calculation is called"
        int lowerX = limitedWorldMapService.limitLowerValue(width, cursorPosition)
        int upperX = limitedWorldMapService.limitUpperValue(width, cursorPosition)

        then: "the expected lower x value is returned"
        lowerX == expectedLowerValue

        and: "the expected upper x value is returned"
        upperX == expectedUpperValue

        where:
        cursorPosition || expectedLowerValue | expectedUpperValue
        1              || 1                  | 5
        3              || 1                  | 5
        4              || 2                  | 6
        7              || 5                  | 9
        8              || 6                  | 10
        10             || 6                  | 10
    }

    def "getting a limited map"() {
        when: "the getting limited method is called"
        WorldMap result = worldMapService.getWorldMapLimited(MAP_ID, 1, 1)
        def resultJson = result.toJson()
        then: "a limited map is returned"
        resultJson == WORLD_MAP_1To5_1To5().toJson()
    }

    def "generating a map"(){
        expect: false;
        // TODO: continue writing tests for WorldMapService
    }

    WorldMap WORLD_MAP_1To5_1To5() {
        WorldMap worldMap = new WorldMap(MAP_ID, MAP_TITLE, MAP_NAME, 10, 10)
        worldMap.setTiles(tiles_1To5_1To5())
        return worldMap
    }

    List<Tile> tiles_1To5_1To5(){
        List<Tile> tiles = new ArrayList<Tile>()
        for (int x = 1; x <= 5; x++) {
            for (int y = 1; y <= 5; y++) {
                tiles.add(aTile(x.toString() + '-' + y.toString(), x, y))
            }
        }
        return tiles
    }

}

package com.ymdwiseguy.col.worldmap.movement

import com.ymdwiseguy.col.worldmap.*
import spock.lang.Specification

import static com.ymdwiseguy.col.worldmap.UnitType.KARAVELLE

class MovementServiceSpec extends Specification {

    MovementService movementService
    String MAP_ID = "123"
    int MAP_WIDTH = 5
    int MAP_HEIGHT = 5
    int X_POSITION = 2
    int Y_POSITION = 3

    String UP = 'up'
    String DOWN = 'down'
    String LEFT = 'left'
    String RIGHT = 'right'

    def setup() {
        movementService = new MovementService()
    }

    def "moving around"() {
        given: "a worldmap with a unit"
        WorldMap worldMap = aWorldmap()
        assertUnitPosition(worldMap, 2, 3)

        when: "a movement left is triggered"
        worldMap = movementService.moveUnit(worldMap, LEFT)

        then: "the unit is moved one field to the left"
        assertUnitPosition(worldMap, 1, 3)

        when: "a movement up is triggered"
        worldMap = movementService.moveUnit(worldMap, UP)

        then: "the unit is moved one field up"
        assertUnitPosition(worldMap, 1, 2)

        when: "a movement right is triggered"
        worldMap = movementService.moveUnit(worldMap, RIGHT)

        then: "the unit is moved one field right"
        assertUnitPosition(worldMap, 2, 2)

        when: "a movement right is triggered"
        worldMap = movementService.moveUnit(worldMap, RIGHT)

        then: "the unit is moved one field right"
        assertUnitPosition(worldMap, 3, 2)

        when: "a movement down is triggered"
        worldMap = movementService.moveUnit(worldMap, DOWN)

        then: "the unit is moved one field down"
        assertUnitPosition(worldMap, 3, 3)
    }

    def "a unit can not be moved below 1"() {
        given: "a worldmap with a unit"
        WorldMap worldMap = aWorldmap()

        when: "a movement to the left is triggered"
        worldMap = movementService.moveUnit(worldMap, LEFT)

        then: "the x-position is 1"
        assertUnitPosition(worldMap, 1, 3)

        when: "a movement to the left is triggered again"
        worldMap = movementService.moveUnit(worldMap, LEFT)

        then: "the x-position still is 1"
        assertUnitPosition(worldMap, 1, 3)
    }

    def "a unit can not be moved outside the map border"() {
        given: "a worldmap with a unit"
        WorldMap worldMap = aWorldmap()

        when: "a movement to the right is triggered three times"
        worldMap = movementService.moveUnit(worldMap, RIGHT)
        worldMap = movementService.moveUnit(worldMap, RIGHT)
        worldMap = movementService.moveUnit(worldMap, RIGHT)

        then: "the position is 5"
        assertUnitPosition(worldMap, 5, 3)

        when: "a movement to the right is triggered again"
        worldMap = movementService.moveUnit(worldMap, RIGHT)

        then: "the position is 5"
        assertUnitPosition(worldMap, 5, 3)
    }

    def "a ship can not move over land tiles"() {
        given: "a map with a land tile and a ship"
        Tile tile = new Tile(UUID.randomUUID().toString(), MAP_ID, 2, 2, TileType.LAND_GRASS)
        WorldMap worldMap = aWorldmap(tile)

        when: "the ship tries to move onto the land tile"
        worldMap = movementService.moveUnit(worldMap, UP)

        then: "the units position stays the same"
        assertUnitPosition(worldMap, 2, 3)
    }

    boolean assertUnitPosition(WorldMap worldMap, int x, int y) {
        assert worldMap.getUnits().get(0).getxPosition() == x
        assert worldMap.getUnits().get(0).getyPosition() == y
        return true
    }

    WorldMap aWorldmap(Tile specialTile = null) {
        WorldMap worldMap = new WorldMap()
        worldMap.setWorldMapId(MAP_ID)
        worldMap.setTitle("new Map")
        worldMap.setWidth(MAP_WIDTH)
        worldMap.setHeight(MAP_HEIGHT)
        List<Tile> tileList = createSomeTiles(MAP_ID, specialTile)
        worldMap.setTiles(tileList)
        List<Unit> units = [aUnit()]
        worldMap.setUnits(units)

        return worldMap
    }

    List<Tile> createSomeTiles(String worldmapId, Tile specialTile = null) {
        List<Tile> tileList = new ArrayList<>()
        for (int yCoord = 1; yCoord <= MAP_HEIGHT; yCoord++) {
            for (int xCoord = 1; xCoord <= MAP_WIDTH; xCoord++) {
                if (specialTile && specialTile.getxCoordinate() == xCoord && specialTile.getyCoordinate() == yCoord) {
                    tileList.add(specialTile)
                } else {
                    Tile newTile = new Tile(UUID.randomUUID().toString(), worldmapId, xCoord, yCoord, TileType.OCEAN_DEEP)
                    tileList.add(newTile)
                }
            }
        }
        return tileList
    }

    Unit aUnit(int x = X_POSITION, int y = Y_POSITION) {
        Unit unit = new Unit()
        unit.setWorldMapId(MAP_ID)
        unit.setUnitType(KARAVELLE)
        unit.setxPosition(x)
        unit.setyPosition(y)
        unit.setActive(true)
        return unit
    }

}

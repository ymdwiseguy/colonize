package com.ymdwiseguy.col.worldmap.movement;

import com.ymdwiseguy.col.worldmap.tile.Tile;
import com.ymdwiseguy.col.worldmap.unit.Unit;
import com.ymdwiseguy.col.worldmap.WorldMap;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovementService {

    private int maxX;
    private int maxY;

    WorldMap moveUnit(WorldMap worldMap, String directionString) {

        UnitDirection direction = UnitDirection.valueOf(directionString.toUpperCase());

        setMaxX(worldMap.getWidth());
        setMaxY(worldMap.getHeight());

        List<Unit> units = worldMap.getUnits();
        ArrayList<Unit> updatedUnits = new ArrayList<>();
        for (Unit unit : units) {
            if (unit.isActive()) {
                unit = getNewCoordinates(unit, direction, worldMap);
            }
            updatedUnits.add(unit);
        }
        worldMap.setUnits(updatedUnits);
        return worldMap;
    }

    private Unit getNewCoordinates(Unit unit, UnitDirection direction, WorldMap worldMap) {
        int newX = unit.getxPosition();
        int newY = unit.getyPosition();
        switch (direction) {
            case UP:
                newY = newY - 1;
                break;
            case DOWN:
                newY = newY + 1;
                break;
            case LEFT:
                newX = newX - 1;
                break;
            case RIGHT:
                newX = newX + 1;
                break;
        }
        if (checkAccessability(worldMap, unit, newX, newY)) {
            unit.setxPosition(newX);
            unit.setyPosition(newY);
        }
        return unit;
    }

    private boolean checkAccessability(WorldMap worldMap, Unit unit, int x, int y){
        boolean accessable = checkWolrdBorders(x, y);
        if(accessable){
            accessable = checkLandSeaConflict(worldMap, unit, x, y);
        }
        return accessable;
    }

    private boolean checkLandSeaConflict(WorldMap worldMap, Unit unit, int x, int y) {
        Tile tile = worldMap.getTileByCoordinates(x,y);
        return (tile.getType().getParentTileType() == unit.getUnitType().getParentTileType());
    }

    private boolean checkWolrdBorders(int x, int y) {
        return !(x <= 0 || x > maxX) && !(y <= 0 || y > maxY);
    }

    private void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    private void setMaxY(int maxY) {
        this.maxY = maxY;
    }

}

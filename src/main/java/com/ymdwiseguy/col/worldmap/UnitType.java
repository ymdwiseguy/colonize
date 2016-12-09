package com.ymdwiseguy.col.worldmap;

import static com.ymdwiseguy.col.worldmap.ParentTileType.LAND;
import static com.ymdwiseguy.col.worldmap.ParentTileType.SEA;

public enum UnitType {
    KARAVELLE(SEA),
    SETTLER(LAND);

    ParentTileType parentTileType;

    UnitType(ParentTileType parentTileType) {
        this.parentTileType = parentTileType;
    }

    public ParentTileType getParentTileType() {
        return parentTileType;
    }
}

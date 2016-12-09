package com.ymdwiseguy.col.worldmap;

import static com.ymdwiseguy.col.worldmap.ParentTileType.LAND;
import static com.ymdwiseguy.col.worldmap.ParentTileType.SEA;

public enum TileType {
    OCEAN_DEEP(SEA),
    OCEAN_SHALLOW(SEA),
    LAND_GRASS(LAND);

    ParentTileType parentTileType;

    TileType(ParentTileType parentTileType) {
        this.parentTileType = parentTileType;
    }

    public ParentTileType getParentTileType() {
        return parentTileType;
    }
}

package com.ymdwiseguy.col.worldmap.tile;

import com.ymdwiseguy.col.worldmap.ParentTileType;

import static com.ymdwiseguy.col.worldmap.ParentTileType.LAND;
import static com.ymdwiseguy.col.worldmap.ParentTileType.SEA;

public enum TileType {
    LAND_ARCTIC(LAND),
//    LAND_BOREAL_FOREST(LAND), => LAND_TUNDRA + FOREST
//    LAND_BROADLEAF_FOREST(LAND), => LAND_PRAIRIE + FOREST
//    LAND_CONIFER_FOREST(LAND), => LAND_GRASS + FOREST
    LAND_DESERT(LAND),
    LAND_GRASS(LAND),
    LAND_HILLS(LAND),
    LAND_MARSH(LAND),
//    LAND_MIXED_FOREST(LAND), => LAND_PLAINS + FOREST
    LAND_MOUNTAINS(LAND),
    LAND_PLAINS(LAND),
    LAND_PRAIRIE(LAND),
//    LAND_RAIN_FOREST(LAND),  => LAND_SWAMP + FOREST
    LAND_SAVANNAH(LAND),
//    LAND_SCRUB_FOREST(LAND), => LAND_DESERT + FOREST
    LAND_SWAMP(LAND),
//    LAND_TROPICAL_FOREST(LAND), => LAND_SAVANNAH + FOREST
    LAND_TUNDRA(LAND),
//    LAND_WETLAND_FOREST(LAND), => LAND_MARSH + FOREST
    OCEAN_DEEP(SEA),
    OCEAN_SHALLOW(SEA);

    ParentTileType parentTileType;

    TileType(ParentTileType parentTileType) {
        this.parentTileType = parentTileType;
    }

    public ParentTileType getParentTileType() {
        return parentTileType;
    }
}

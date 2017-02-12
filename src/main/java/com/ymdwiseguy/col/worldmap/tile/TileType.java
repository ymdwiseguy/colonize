package com.ymdwiseguy.col.worldmap.tile;

import com.ymdwiseguy.col.worldmap.ParentTileType;

import static com.ymdwiseguy.col.worldmap.ParentTileType.LAND;
import static com.ymdwiseguy.col.worldmap.ParentTileType.SEA;

public enum TileType {
    LAND_ARCTIC(LAND),
    LAND_BOREAL_FOREST(LAND),
    LAND_BROADLEAF_FOREST(LAND),
    LAND_CONIFER_FOREST(LAND),
    LAND_DESERT(LAND),
    LAND_GRASS(LAND),
    LAND_HILLS(LAND),
    LAND_MARSH(LAND),
    LAND_MIXED_FOREST(LAND),
    LAND_MOUNTAINS(LAND),
    LAND_PLAINS(LAND),
    LAND_PRAIRIE(LAND),
    LAND_RAIN_FOREST(LAND),
    LAND_SAVANNAH(LAND),
    LAND_SCRUB_FOREST(LAND),
    LAND_SWAMP(LAND),
    LAND_TROPICAL_FOREST(LAND),
    LAND_TUNDRA(LAND),
    LAND_WETLAND_FOREST(LAND),
    OCEAN_DEEP(SEA),
    OCEAN_SHALLOW(SEA);

    ParentTileType parentTileType;

    private TileType convertsTo;

    static {
        LAND_ARCTIC.convertsTo = null;
        LAND_BOREAL_FOREST.convertsTo = LAND_TUNDRA;
        LAND_BROADLEAF_FOREST.convertsTo = LAND_PRAIRIE;
        LAND_CONIFER_FOREST.convertsTo = LAND_GRASS;
        LAND_DESERT.convertsTo = null;
        LAND_GRASS.convertsTo = null;
        LAND_HILLS.convertsTo = null;
        LAND_MARSH.convertsTo = null;
        LAND_MIXED_FOREST.convertsTo = TileType.LAND_PLAINS;
        LAND_MOUNTAINS.convertsTo = null;
        LAND_PLAINS.convertsTo = null;
        LAND_PRAIRIE.convertsTo = null;
        LAND_RAIN_FOREST.convertsTo = TileType.LAND_SWAMP;
        LAND_SAVANNAH.convertsTo = null;
        LAND_SCRUB_FOREST.convertsTo = TileType.LAND_DESERT;
        LAND_SWAMP.convertsTo = null;
        LAND_TROPICAL_FOREST.convertsTo = TileType.LAND_SAVANNAH;
        LAND_TUNDRA.convertsTo = null;
        LAND_WETLAND_FOREST.convertsTo = TileType.LAND_MARSH;
        OCEAN_DEEP.convertsTo = null;
        OCEAN_SHALLOW.convertsTo = null;
    }

    public TileType convertsTo() {
        return this.convertsTo;
    }

    TileType(ParentTileType parentTileType) {
        this.parentTileType = parentTileType;
    }

    public ParentTileType getParentTileType() {
        return parentTileType;
    }
}

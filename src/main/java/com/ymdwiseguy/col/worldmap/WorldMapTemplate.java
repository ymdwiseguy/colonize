package com.ymdwiseguy.col.worldmap;

import com.github.jknack.handlebars.TypeSafeTemplate;

public interface WorldMapTemplate extends TypeSafeTemplate<WorldMap> {

    WorldMapTemplate setWorldMap(String worldMap);
}

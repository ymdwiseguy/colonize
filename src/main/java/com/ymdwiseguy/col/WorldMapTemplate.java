package com.ymdwiseguy.col;

import com.github.jknack.handlebars.TypeSafeTemplate;

public interface WorldMapTemplate extends TypeSafeTemplate<WorldMap> {

    WorldMapTemplate setWorldMap(String worldMap);
}

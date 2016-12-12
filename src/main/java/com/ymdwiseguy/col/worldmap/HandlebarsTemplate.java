package com.ymdwiseguy.col.worldmap;

import com.github.jknack.handlebars.TypeSafeTemplate;
import com.ymdwiseguy.col.Game;

public interface HandlebarsTemplate extends TypeSafeTemplate<Game> {

    HandlebarsTemplate setWorldMap(String worldMap);
    HandlebarsTemplate setGame(String game);
}

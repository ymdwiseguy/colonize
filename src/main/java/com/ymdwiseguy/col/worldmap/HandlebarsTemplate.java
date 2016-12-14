package com.ymdwiseguy.col.worldmap;

import com.github.jknack.handlebars.TypeSafeTemplate;

public interface HandlebarsTemplate extends TypeSafeTemplate<String> {
    HandlebarsTemplate setGame(String game);
}

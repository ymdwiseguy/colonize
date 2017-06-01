package com.ymdwiseguy.col.menu.implementation;

import com.ymdwiseguy.col.menu.structure.MenuEntry;
import com.ymdwiseguy.col.menu.structure.PopupMenu;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ymdwiseguy.col.menu.structure.PopupType.GAME_MAIN_MENU;

@Component
public class GameMainMenu {

    public static PopupMenu create() {
        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry newGame = new MenuEntry("Start a new game", "/");
        MenuEntry loadGame = new MenuEntry("Load a game", "/");
        MenuEntry mapEditor = new MenuEntry("Map editor", "/mapeditor/");
        entries.add(newGame);
        entries.add(loadGame);
        entries.add(mapEditor);

        return new PopupMenu("Menu", entries, GAME_MAIN_MENU);
    }
}

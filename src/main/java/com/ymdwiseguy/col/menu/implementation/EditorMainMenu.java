package com.ymdwiseguy.col.menu.implementation;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.menu.structure.GameMenu;
import com.ymdwiseguy.col.menu.structure.MenuEntry;
import com.ymdwiseguy.col.menu.structure.Submenu;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EditorMainMenu {

    public GameMenu create(Game mapEditor) {
        MenuEntry loadMap = new MenuEntry("Load Map ...", "/api/mapeditor/" + mapEditor.getGameId() + "/maps/?showPopup=SHOW_MAPLIST");
        MenuEntry saveMap = new MenuEntry("Save map", "/api/mapeditor/" + mapEditor.getGameId() + "?showPopup=SAVE_MAPEDITOR");
        List<MenuEntry> menuEntries = new ArrayList<>();
        menuEntries.add(loadMap);
        menuEntries.add(saveMap);

        Submenu editorSubmenu = new Submenu("Editor", menuEntries);
        List<Submenu> subMenus = new ArrayList<>();
        subMenus.add(editorSubmenu);

        GameMenu gameMenu = new GameMenu(subMenus);
        return gameMenu;
    }
}

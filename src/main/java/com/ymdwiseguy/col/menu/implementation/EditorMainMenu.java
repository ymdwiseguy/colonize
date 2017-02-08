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
        String editorBaseUrl = "/api/mapeditor/" + mapEditor.getGameId();
        MenuEntry generateMap = new MenuEntry("Generate Map ...", editorBaseUrl + "?showPopup=GENERATE_MAP");
        MenuEntry loadMap = new MenuEntry("Load Map ...", editorBaseUrl + "?showPopup=SHOW_MAPLIST");
        MenuEntry saveMap = new MenuEntry("Save map ...", editorBaseUrl + "?showPopup=SAVE_MAPEDITOR");
        List<MenuEntry> menuEntries = new ArrayList<>();
        menuEntries.add(generateMap);
        menuEntries.add(loadMap);
        menuEntries.add(saveMap);

        Submenu editorSubmenu = new Submenu("Editor", menuEntries);
        List<Submenu> subMenus = new ArrayList<>();
        subMenus.add(editorSubmenu);

        return new GameMenu(subMenus);
    }
}

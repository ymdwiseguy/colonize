package com.ymdwiseguy.col.menu.implementation;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.menu.structure.MenuEntry;
import com.ymdwiseguy.col.menu.structure.SideMenu;
import com.ymdwiseguy.col.worldmap.tile.TileType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ymdwiseguy.col.menu.structure.SideMenuType.EDITOR_SELECT_TILES;
import static org.springframework.http.HttpMethod.PUT;

@Component
public class SideMenuSelectTiles {

    public SideMenu create(Game mapEditor) {
        SideMenu sideMenu = new SideMenu();
        sideMenu.setHeader("Select Tiles");
        sideMenu.setType(EDITOR_SELECT_TILES);
        List<MenuEntry> entries = new ArrayList<>();
        for (TileType tileType : TileType.values()) {
            MenuEntry entry = new MenuEntry(
                tileType.toString(),
                "/api/mapeditor/" + mapEditor.getGameId() + "/selecttiletype/" + tileType.toString());
            entry.setMethod(PUT);
            entries.add(entry);
        }
        sideMenu.setEntries(entries);
        return sideMenu;
    }
}

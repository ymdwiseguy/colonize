package com.ymdwiseguy.col.menu.implementation;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.menu.structure.MenuEntry;
import com.ymdwiseguy.col.menu.structure.PopupMenu;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ymdwiseguy.col.menu.structure.PopupType.SAVE_MAPEDITOR;
import static org.springframework.http.HttpMethod.PUT;

@Component
public class SaveGamePopupMenu {

    public PopupMenu create(Game mapEditor) {
        if(mapEditor.getWorldMap() == null || mapEditor.getWorldMap().getWorldMapName() == null){
            return null;
        }
        String worldMapName = mapEditor.getWorldMap().getWorldMapName();
        String gameId = mapEditor.getGameId();
        List<MenuEntry> entries = new ArrayList<>();
        MenuEntry save = new MenuEntry("Save", "/api/mapeditor/" + gameId + "/maps/" + worldMapName);
        save.setMethod(PUT);
        MenuEntry abort = new MenuEntry("Abort", "/api/mapeditor/" + gameId);
        entries.add(save);
        entries.add(abort);

        return new PopupMenu("Overwrite map '" + worldMapName + "'?", entries, SAVE_MAPEDITOR);
    }
}

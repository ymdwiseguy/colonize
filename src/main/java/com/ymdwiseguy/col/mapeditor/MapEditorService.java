package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.menu.SideMenu;
import com.ymdwiseguy.col.worldmap.WorldMap;
import com.ymdwiseguy.col.worldmap.WorldMapRepo;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class MapEditorService {

    private MapEditorRepo mapEditorRepo;
    private WorldMapRepo worldMapRepo;

    @Inject
    public MapEditorService(MapEditorRepo mapEditorRepo) {
        this.mapEditorRepo = mapEditorRepo;
    }

    SideMenu getMapList() {
        SideMenu sideMenu = new SideMenu();
        sideMenu.setHeader("Load map...");
        sideMenu.setEntries(mapEditorRepo.getMapListFromPath());
        return sideMenu;
    }


    WorldMap getMap(String mapid) {
        return mapEditorRepo.getWorldmap(mapid);
    }



}

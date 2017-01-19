package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.filehandling.MapFileHandler;
import com.ymdwiseguy.col.menu.structure.MenuEntry;
import com.ymdwiseguy.col.worldmap.WorldMap;
import com.ymdwiseguy.col.worldmap.tile.Tile;
import com.ymdwiseguy.col.worldmap.unit.Unit;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MapFileRepo {

    private MapFileHandler mapFileHandler;
    private static final Logger LOGGER = getLogger(MapFileRepo.class);

    @Inject
    public MapFileRepo(MapFileHandler mapFileHandler) {
        this.mapFileHandler = mapFileHandler;
    }

    List<MenuEntry> getMapListFromPath(String gameId) {
        List<MenuEntry> results = new ArrayList<>();
        URL url = getClass().getResource("/maps");
        File filePath = new File(url.getPath());
        File[] files = filePath.listFiles();

        if (files == null) {
            LOGGER.error("could not read config data from {}", "/maps");
        } else {
            for (File file : files) {
                if (file.isFile() && !file.isHidden()) {
                    String mapId = cleanFileName(file.getName());
                    MenuEntry menuEntry = new MenuEntry(mapId, "/api/mapeditor/" + gameId + "/maps/" + mapId);
                    results.add(menuEntry);
                }
            }
        }
        return results;
    }

    WorldMap getWorldmap(String mapid) {
        Optional<String> mapData = mapFileHandler.readDataFromFile(mapid);
        if (mapData.isPresent()) {
            try {
                WorldMap worldMap = new WorldMap();
                return worldMap.fromJson(mapData.get());
            } catch (IOException e) {
                LOGGER.info("unable to load map with id '{}'", mapid);
            }
        }
        return null;
    }

    boolean updateWorldMap(String mapid, WorldMap worldMap){
        WorldMap strippedIds = stripIds(worldMap);
        String mapData = strippedIds.toJson();
        return mapFileHandler.writeDataToFile(mapid, mapData);
    }

    private WorldMap stripIds(WorldMap worldMap) {
        worldMap.setWorldMapId(null);
        List<Tile> strippedTiles = new ArrayList<>();
        for (Tile strippedTile : worldMap.getTiles()) {
            strippedTile.setTileId(null);
            strippedTiles.add(strippedTile);
        }
        worldMap.setTiles(strippedTiles);
        List<Unit> strippedUnits = new ArrayList<>();
        for (Unit strippedUnit : worldMap.getUnits()) {
            strippedUnit.setUnitId(null);
            strippedUnits.add(strippedUnit);
        }
        worldMap.setUnits(strippedUnits);
        return worldMap;
    }

    boolean fileExists(String mapName) {
        return mapFileHandler.fileExists(mapName);
    }

    private String cleanFileName(String filename) {
        if(filename.lastIndexOf(".json") > 0) {
            return filename.substring(0, filename.lastIndexOf(".json"));
        }
        return filename;
    }

}

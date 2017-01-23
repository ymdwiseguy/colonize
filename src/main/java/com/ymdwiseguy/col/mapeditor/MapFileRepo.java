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
        WorldMap worldMapWithoutIds = copyWorldMapWithoutIds(worldMap);
        String mapData = worldMapWithoutIds.toJson();
        return mapFileHandler.writeDataToFile(mapid, mapData);
    }

    private WorldMap copyWorldMapWithoutIds(WorldMap worldMap) {
        WorldMap worldMapCopy = new WorldMap(null);

        List<Tile> strippedTiles = new ArrayList<>();
        for (Tile tile : worldMap.getTiles()) {
            Tile strippedTile = new Tile();
            strippedTile.setType(tile.getType());
            strippedTile.setxCoordinate(tile.getxCoordinate());
            strippedTile.setyCoordinate(tile.getyCoordinate());
            strippedTiles.add(strippedTile);
        }
        worldMapCopy.setTiles(strippedTiles);

        List<Unit> strippedUnits = new ArrayList<>();
        for (Unit unit : worldMap.getUnits()) {
            Unit strippedUnit = new Unit();
            strippedUnit.setActive(false);
            strippedUnit.setUnitType(unit.getUnitType());
            strippedUnit.setxPosition(unit.getxPosition());
            strippedUnit.setyPosition(unit.getyPosition());
            strippedUnits.add(strippedUnit);
        }
        worldMapCopy.setUnits(strippedUnits);

        worldMapCopy.setHeight(worldMap.getHeight());
        worldMapCopy.setWidth(worldMap.getWidth());
        worldMapCopy.setTitle(worldMap.getTitle());
        worldMapCopy.setWorldMapName(worldMap.getWorldMapName());

        return worldMapCopy;
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

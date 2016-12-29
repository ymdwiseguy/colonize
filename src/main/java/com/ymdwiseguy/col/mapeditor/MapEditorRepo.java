package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.menu.MenuEntry;
import com.ymdwiseguy.col.worldmap.WorldMap;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MapEditorRepo {

    private static final Logger LOGGER = getLogger(MapEditorRepo.class);

    List<MenuEntry> getMapListFromPath(String gameId) {
        List<MenuEntry> results = new ArrayList<>();
        URL url = getClass().getResource("/maps");
        File filePath = new File(url.getPath());
        File[] files = filePath.listFiles();

        if (files == null) {
            LOGGER.error("could not read config data from {}", "/maps");
        } else {
            for (File file : files) {
                if (file.isFile()) {
                    String mapId = cleanFileName(file.getName());
                    MenuEntry menuEntry = new MenuEntry(mapId, "/api/mapeditor/" + gameId + "/maps/" + mapId);
                    results.add(menuEntry);
                }
            }
        }
        return results;
    }

    WorldMap getWorldmap(String mapid) {
        Optional<String> mapData = getMapDataFromFile(mapid);
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

    private Optional<String> getMapDataFromFile(final String mapName) {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("maps/" + mapName + ".json");
        if (inputStream != null) {
            StringBuilder worldmapData = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    worldmapData.append(line);
                }
                return Optional.of(worldmapData.toString());
            } catch (IOException e) {
                LOGGER.error("could not read config data from " + mapName, e);
            }
        } else {
            LOGGER.error("could not read config data from {}", mapName);
        }
        return Optional.empty();
    }

    private String cleanFileName(String filename) {
        return filename.substring(0, filename.lastIndexOf(".json"));
    }

}

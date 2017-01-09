package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.filehandling.FileGetter;
import com.ymdwiseguy.col.menu.MenuEntry;
import com.ymdwiseguy.col.worldmap.WorldMap;
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
public class MapEditorRepo {

    private FileGetter fileGetter;
    private static final Logger LOGGER = getLogger(MapEditorRepo.class);

    @Inject
    public MapEditorRepo(FileGetter fileGetter) {
        this.fileGetter = fileGetter;
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
        Optional<String> mapData = fileGetter.readDataFromFile(mapid, "maps/");
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

    private String cleanFileName(String filename) {
        return filename.substring(0, filename.lastIndexOf(".json"));
    }

}

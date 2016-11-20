package com.ymdwiseguy.col.worldmap;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorldMapService {

    private static final Logger LOGGER = getLogger(WorldMapService.class);

    private final WorldMapRepo worldMapRepo;

    private final TileRepo tileRepo;

    @Autowired
    public WorldMapService(final WorldMapRepo WorldMapRepo, TileRepo tileRepo) {
        this.worldMapRepo = WorldMapRepo;
        this.tileRepo = tileRepo;
    }


    public WorldMap getWorldMap(String uuid) {
        Optional<WorldMap> worldMapOptional = worldMapRepo.getWorldmap(uuid);
        if (worldMapOptional.isPresent()) {
            WorldMap worldMap = worldMapOptional.get();
            List<Tile> tiles = tileRepo.getTiles(worldMap.getWorldMapID());
            worldMap.setTiles(tiles);
            return worldMap;
        }
        return null;
    }

    public WorldMap generateMap(int width, int height) {
        WorldMap generatedMap = new WorldMap();
        String mapId = UUID.randomUUID().toString();
        generatedMap.setWorldMapID(mapId);
        generatedMap.setTitle("new Map");
        generatedMap.setWidth(width);
        generatedMap.setHeight(height);
        List<Tile> tileList = new ArrayList<>();
        for (int rowCount = 1; rowCount <= height; rowCount++) {
            for (int tileCount = 1; tileCount <= width; tileCount++) {
                Tile newTile = new Tile(UUID.randomUUID().toString(), mapId, tileCount, rowCount, TileType.OCEAN_DEEP);
                tileList.add(newTile);
            }
        }
        generatedMap.setTiles(tileList);
        return generatedMap;
    }

//    public String generateMapJson(int width, int height) {
//        return generateMap(width, height).toJson();
//    }

    public String saveNewWorldMap(WorldMap worldMap) {
        String worldMapId = worldMapRepo.createWorldmap(worldMap);
        List<Tile> tiles = worldMap.getTiles();
        tileRepo.createTiles(tiles);

        return worldMapId;
    }

    public String saveWorldMapFromJson(String worldMapData) {
        WorldMap worldMap = null;
        try {
            worldMap = new WorldMap().fromJson(worldMapData);
            return worldMapRepo.createWorldmap(worldMap);
        } catch (IOException e) {
            LOGGER.error("unable to convert Json to WorldMap: {}", e);
        }
        return null;
    }

    public WorldMap updateWorldMap(WorldMap worldMap) {
        Optional<WorldMap> worldMapOptional = worldMapRepo.updateWorldmap(worldMap);
        if (worldMapOptional.isPresent()) {
            WorldMap updatedMap = worldMapOptional.get();
            updatedMap.setTiles(tileRepo.getTiles(updatedMap.getWorldMapID() ));
            return updatedMap;
        }
        LOGGER.info("Unable to get Map after update");
        return null;
    }
}

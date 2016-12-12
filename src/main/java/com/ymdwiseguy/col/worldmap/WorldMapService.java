package com.ymdwiseguy.col.worldmap;

import com.ymdwiseguy.col.worldmap.tile.Tile;
import com.ymdwiseguy.col.worldmap.tile.TileRepo;
import com.ymdwiseguy.col.worldmap.tile.TileType;
import com.ymdwiseguy.col.worldmap.unit.Unit;
import com.ymdwiseguy.col.worldmap.unit.UnitRepo;
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
    private final UnitRepo unitRepo;

    @Autowired
    public WorldMapService(final WorldMapRepo WorldMapRepo, TileRepo tileRepo, UnitRepo unitRepo) {
        this.worldMapRepo = WorldMapRepo;
        this.tileRepo = tileRepo;
        this.unitRepo = unitRepo;
    }


    public WorldMap getWorldMap(String uuid) {
        Optional<WorldMap> worldMapOptional = worldMapRepo.getWorldmap(uuid);
        if (worldMapOptional.isPresent()) {
            WorldMap worldMap = worldMapOptional.get();
            List<Tile> tiles = tileRepo.getTiles(worldMap.getWorldMapId());
            worldMap.setTiles(tiles);
            List<Unit> units = unitRepo.getUnits(worldMap.getWorldMapId());
            worldMap.setUnits(units);
            return worldMap;
        }
        return null;
    }

    public WorldMap generateMap(int width, int height) {
        WorldMap generatedMap = new WorldMap();
        String mapId = UUID.randomUUID().toString();
        generatedMap.setWorldMapId(mapId);
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

    public String saveNewWorldMap(WorldMap worldMap) {
        String worldMapId = worldMapRepo.createWorldmap(worldMap);
        List<Tile> tiles = worldMap.getTiles();
        tileRepo.createTiles(tiles);

        return worldMapId;
    }

    public WorldMap saveWorldMapFromJson(String worldMapData) {
        WorldMap worldMap = null;
        try {
            worldMap = new WorldMap().fromJson(worldMapData);
            if(worldMap.getWorldMapId() == null){
                worldMap.setWorldMapId(UUID.randomUUID().toString());
            }
            String mapId = worldMapRepo.createWorldmap(worldMap);
            worldMap.setUnits(saveUnits(worldMap.getUnits(), mapId));
            worldMap.setTiles(saveTiles(worldMap.getTiles(), mapId));
        } catch (IOException e) {
            LOGGER.error("unable to convert Json to WorldMap: {}", e);
        }
        return worldMap;
    }

    public WorldMap updateWorldMap(WorldMap worldMap) {
        Optional<WorldMap> worldMapOptional = worldMapRepo.updateWorldmap(worldMap);
        if (worldMapOptional.isPresent()) {
            List<Unit> units = unitRepo.updateUnits(worldMap.getUnits());
            List<Tile> tiles = tileRepo.updateTiles(worldMap.getTiles());

            WorldMap updatedMap = worldMapOptional.get();
            updatedMap.setUnits(units);
            updatedMap.setTiles(tiles);

            return updatedMap;
        }
        LOGGER.info("Unable to get Map after update");
        return null;
    }

    private List<Tile> saveTiles(List<Tile> tiles, String mapId){
        for (Tile tile : tiles) {
            if (tile.getTileId() == null) {
                tile.setTileId(UUID.randomUUID().toString());
            }
            tile.setWorldMapId(mapId);
        }
        tileRepo.createTiles(tiles);
        return tiles;
    }

    private List<Unit> saveUnits(List<Unit> units, String mapId){
        for (Unit unit : units) {
            if (unit.getUnitId() == null) {
                unit.setUnitId(UUID.randomUUID().toString());
            }
            unit.setWorldMapId(mapId);
        }
        unitRepo.createUnits(units);
        return units;
    }

}

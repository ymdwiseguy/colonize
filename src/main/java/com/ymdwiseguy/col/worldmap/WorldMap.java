package com.ymdwiseguy.col.worldmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymdwiseguy.col.worldmap.tile.Tile;
import com.ymdwiseguy.col.worldmap.unit.Unit;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class WorldMap {

    private static final Logger LOGGER = getLogger(WorldMap.class);
    private static final ObjectMapper mapper = new ObjectMapper();


    private String worldMapId;
    private String title;
    private List<Tile> tiles;
    private List<Unit> units;
    private int width = 56;
    private int height = 70;

    public WorldMap(String worldMapId) {
        setWorldMapId(worldMapId);
    }

    public WorldMap() {
    }

    public WorldMap(String worldMapId, String title, int width, int height) {
        setWorldMapId(worldMapId);
        setTitle(title);
        setWidth(width);
        setHeight(height);
    }

    public String getWorldMapId() {
        return worldMapId;
    }

    public void setWorldMapId(String worldMapId) {
        this.worldMapId = worldMapId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void setUnits(List<Unit> units) {
        this.units = units;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String toJson() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public WorldMap fromJson(String worldMapData) throws IOException {
        return mapper.reader().forType(WorldMap.class).readValue(worldMapData);
    }

    public Tile getTileByCoordinates(int x, int y) {
        List<Tile> tiles = this.getTiles();
        for (Tile tile : tiles) {
            if(tile.getxCoordinate() == x && tile.getyCoordinate() == y){
                return tile;
            }
        }
        return null;
    }
}

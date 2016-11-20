package com.ymdwiseguy.col.worldmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class WorldMap {

    private static final Logger LOGGER = getLogger(WorldMap.class);
    private static final ObjectMapper mapper = new ObjectMapper();


    private String worldMapID;
    private String title;
    private List<Tile> tiles;
    private int width = 56;
    private int height = 70;

    public WorldMap(String worldMapID) {
        setWorldMapID(worldMapID);
    }

    public WorldMap() {
    }

    public WorldMap(String worldMapID, String title, int width, int height) {
        setWorldMapID(worldMapID);
        setTitle(title);
        setWidth(width);
        setHeight(height);
    }

    public String getWorldMapID() {
        return worldMapID;
    }

    public String getTitle() {
        return title;
    }


    public void setWorldMapID(String worldMapID) {
        this.worldMapID = worldMapID;
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
}

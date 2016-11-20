package com.ymdwiseguy.col.worldmap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class Tile {


    private static final Logger LOGGER = getLogger(Tile.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private String tileId;
    private String worldMapId;
    private int xCoordinate;
    private int yCoordinate;
    private TileType type;

    public Tile(){

    }

    public Tile(String tile_id, String world_map_id, int x_coordinate, int y_coordinate, TileType type) {
        this.setTileId(tile_id);
        this.setWorldMapId(world_map_id);
        this.setxCoordinate(x_coordinate);
        this.setyCoordinate(y_coordinate);
        this.setType(type);
    }

    public String getTileId() {
        return tileId;
    }

    public void setTileId(String tileId) {
        this.tileId = tileId;
    }

    public String getWorldMapId() {
        return worldMapId;
    }

    public void setWorldMapId(String worldMapId) {
        this.worldMapId = worldMapId;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
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

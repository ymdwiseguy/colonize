package com.ymdwiseguy.col.worldmap.tile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TileAssets {

    private static final ObjectMapper mapper = new ObjectMapper();

    private Boolean extra;
    private Boolean forest;
    private Boolean hill;
    private Boolean river;
    private Boolean road;

    public Boolean getExtra() {
        return extra;
    }

    public void setExtra(Boolean extra) {
        this.extra = extra;
    }

    public Boolean getForest() {
        return forest;
    }

    public void setForest(Boolean forest) {
        this.forest = forest;
    }

    public Boolean getHill() {
        return hill;
    }

    public void setHill(Boolean hill) {
        this.hill = hill;
    }

    public Boolean getRiver() {
        return river;
    }

    public void setRiver(Boolean river) {
        this.river = river;
    }

    public Boolean getRoad() {
        return road;
    }

    public void setRoad(Boolean road) {
        this.road = road;
    }

    public String toJson() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public TileAssets fromJson(String tileData) throws IOException {
        return mapper.reader().forType(TileAssets.class).readValue(tileData);
    }
}

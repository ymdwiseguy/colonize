package com.ymdwiseguy.col.worldmap.tile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TileAssets {

    private static final ObjectMapper mapper = new ObjectMapper();

    private Boolean forest = false;
    private Boolean hill = false;
    private Boolean river = false;

    public TileAssets() {
    }

    public TileAssets(Boolean forest, Boolean hill, Boolean river) {
        this.forest = forest;
        this.hill = hill;
        this.river = river;
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

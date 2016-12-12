package com.ymdwiseguy.col.worldmap.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymdwiseguy.col.worldmap.WorldMap;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class Unit {


    private static final Logger LOGGER = getLogger(Unit.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private String unitId;
    private String worldMapId;
    private UnitType unitType;
    private boolean active = false;
    private int xPosition;
    private int yPosition;

    public Unit() {
    }

    public Unit(String unitId, String worldMapId, UnitType unitType, boolean active, int xPosition, int yPosition) {
        this.unitId = unitId;
        this.worldMapId = worldMapId;
        this.unitType = unitType;
        this.active = active;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getWorldMapId() {
        return worldMapId;
    }

    public void setWorldMapId(String worldMapId) {
        this.worldMapId = worldMapId;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public String toJson() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public WorldMap fromJson(String unitData) throws IOException {
        return mapper.reader().forType(Unit.class).readValue(unitData);
    }
}

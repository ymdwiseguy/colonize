package com.ymdwiseguy.col;

public class WorldMap {
    private String worldMapID;
    private String title;

    public WorldMap(String worldMapID) {
        setWorldMapID(worldMapID);
    }

    public WorldMap() {
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

}

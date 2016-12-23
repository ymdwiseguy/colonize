package com.ymdwiseguy.col.menu;

public class MenuEntry {

    private String entryName;
    private String endpointUrl;

    public MenuEntry(String entryName, String endpointUrl) {

        this.entryName = entryName;
        this.endpointUrl = endpointUrl;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }
}

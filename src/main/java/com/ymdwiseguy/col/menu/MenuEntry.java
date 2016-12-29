package com.ymdwiseguy.col.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class MenuEntry {

    private static final Logger LOGGER = getLogger(MenuEntry.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private String entryName;
    private String endpointUrl;

    public MenuEntry() {
    }

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


    public String toJson() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public MenuEntry fromJson(String menuEntry) throws IOException {
        return mapper.reader().forType(MenuEntry.class).readValue(menuEntry);
    }
}

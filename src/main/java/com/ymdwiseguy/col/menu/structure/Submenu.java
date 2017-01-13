package com.ymdwiseguy.col.menu.structure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class Submenu {

    private static final Logger LOGGER = getLogger(Submenu.class);
    private static final ObjectMapper mapper = new ObjectMapper();


    private String entryName;
    private List<MenuEntry> menuEntries;

    public Submenu() {
    }

    public Submenu(String entryName, List<MenuEntry> menuEntries) {
        this.entryName = entryName;
        this.menuEntries = menuEntries;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public List<MenuEntry> getMenuEntries() {
        return menuEntries;
    }

    public void setMenuEntries(List<MenuEntry> menuEntries) {
        this.menuEntries = menuEntries;
    }


    public String toJson() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Submenu fromJson(String submenu) throws IOException {
        return mapper.reader().forType(Submenu.class).readValue(submenu);
    }
}

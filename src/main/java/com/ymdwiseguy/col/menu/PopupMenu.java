package com.ymdwiseguy.col.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class PopupMenu {

    private static final Logger LOGGER = getLogger(PopupMenu.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public PopupMenu() {
    }

    public PopupMenu(String header, List<MenuEntry> menuEntries) {
        this.header = header;
        this.menuEntries = menuEntries;
    }

    private String header;
    private List<MenuEntry> menuEntries;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
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

    public PopupMenu fromJson(String sideMenu) throws IOException {
        return mapper.reader().forType(PopupMenu.class).readValue(sideMenu);
    }
}

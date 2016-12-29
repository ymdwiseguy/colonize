package com.ymdwiseguy.col.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class GameMenu {

    private static final Logger LOGGER = getLogger(GameMenu.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private List<Submenu> submenus;

    public GameMenu() {
    }

    public GameMenu(List<Submenu> submenus) {
        this.submenus = submenus;
    }

    public List<Submenu> getSubmenus() {
        return submenus;
    }

    public void setSubmenus(List<Submenu> submenus) {
        this.submenus = submenus;
    }


    public String toJson() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public GameMenu fromJson(String gameMenu) throws IOException {
        return mapper.reader().forType(GameMenu.class).readValue(gameMenu);
    }
}

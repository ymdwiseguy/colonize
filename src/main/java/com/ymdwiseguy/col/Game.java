package com.ymdwiseguy.col;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymdwiseguy.col.menu.GameMenu;
import com.ymdwiseguy.col.menu.SideMenu;
import com.ymdwiseguy.col.worldmap.WorldMap;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class Game {

    private static final Logger LOGGER = getLogger(Game.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private String gameId;
    private GameScreen gameScreen;
    private GameMenu gameMenu;
    private WorldMap worldMap;
    private SideMenu sideMenu;

    public Game() {
    }

    public Game(String gameId, GameScreen gameScreen, WorldMap worldMap) {
        this.gameId = gameId;
        this.gameScreen = gameScreen;
        this.worldMap = worldMap;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public GameMenu getGameMenu() {
        return gameMenu;
    }

    public void setGameMenu(GameMenu gameMenu) {
        this.gameMenu = gameMenu;
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public SideMenu getSideMenu() {
        return sideMenu;
    }

    public void setSideMenu(SideMenu sideMenu) {
        this.sideMenu = sideMenu;
    }

    public String toJson() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Game fromJson(String game) throws IOException {
        return mapper.reader().forType(Game.class).readValue(game);
    }
}

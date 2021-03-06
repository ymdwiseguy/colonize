package com.ymdwiseguy.col;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ymdwiseguy.col.cursor.Cursor;
import com.ymdwiseguy.col.menu.structure.GameMenu;
import com.ymdwiseguy.col.menu.structure.PopupMenu;
import com.ymdwiseguy.col.menu.structure.SideMenu;
import com.ymdwiseguy.col.worldmap.WorldMap;
import com.ymdwiseguy.col.worldmap.tile.TileType;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class Game {

    private static final Logger LOGGER = getLogger(Game.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private String gameId;
    private GameScreen gameScreen;
    private GameMenu gameMenu;
    private WorldMap worldMap;
    private SideMenu sideMenu;
    private PopupMenu popupMenu;

    private Cursor cursor;
    private TileType selectedTileType;

    public Game() {
    }

    public Game(String gameId, GameScreen gameScreen, WorldMap worldMap, Cursor cursor) {
        this.gameId = gameId;
        this.gameScreen = gameScreen;
        this.worldMap = worldMap;
        this.cursor = Objects.requireNonNull(cursor);
    }

    public Game(String gameId, GameScreen gameScreen, WorldMap worldMap, Cursor cursor, TileType selectedTileType) {
        this.gameId = gameId;
        this.gameScreen = gameScreen;
        this.worldMap = worldMap;
        this.cursor = cursor;
        this.selectedTileType = selectedTileType;
    }

    public Game(String gameId, String game_screen, String worldMapId, Cursor cursor, String tile_type) {
        this.gameId = gameId;
        try {
            this.gameScreen = GameScreen.valueOf(game_screen);
        } catch (NullPointerException ex) {
            this.gameScreen = null;
        }
        if (worldMapId != null) {
            this.worldMap = new WorldMap(worldMapId);
        }
        this.cursor = cursor;
        try {
            this.selectedTileType = TileType.valueOf(tile_type);
        } catch (NullPointerException ex) {
            this.selectedTileType = null;
        }
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

    public PopupMenu getPopupMenu() {
        return popupMenu;
    }

    public void setPopupMenu(PopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public TileType getSelectedTileType() {
        return selectedTileType;
    }

    public void setSelectedTileType(TileType selectedTileType) {
        this.selectedTileType = selectedTileType;
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

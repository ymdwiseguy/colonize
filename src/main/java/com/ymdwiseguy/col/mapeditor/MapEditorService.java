package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.GameRepo;
import com.ymdwiseguy.col.GameScreen;
import com.ymdwiseguy.col.menu.GameMenu;
import com.ymdwiseguy.col.menu.MenuEntry;
import com.ymdwiseguy.col.menu.SideMenu;
import com.ymdwiseguy.col.menu.Submenu;
import com.ymdwiseguy.col.worldmap.WorldMap;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR;

@Service
public class MapEditorService {

    private MapEditorRepo mapEditorRepo;
    private GameRepo gameRepo;


    @Inject
    public MapEditorService(MapEditorRepo mapEditorRepo, GameRepo gameRepo) {
        this.mapEditorRepo = mapEditorRepo;
        this.gameRepo = gameRepo;
    }

    public Game initGame(GameScreen gameScreen, String gameid) {
        Game mapEditor;
        if (gameid != null) {
            mapEditor = getMapEditor(gameid);
        } else {
            mapEditor = new Game();
            mapEditor.setGameScreen(gameScreen);
            saveGameState(mapEditor);
        }
        mapEditor = setMenuPoints(mapEditor);
        return mapEditor;
    }

    public SideMenu getMapList(String gameId) {
        SideMenu sideMenu = new SideMenu();
        sideMenu.setHeader("Load map...");
        sideMenu.setEntries(mapEditorRepo.getMapListFromPath(gameId));
        return sideMenu;
    }

    public Game editorWithMapList(String gameId) {
        Game mapEditor = getMapEditor(gameId);
        mapEditor.setSideMenu(getMapList(gameId));

        return mapEditor;
    }

    public Game loadMap(String gameid, String mapid) {
        Game mapEditor = getMapEditor(gameid);

        mapEditor.setGameScreen(MAPEDITOR);
        WorldMap worldMap = getMap(mapid);
        mapEditor.setWorldMap(worldMap);
        return mapEditor;
    }

    public Game getMapEditor(String gameId) {
        return gameRepo.getGame(gameId)
            .map(game -> {
                game.setGameScreen(MAPEDITOR);
                game = setMenuPoints(game);
                return game;
            }).orElse(null);
    }

    public WorldMap getMap(String mapid) {
        return mapEditorRepo.getWorldmap(mapid);
    }

    private Game saveGameState(Game game) {
        Game savedGame = gameRepo.createGame(game);
        return savedGame;
    }

    private Game setMenuPoints(Game mapeditor) {
        MenuEntry loadMap = new MenuEntry("Load Map", "/api/mapeditor/" + mapeditor.getGameId() + "/maps/");
        List<MenuEntry> menuEntries = new ArrayList<>();
        menuEntries.add(0, loadMap);

        Submenu editorSubmenu = new Submenu("Editor", menuEntries);
        List<Submenu> submenus = new ArrayList<>();
        submenus.add(editorSubmenu);

        GameMenu gameMenu = new GameMenu(submenus);
        mapeditor.setGameMenu(gameMenu);
        return mapeditor;
    }


}

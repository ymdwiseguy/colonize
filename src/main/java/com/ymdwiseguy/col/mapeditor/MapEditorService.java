package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.GameRepo;
import com.ymdwiseguy.col.GameScreen;
import com.ymdwiseguy.col.menu.GameMenu;
import com.ymdwiseguy.col.menu.MenuEntry;
import com.ymdwiseguy.col.menu.SideMenu;
import com.ymdwiseguy.col.menu.Submenu;
import com.ymdwiseguy.col.worldmap.WorldMap;
import com.ymdwiseguy.col.worldmap.WorldMapRepo;
import com.ymdwiseguy.col.worldmap.WorldMapService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR;

@Service
public class MapEditorService {

    private MapEditorRepo mapEditorRepo;
    private WorldMapRepo worldMapRepo;
    private GameRepo gameRepo;
    private WorldMapService worldMapService;


    @Inject
    public MapEditorService(MapEditorRepo mapEditorRepo, WorldMapRepo worldMapRepo, GameRepo gameRepo, WorldMapService worldMapService) {
        this.mapEditorRepo = mapEditorRepo;
        this.worldMapRepo = worldMapRepo;
        this.gameRepo = gameRepo;
        this.worldMapService = worldMapService;
    }

    Game initGame(GameScreen gameScreen, String gameid) {
        Game mapEditor;
        if (gameid != null) {
            mapEditor = getMapEditor(gameid);
        } else {
            mapEditor = new Game();
            mapEditor.setGameScreen(gameScreen);
            saveGameState(mapEditor);
        }
        mapEditor = setMenuPoints(mapEditor);
        mapEditor = setWorldMap(mapEditor);
        return mapEditor;
    }

    Game editorWithMapList(String gameId) {
        Game mapEditor = getMapEditor(gameId);
        mapEditor = setMenuPoints(mapEditor);
        mapEditor = setWorldMap(mapEditor);
        mapEditor.setSideMenu(getMapList(gameId));

        return mapEditor;
    }

    Game loadMap(String gameid, String mapName) {
        Game mapEditor = getMapEditor(gameid);

        mapEditor.setGameScreen(MAPEDITOR);
        WorldMap worldMap = getMap(mapName);
        mapEditor.setWorldMap(worldMap);

        updateGameState(mapEditor);

        return mapEditor;
    }

    private SideMenu getMapList(String gameId) {
        SideMenu sideMenu = new SideMenu();
        sideMenu.setHeader("Load map...");
        sideMenu.setEntries(mapEditorRepo.getMapListFromPath(gameId));
        return sideMenu;
    }

    private Game getMapEditor(String gameId) {
        return gameRepo.getGame(gameId)
            .map(game -> {
                game.setGameScreen(MAPEDITOR);
                game = setMenuPoints(game);
                return game;
            }).orElse(null);
    }

    private WorldMap getMap(String mapid) {
        WorldMap worldMap = mapEditorRepo.getWorldmap(mapid);
        String worldMapId = worldMapService.saveNewWorldMap(worldMap).getWorldMapId();
        worldMap.setWorldMapId(worldMapId);
        return worldMap;
    }

    private Game saveGameState(Game game) {
        Game savedGame = gameRepo.createGame(game);
        return savedGame;
    }

    private Game updateGameState(Game game) {
        Optional<Game> savedGame = gameRepo.updateGame(game);
        return savedGame.orElse(null);
    }


    private Game setWorldMap(Game mapEditor) {
        if(mapEditor.getWorldMap() == null){
            return mapEditor;
        }
        String worldMapId = mapEditor.getWorldMap().getWorldMapId();
        if(worldMapId != null){
            mapEditor.setWorldMap(worldMapService.getWorldMap(worldMapId));
        }
        return mapEditor;
    }

    private Game setMenuPoints(Game mapeditor) {
        MenuEntry loadMap = new MenuEntry("Load Map ...", "/api/mapeditor/" + mapeditor.getGameId() + "/maps/");
        // TODO: add "saveName" to maps
//        MenuEntry saveMap = new MenuEntry("Save Map As ...", "/api/mapeditor/" + mapeditor.getGameId() + "/maps/" + mapeditor.getWorldMap().getWorldMapId());
        List<MenuEntry> menuEntries = new ArrayList<>();
        menuEntries.add(0, loadMap);
//        menuEntries.add(1, saveMap);

        Submenu editorSubmenu = new Submenu("Editor", menuEntries);
        List<Submenu> submenus = new ArrayList<>();
        submenus.add(editorSubmenu);

        GameMenu gameMenu = new GameMenu(submenus);
        mapeditor.setGameMenu(gameMenu);
        return mapeditor;
    }


}

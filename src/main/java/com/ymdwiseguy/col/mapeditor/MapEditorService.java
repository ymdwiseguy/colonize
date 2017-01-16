package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.GameRepo;
import com.ymdwiseguy.col.GameScreen;
import com.ymdwiseguy.col.menu.implementation.SaveGamePopupMenu;
import com.ymdwiseguy.col.menu.structure.GameMenu;
import com.ymdwiseguy.col.menu.structure.MenuEntry;
import com.ymdwiseguy.col.menu.structure.PopupMenu;
import com.ymdwiseguy.col.menu.structure.PopupType;
import com.ymdwiseguy.col.menu.structure.SideMenu;
import com.ymdwiseguy.col.menu.structure.Submenu;
import com.ymdwiseguy.col.worldmap.WorldMap;
import com.ymdwiseguy.col.worldmap.WorldMapService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR;

@Service
public class MapEditorService {

    private MapEditorRepo mapEditorRepo;
    private GameRepo gameRepo;
    private WorldMapService worldMapService;


    @Inject
    public MapEditorService(MapEditorRepo mapEditorRepo, GameRepo gameRepo, WorldMapService worldMapService) {
        this.mapEditorRepo = mapEditorRepo;
        this.gameRepo = gameRepo;
        this.worldMapService = worldMapService;
    }

    // CREATE
    Game initGame(GameScreen gameScreen, String gameid, PopupType showPopup) {
        Game mapEditor;
        if (gameid != null) {
            mapEditor = getMapEditor(gameid);
        } else {
            mapEditor = new Game();
            mapEditor.setGameScreen(gameScreen);
            saveGameState(mapEditor);
        }
        mapEditor = addStaticData(mapEditor, showPopup);
        return mapEditor;
    }

    // GET
    Game editorWithMapList(String gameId) {
        Game mapEditor = getMapEditor(gameId);
        mapEditor = setMenuPoints(mapEditor);
        mapEditor = setWorldMap(mapEditor);
        mapEditor.setSideMenu(getMapList(gameId));

        return mapEditor;
    }

    // GET
    Game loadMap(String gameid, String mapName) {
        Game mapEditor = getMapEditor(gameid);

        mapEditor.setGameScreen(MAPEDITOR);
        WorldMap worldMap = getMap(mapName);
        mapEditor.setWorldMap(worldMap);

        mapEditor = updateGameState(mapEditor, null);

        return mapEditor;
    }

    // UPDATE
    Game updateMap(Game mapEditor, String mapName) {

        if (!Objects.equals(mapEditor.getWorldMap().getWorldMapName(), mapName)) {
            return null;
        }

        if (!mapEditorRepo.fileExists(mapName)) {
            return null;
        }

        if (mapEditorRepo.updateWorldMap(mapName, mapEditor.getWorldMap().toJson())) {
            mapEditor = updateGameState(mapEditor, null);
            return mapEditor;

        }
        return null;
    }


//  -- private methods .. TODO: move stuff to own classes

    private Game addStaticData(Game mapEditor, PopupType showPopup){
        mapEditor = setMenuPoints(mapEditor);
        mapEditor = setWorldMap(mapEditor);
        if (showPopup != null) {
            mapEditor = setPopup(mapEditor, showPopup);
        }
        return mapEditor;
    }

    private Game setMenuPoints(Game mapeditor) {
        MenuEntry loadMap = new MenuEntry("Load Map ...", "/api/mapeditor/" + mapeditor.getGameId() + "/maps/");
        MenuEntry saveMap = new MenuEntry("Save map", "/api/mapeditor/" + mapeditor.getGameId() + "?showPopup=SAVE_MAPEDITOR");
        List<MenuEntry> menuEntries = new ArrayList<>();
        menuEntries.add(loadMap);
        menuEntries.add(saveMap);

        Submenu editorSubmenu = new Submenu("Editor", menuEntries);
        List<Submenu> submenus = new ArrayList<>();
        submenus.add(editorSubmenu);

        GameMenu gameMenu = new GameMenu(submenus);
        mapeditor.setGameMenu(gameMenu);
        return mapeditor;
    }

    private Game setPopup(Game mapEditor, PopupType showPopup) {
        PopupMenu popupMenu;
        switch (showPopup) {
            case SAVE_MAPEDITOR:
                popupMenu = new SaveGamePopupMenu().create(mapEditor);
                break;
            default:
                popupMenu = null;
        }
        mapEditor.setPopupMenu(popupMenu);
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

    private WorldMap getMap(String mapName) {
        WorldMap worldMap = mapEditorRepo.getWorldmap(mapName);
        if (worldMap.getWorldMapId() == null) {
            String worldMapId = worldMapService.saveNewWorldMap(worldMap).getWorldMapId();
            worldMap.setWorldMapId(worldMapId);
        }
        return worldMap;
    }

    private Game saveGameState(Game game) {
        return gameRepo.createGame(game);
    }

    private Game updateGameState(Game game, PopupType showPopup) {
        Optional<Game> savedGame = gameRepo.updateGame(game);

        if(savedGame.isPresent()){
            Game sg = savedGame.get();
            return addStaticData(sg, showPopup);
        }

        return null;
    }

    private Game setWorldMap(Game mapEditor) {
        if (mapEditor.getWorldMap() == null) {
            return mapEditor;
        }
        String worldMapId = mapEditor.getWorldMap().getWorldMapId();
        if (worldMapId != null) {
            mapEditor.setWorldMap(worldMapService.getWorldMap(worldMapId));
        }
        return mapEditor;
    }
}

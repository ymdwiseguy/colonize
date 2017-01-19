package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.GameRepo;
import com.ymdwiseguy.col.GameScreen;
import com.ymdwiseguy.col.menu.implementation.EditorMainMenu;
import com.ymdwiseguy.col.menu.implementation.SaveGamePopupMenu;
import com.ymdwiseguy.col.menu.structure.PopupMenu;
import com.ymdwiseguy.col.menu.structure.PopupType;
import com.ymdwiseguy.col.worldmap.WorldMap;
import com.ymdwiseguy.col.worldmap.WorldMapService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR;

@Service
public class MapEditorService {

    private MapFileRepo mapFileRepo;
    private GameRepo gameRepo;
    private WorldMapService worldMapService;
    private EditorMainMenu editorMainMenu;
    private MapEditorRepo mapEditorRepo;


    @Inject
    public MapEditorService(MapFileRepo mapFileRepo, GameRepo gameRepo, WorldMapService worldMapService, EditorMainMenu editorMainMenu, MapEditorRepo mapEditorRepo) {
        this.mapFileRepo = mapFileRepo;
        this.gameRepo = gameRepo;
        this.worldMapService = worldMapService;
        this.editorMainMenu = editorMainMenu;
        this.mapEditorRepo = mapEditorRepo;
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
    Game editorWithMapList(String gameId, PopupType showPopup) {
        Game mapEditor = getMapEditor(gameId);
        mapEditor = addStaticData(mapEditor, showPopup);
        return mapEditor;
    }

    private Game updateGameState(Game game) {
        Optional<Game> savedGame = gameRepo.updateGame(game);

        if (savedGame.isPresent()) {
            Game sg = savedGame.get();
            return addStaticData(sg, null);
        }

        return null;
    }

    private Game addStaticData(Game mapEditor, PopupType showPopup) {
        mapEditor.setGameMenu(editorMainMenu.create(mapEditor));
        mapEditor = setWorldMap(mapEditor);
        if (showPopup != null) {
            mapEditor = setPopup(mapEditor, showPopup);
        }
        return mapEditor;
    }

    // GET
    Game loadMap(String gameid, String mapName) {
        Game mapEditor = getMapEditor(gameid);

        mapEditor.setGameScreen(MAPEDITOR);
        WorldMap worldMap = getMap(mapName);
        mapEditor.setWorldMap(worldMap);

//        mapEditor = updateGameState(mapEditor);

        mapEditor = mapEditorRepo.update(mapEditor);
        mapEditor = setPopup(mapEditor, null);

        return mapEditor;
    }

    // UPDATE
    Game updateMap(Game mapEditor, String mapName) {

        if (!Objects.equals(mapEditor.getWorldMap().getWorldMapName(), mapName)) {
            return null;
        }

        if (!mapFileRepo.fileExists(mapName)) {
            return null;
        }

        if (mapFileRepo.updateWorldMap(mapName, mapEditor.getWorldMap())) {
            mapEditor = updateGameState(mapEditor);
            return mapEditor;

        }
        return null;
    }


//  -- private methods .. TODO: move stuff to own classes

    private Game setPopup(Game mapEditor, PopupType showPopup) {

        if (showPopup == null) {
            return mapEditor;
        }

        PopupMenu popupMenu;

        switch (showPopup) {
            case SAVE_MAPEDITOR:
                popupMenu = new SaveGamePopupMenu().create(mapEditor);
                break;
            case SHOW_MAPLIST:
                popupMenu = new PopupMenu(
                    "Load map...",
                    mapFileRepo.getMapListFromPath(mapEditor.getGameId()),
                    PopupType.SHOW_MAPLIST);
                break;
            default:
                popupMenu = null;
        }

        mapEditor.setPopupMenu(popupMenu);
        return mapEditor;
    }

    private Game getMapEditor(String gameId) {
        return gameRepo.getGame(gameId)
            .map(mapEditor -> {
                mapEditor.setGameScreen(MAPEDITOR);
                mapEditor.setGameMenu(editorMainMenu.create(mapEditor));

                return mapEditor;
            }).orElse(null);
    }

    private WorldMap getMap(String mapName) {
        WorldMap worldMap = mapFileRepo.getWorldmap(mapName);
        if (worldMap.getWorldMapId() == null) {
            String worldMapId = worldMapService.saveNewWorldMap(worldMap).getWorldMapId();
            worldMap.setWorldMapId(worldMapId);
        }
        return worldMap;
    }

    private Game saveGameState(Game game) {
        return gameRepo.createGame(game);
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

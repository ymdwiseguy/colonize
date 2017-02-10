package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.GameRepo;
import com.ymdwiseguy.col.menu.implementation.EditorMainMenu;
import com.ymdwiseguy.col.menu.implementation.SideMenuSelectTiles;
import com.ymdwiseguy.col.worldmap.WorldMapService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Optional;

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR;

@Component
public class MapEditorRepo {

    private GameRepo gameRepo;
    private EditorMainMenu editorMainMenu;
    private WorldMapService worldMapService;
    private SideMenuSelectTiles sideMenuSelectTiles;

    @Inject
    public MapEditorRepo(GameRepo gameRepo, EditorMainMenu editorMainMenu, WorldMapService worldMapService, SideMenuSelectTiles sideMenuSelectTiles) {
        this.gameRepo = gameRepo;
        this.editorMainMenu = editorMainMenu;
        this.worldMapService = worldMapService;
        this.sideMenuSelectTiles = sideMenuSelectTiles;
    }

    public Game getMapEditor(String gameId) {
        return gameRepo.getGame(gameId)
            .map(mapEditor -> {
                mapEditor.setGameScreen(MAPEDITOR);
                mapEditor.setGameMenu(editorMainMenu.create(mapEditor));
                mapEditor.setSideMenu(sideMenuSelectTiles.create(mapEditor));

                mapEditor = setWorldMap(mapEditor);

                return mapEditor;
            }).orElse(null);
    }

    public Game update(Game mapEditor) {
        Optional<Game> savedGame = gameRepo.updateGame(mapEditor);

        if(savedGame.isPresent()){
            Game sg = savedGame.get();
            sg.setGameMenu(editorMainMenu.create(sg));
            mapEditor.setSideMenu(sideMenuSelectTiles.create(mapEditor));

            sg = setWorldMap(sg);
            return sg;
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

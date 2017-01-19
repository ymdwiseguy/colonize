package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.GameRepo;
import com.ymdwiseguy.col.menu.implementation.EditorMainMenu;
import com.ymdwiseguy.col.worldmap.WorldMapService;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Optional;

@Component
public class MapEditorRepo {

    private GameRepo gameRepo;
    private EditorMainMenu editorMainMenu;
    private WorldMapService worldMapService;

    @Inject
    public MapEditorRepo(GameRepo gameRepo, EditorMainMenu editorMainMenu, WorldMapService worldMapService) {
        this.gameRepo = gameRepo;
        this.editorMainMenu = editorMainMenu;
        this.worldMapService = worldMapService;
    }

    public Game update(Game mapEditor) {
        Optional<Game> savedGame = gameRepo.updateGame(mapEditor);

        if(savedGame.isPresent()){
            Game sg = savedGame.get();
            return addStaticData(sg);
        }

        return null;
    }


    private Game addStaticData(Game mapEditor){
        mapEditor.setGameMenu(editorMainMenu.create(mapEditor));
        mapEditor = setWorldMap(mapEditor);
        return mapEditor;
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

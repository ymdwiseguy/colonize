package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.menu.GameMenu;
import com.ymdwiseguy.col.menu.MenuEntry;
import com.ymdwiseguy.col.menu.Submenu;
import com.ymdwiseguy.col.worldmap.WorldMap;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class MapEditorController {

    private static final Logger LOGGER = getLogger(MapEditorController.class);

    private final MapEditorView mapEditorView;
    private final MapEditorService mapEditorService;

    @Inject
    public MapEditorController(MapEditorView mapEditorView, MapEditorService mapEditorService) {
        this.mapEditorView = mapEditorView;
        this.mapEditorService = mapEditorService;
    }

    // GET - HTML
    @RequestMapping(value = "/mapeditor")
    public ResponseEntity startMapEditor() {

        Game mapeditor = new Game();
        mapeditor.setGameScreen(MAPEDITOR);
        mapeditor = setMenuPoints(mapeditor);

        return new ResponseEntity<>(mapEditorView.render(mapeditor.toJson()), HttpStatus.OK);
    }

    // GET - JSON
    @RequestMapping(value = "/api/mapeditor", method = GET, produces = "application/hal+json")
    public ResponseEntity getMapEditorData() {

        Game mapeditor = new Game();
        mapeditor.setGameScreen(MAPEDITOR);
        mapeditor = setMenuPoints(mapeditor);

        return new ResponseEntity<>(mapeditor.toJson(), HttpStatus.OK);
    }

    // GET LIST - JSON
    @RequestMapping(value = "/mapeditor/maps", method = GET, produces = "application/hal+json")
    public ResponseEntity getMapList() {

        Game mapeditor = new Game();
        mapeditor.setGameScreen(MAPEDITOR);
        mapeditor = setMenuPoints(mapeditor);
        mapeditor.setSideMenu(mapEditorService.getMapList());

        return new ResponseEntity<>(mapeditor.toJson(), HttpStatus.CREATED);
    }


    // GET MAP - JSON
    @RequestMapping(value = "/mapeditor/maps/{mapid}", method = GET)
    public ResponseEntity loadMap(@PathVariable String mapid) {
//        mapConfigurationReader.setFilename(mapid);
//        String worldMapData = mapConfigurationReader.read().orElse("[]");


        Game mapeditor = new Game();
        mapeditor.setGameScreen(MAPEDITOR);
        mapeditor = setMenuPoints(mapeditor);
        WorldMap worldMap = mapEditorService.getMap(mapid);
        mapeditor.setWorldMap(worldMap);

        return new ResponseEntity<>(mapeditor.toJson(), HttpStatus.OK);
    }

    private Game setMenuPoints(Game mapeditor) {
        MenuEntry loadMap = new MenuEntry("Load Map", "/mapeditor/maps/");
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

package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import java.util.Objects;

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

    // GET - HTML initial
    @RequestMapping(value = "/mapeditor", method = GET)
    public ResponseEntity startMapEditor() {
        return initMapEditor(null, null);
    }

    // GET - HTML with ID
    @RequestMapping(value = "/mapeditor/{gameid}", method = GET)
    public ResponseEntity getMapEditorData(@PathVariable String gameid) {
        return initMapEditor(gameid, null);
    }

    // GET - JSON initial
    @RequestMapping(value = "/api/mapeditor", method = GET, produces = "application/json")
    public ResponseEntity startMapEditorJson() {
        return initMapEditor(null, "application/json");
    }

    // GET - JSON with ID
    @RequestMapping(value = "/api/mapeditor/{gameid}", method = GET, produces = "application/json")
    public ResponseEntity getMapEditorDataJson(@PathVariable String gameid) {
        return initMapEditor(gameid, "application/json");
    }

    private ResponseEntity initMapEditor(String gameId, String produces) {
        Game mapEditor = mapEditorService.initGame(MAPEDITOR, gameId);
        if (Objects.equals(produces, "application/json")) {
            return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
        }
        return new ResponseEntity<>(mapEditorView.render(mapEditor.toJson()), HttpStatus.OK);
    }

    // GET LIST - JSON
    @RequestMapping(value = "/api/mapeditor/{gameid}/maps", method = GET, produces = "application/json")
    public ResponseEntity getMapList(@PathVariable String gameid) {
        Game mapEditor = mapEditorService.editorWithMapList(gameid);
        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

    // GET MAP - JSON
    @RequestMapping(value = "/api/mapeditor/{gameId}/maps/{mapName}", method = GET)
    public ResponseEntity loadMap(@PathVariable String gameId, @PathVariable String mapName) {
        Game mapEditor = mapEditorService.loadMap(gameId, mapName);
        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

//    // POST MAP - JSON
//    @RequestMapping(value = "/api/mapeditor/{gameId}/maps/{mapName}", method = POST)
//    public ResponseEntity saveMap(@PathVariable String gameId, @PathVariable String mapName) {
//        Game mapEditor = mapEditorService.saveMap(gameId, mapName);
//        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
//    }
//
//    // PUT MAP - JSON
//    @RequestMapping(value = "/api/mapeditor/{gameId}/maps/{mapName}", method = PUT)
//    public ResponseEntity updateMap(@PathVariable String gameId, @PathVariable String mapName) {
//        Game mapEditor = mapEditorService.updateMap(gameId, mapName);
//        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
//    }

}

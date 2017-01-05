package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.menu.PopupType;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Objects;

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


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
    public ResponseEntity startMapEditor(@RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(null, null, showPopup);
    }

    // GET - HTML with ID
    @RequestMapping(value = "/mapeditor/{gameid}", method = GET)
    public ResponseEntity getMapEditorData(@PathVariable String gameid, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(gameid, null, showPopup);
    }

    // GET - JSON initial
    @RequestMapping(value = "/api/mapeditor", method = GET, produces = "application/json")
    public ResponseEntity startMapEditorJson(@RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(null, "application/json", showPopup);
    }

    // GET - JSON with ID
    @RequestMapping(value = "/api/mapeditor/{gameid}", method = GET, produces = "application/json")
    public ResponseEntity getMapEditorDataJson(@PathVariable String gameid, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(gameid, "application/json", showPopup);
    }

    private ResponseEntity initMapEditor(String gameId, String produces, PopupType showPopup) {
        Game mapEditor = mapEditorService.initGame(MAPEDITOR, gameId, showPopup);
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
    // PUT MAP - JSON
    @RequestMapping(value = "/api/mapeditor/{gameId}/maps/{mapName}", method = PUT)
    public ResponseEntity updateMap(@RequestBody String gameJson, @PathVariable String mapName) {
        Game game;
        try {
            game = new Game().fromJson(gameJson);
        } catch (IOException e) {
            LOGGER.info("Invalid json data: {}", e);
            return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        }
        Game mapEditor = mapEditorService.updateMap(game, mapName);
        if(mapEditor == null){
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

}

package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.menu.structure.PopupType;
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

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


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
    @RequestMapping(value = "/mapeditor/{gameId}", method = GET)
    public ResponseEntity getMapEditorData(@PathVariable String gameId, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(gameId, null, showPopup);
    }

    // GET - JSON initial
    @RequestMapping(value = "/api/mapeditor", method = GET, produces = "application/json")
    public ResponseEntity startMapEditorJson(@RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(null, "application/json", showPopup);
    }

    // GET - JSON with ID
    @RequestMapping(value = "/api/mapeditor/{gameId}", method = GET, produces = "application/json")
    public ResponseEntity getMapEditorDataJson(@PathVariable String gameId, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(gameId, "application/json", showPopup);
    }

    private ResponseEntity initMapEditor(String gameId, String produces, PopupType showPopup) {
//        Game mapEditor = mapEditorService.initGame(gameId, showPopup);
        Game mapEditor = mapEditorService.generateMap(null, "New Map", 56, 70, "new_map", showPopup);
        if (Objects.equals(produces, "application/json")) {
            return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
        }
        return new ResponseEntity<>(mapEditorView.render(mapEditor.toJson()), HttpStatus.OK);
    }

    // GET MAP LIST - JSON
    @RequestMapping(value = "/api/mapeditor/{gameId}/maps", method = GET, produces = "application/json")
    public ResponseEntity getMapList(@PathVariable String gameId, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        Game mapEditor = mapEditorService.initGame(gameId, showPopup);
        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

    // GET MAP - JSON
    @RequestMapping(value = "/api/mapeditor/{gameId}/maps/{mapName}", method = GET)
    public ResponseEntity loadMap(@PathVariable String gameId, @PathVariable String mapName) {
        Game mapEditor = mapEditorService.loadMap(gameId, mapName);
        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

    // GET MAP - JSON
    @RequestMapping(value = "/api/mapeditor/{gameId}/maps/generate", method = POST, produces = "application/json")
    public ResponseEntity generateMap(@PathVariable String gameId, @RequestBody String formDataJson) {
        try {
            FormData formData = new FormData().fromJson(formDataJson);
            Game mapEditor = mapEditorService.generateMap(gameId, formData.getTitle(), formData.getWidth(), formData.getHeight(), formData.getName(), null);
            return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.info("Could not create FormData Object, reason: {}", e);
            return initMapEditor(gameId, null, null);
        }
    }

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
        if (mapEditor == null) {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

}

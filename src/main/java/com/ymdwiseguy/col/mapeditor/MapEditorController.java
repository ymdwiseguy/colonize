package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.cursor.CursorMovementService;
import com.ymdwiseguy.col.menu.structure.PopupType;
import com.ymdwiseguy.col.worldmap.movement.UnitDirection;
import com.ymdwiseguy.col.worldmap.tile.Tile;
import com.ymdwiseguy.col.worldmap.tile.TileAssetToggler;
import com.ymdwiseguy.col.worldmap.tile.TileAssetType;
import com.ymdwiseguy.col.worldmap.tile.TileRepo;
import com.ymdwiseguy.col.worldmap.tile.TileType;
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
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@RestController
public class MapEditorController {

    private static final Logger LOGGER = getLogger(MapEditorController.class);
    private final MapEditorView mapEditorView;
    private final MapEditorService mapEditorService;
    private final CursorMovementService cursorMovementService;
    private final MapEditorRepo mapEditorRepo;
    private final TileRepo tileRepo;
    private final TileAssetToggler tileAssetToggler;

    @Inject
    public MapEditorController(MapEditorView mapEditorView, MapEditorService mapEditorService, CursorMovementService cursorMovementService, MapEditorRepo mapEditorRepo, TileRepo tileRepo, TileAssetToggler tileAssetToggler) {
        this.mapEditorView = mapEditorView;
        this.mapEditorService = mapEditorService;
        this.cursorMovementService = cursorMovementService;
        this.mapEditorRepo = mapEditorRepo;
        this.tileRepo = tileRepo;
        this.tileAssetToggler = tileAssetToggler;
    }

    // GET - map editor HTML initial
    @RequestMapping(value = "/mapeditor", method = GET)
    public ResponseEntity startMapEditor(@RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(null, null, showPopup);
    }

    // GET - map editor HTML with ID
    @RequestMapping(value = "/mapeditor/{gameId}", method = GET)
    public ResponseEntity getMapEditorData(@PathVariable String gameId, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(gameId, null, showPopup);
    }

    // GET - map editor JSON initial
    @RequestMapping(value = "/api/mapeditor", method = GET, produces = "application/json")
    public ResponseEntity startMapEditorJson(@RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(null, "application/json", showPopup);
    }

    // GET - map editor JSON with ID
    @RequestMapping(value = "/api/mapeditor/{gameId}", method = GET, produces = "application/json")
    public ResponseEntity getMapEditorDataJson(@PathVariable String gameId, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return initMapEditor(gameId, "application/json", showPopup);
    }

    private ResponseEntity initMapEditor(String gameId, String produces, PopupType showPopup) {
        Game mapEditor = mapEditorService.initGame(gameId, showPopup);
        if (Objects.equals(produces, "application/json")) {
            return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
        }
        return new ResponseEntity<>(mapEditorView.render(mapEditor.toJson()), HttpStatus.OK);
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
            Game mapEditor = mapEditorService.generateMap(gameId, formData.getTitle(), formData.getWidth(), formData.getHeight(), formData.getName());
            return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.info("Could not create FormData Object, reason: {}", e);
            return initMapEditor(gameId, null, null);
        }
    }

    // PUT MAP - JSON
    @RequestMapping(value = "/api/mapeditor/{gameId}/maps/{mapName}", method = PUT)
    public ResponseEntity updateMap(@RequestBody String gameJson, @PathVariable String mapName, @PathVariable String gameId) {
        Game game;
        try {
            game = new Game().fromJson(gameJson);
        } catch (IOException e) {
            LOGGER.info("Invalid json data: {}", e);
            return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(game.getGameId(), gameId)) {
            return new ResponseEntity<>("{}", HttpStatus.FORBIDDEN);
        }
        Game mapEditor = mapEditorService.updateMap(game, mapName);
        if (mapEditor == null) {
            return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

    // PUT move cursor - JSON
    @RequestMapping(value = "/api/mapeditor/{gameId}/movecursor/{direction}", method = PUT, produces = "application/json")
    public ResponseEntity moveCursor(@PathVariable String gameId, @PathVariable UnitDirection direction, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        Game mapEditor = mapEditorService.initGame(gameId, showPopup);
        mapEditor = cursorMovementService.moveCursor(mapEditor, direction);
        mapEditor = mapEditorRepo.update(mapEditor);
        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

    // PUT put cursor to position - JSON
    @RequestMapping(value = "/api/mapeditor/{gameId}/movecursor/{xPosition}/{yPosition}", method = PUT, produces = "application/json")
    public ResponseEntity putCursor(@PathVariable String gameId, @PathVariable int xPosition, @PathVariable int yPosition, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        Game mapEditor = mapEditorService.initGame(gameId, showPopup);
        mapEditor = cursorMovementService.putCursor(mapEditor, xPosition, yPosition);
        mapEditor = mapEditorRepo.update(mapEditor);
        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

    // PUT Menu click - set active tile type - JSON
    @RequestMapping(value = "/api/mapeditor/{gameId}/selecttiletype/{tileType}", method = PUT, produces = "application/json")
    public ResponseEntity selectTileType(@PathVariable String gameId, @PathVariable TileType tileType, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        Game mapEditor = mapEditorService.initGame(gameId, showPopup);
        mapEditor.setSelectedTileType(tileType);
        mapEditor = mapEditorRepo.update(mapEditor);
        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

    // PUT Overwrite tile with active tile type - JSON
    @RequestMapping(value = "/api/mapeditor/{gameId}/activetile", method = PUT, produces = "application/json")
    public ResponseEntity setActiveTile(@PathVariable String gameId, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {

        Game mapEditor = mapEditorService.initGame(gameId, showPopup);
        int cursorX = mapEditor.getCursor().getxPosition();
        int cursorY = mapEditor.getCursor().getyPosition();

        Tile tile = mapEditor.getWorldMap().getTileByCoordinates(cursorX, cursorY);
        tile.setType(mapEditor.getSelectedTileType());
        tileRepo.updateTile(tile);

        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }

    // PUT toggle tile asset
    @RequestMapping(value = "/api/mapeditor/{gameId}/toggleasset/{tileAssetType}", method = PUT, produces = "application/json")
    public ResponseEntity toggleTileAsset(@PathVariable String gameId, @PathVariable TileAssetType tileAssetType, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {

        Game mapEditor = mapEditorService.initGame(gameId, showPopup);
        int cursorX = mapEditor.getCursor().getxPosition();
        int cursorY = mapEditor.getCursor().getyPosition();

        Tile tile = mapEditor.getWorldMap().getTileByCoordinates(cursorX, cursorY);
        tile = tileAssetToggler.toggleAsset(tile, tileAssetType);
        tileRepo.updateTile(tile);

        return new ResponseEntity<>(mapEditor.toJson(), HttpStatus.OK);
    }
}

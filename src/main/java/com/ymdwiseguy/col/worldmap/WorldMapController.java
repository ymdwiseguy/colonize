package com.ymdwiseguy.col.worldmap;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.filehandling.MapFileHandler;
import com.ymdwiseguy.col.views.WorldMapView;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.ymdwiseguy.col.GameScreen.WORLDMAP;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@RestController
public class WorldMapController {

    private static final Logger LOGGER = getLogger(WorldMapController.class);

    private final WorldMapView worldMapView;
    private final WorldMapService worldMapService;
    private final MapFileHandler mapFileHandler;

    @Autowired
    public WorldMapController(WorldMapView worldMapView, WorldMapService worldMapService, MapFileHandler mapFileHandler) {
        this.worldMapView = worldMapView;
        this.worldMapService = worldMapService;
        this.mapFileHandler = mapFileHandler;
    }


    // NEW (generated) - HTML
    @RequestMapping(value = "/maps/generate/{width}/{height}", method = GET)
    public String generateMap(@PathVariable int width, @PathVariable int height) {
        WorldMap worldMapData = worldMapService.generateMap(width, height);
        worldMapService.saveNewWorldMap(worldMapData);

        Game game = new Game();
        game.setGameScreen(WORLDMAP);
        game.setWorldMap(worldMapData);

        return worldMapView.render(game.toJson());
    }

    // NEW (generated) - JSON
    @RequestMapping(value = "/api/maps/generate/{width}/{height}", method = POST, produces = "application/json")
    public ResponseEntity generateMapJson(@PathVariable int width, @PathVariable int height) {
        WorldMap worldMapData = worldMapService.generateMap(width, height);
        worldMapService.saveNewWorldMap(worldMapData);
        return new ResponseEntity<>(worldMapData.toJson(), HttpStatus.CREATED);
    }

    // NEW - JSON
    @RequestMapping(value = "/api/maps", method = POST, produces = "application/json")
    public ResponseEntity createMap(@RequestBody String worldMapJson) {
        try {
            WorldMap worldMap = new WorldMap().fromJson(worldMapJson);
            worldMapService.saveNewWorldMap(worldMap);
            return new ResponseEntity<>(worldMap.toJson(), HttpStatus.CREATED);
        } catch (IOException e) {
            LOGGER.error("unable to save map: {}", e);
            return new ResponseEntity<>("{unable to save map}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - JSON
    @RequestMapping(value = "/api/maps/{mapid}", method = PUT, produces = "application/json")
    public ResponseEntity updateMap(@RequestBody String worldMapJson, @PathVariable String mapid) {
        LOGGER.info(worldMapJson);
        try {
            WorldMap worldMap = new WorldMap().fromJson(worldMapJson);
            if (!mapid.equals(worldMap.getWorldMapId())) {
                LOGGER.info("received invalid data for mapid {}", mapid);
                return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
            }
            WorldMap updatedWorldmap = worldMapService.updateWorldMap(worldMap);
            if (updatedWorldmap == null) {
                return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedWorldmap.toJson(), HttpStatus.OK);
        } catch (IOException e) {
            LOGGER.error("unable to save map: {}", e);
            return new ResponseEntity<>("{}", HttpStatus.BAD_REQUEST);
        }
    }


    // GET - JSON
    @RequestMapping(value = "/api/maps/{mapid}", method = GET)
    public ResponseEntity loadMap(@PathVariable String mapid) {
        WorldMap worldMap = worldMapService.getWorldMap(mapid);
        if(worldMap != null){
            return new ResponseEntity<>(worldMap.toJson(), HttpStatus.OK);
        }
        return new ResponseEntity<>("{}", HttpStatus.NOT_FOUND);
    }

    // GET - JSON
    @RequestMapping(value = "/maps/{name}")
    public ResponseEntity loadMapFromFile(@PathVariable String name) {
        String worldMapData = mapFileHandler.readDataFromFile(name).orElse("[]");

//        For now: just load the map data without initializing a game
//        WorldMap worldMap = worldMapService.saveWorldMapFromJson(worldMapData);
//        Game game = new Game();
//        game.setGameScreen(WORLDMAP);
//        game.setWorldMap(worldMap);

        return new ResponseEntity<>(worldMapData, HttpStatus.CREATED);
    }


}

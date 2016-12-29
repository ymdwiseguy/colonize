package com.ymdwiseguy.col.worldmap.movement;

import com.ymdwiseguy.col.worldmap.WorldMap;
import com.ymdwiseguy.col.worldmap.WorldMapService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
public class MovementController {

    private static final Logger LOGGER = getLogger(MovementController.class);

    private final MovementService movementService;
    private final WorldMapService worldMapService;

    @Autowired
    public MovementController(MovementService movementService, WorldMapService worldMapService) {
        this.movementService = movementService;
        this.worldMapService = worldMapService;
    }

    // UPDATE - JSON - MOVE UNIT
    @RequestMapping(value = "/api/maps/{mapid}/move/{direction}", method = PUT, produces = "application/json")
    public ResponseEntity moveUnit(@PathVariable String mapid, @PathVariable String direction) {
        WorldMap worldMap = worldMapService.getWorldMap(mapid);
        WorldMap updatedWorldMap = movementService.moveUnit(worldMap, direction);
        return new ResponseEntity<>(worldMapService.updateWorldMap(updatedWorldMap).toJson(), HttpStatus.OK);
    }
}

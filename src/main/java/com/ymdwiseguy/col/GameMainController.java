package com.ymdwiseguy.col;

import com.ymdwiseguy.col.menu.implementation.GameMainMenu;
import com.ymdwiseguy.col.menu.structure.PopupType;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class GameMainController {

    private static final Logger LOGGER = getLogger(GameMainController.class);

    // GET - current game state
    @RequestMapping(value = "/game", method = GET)
    public ResponseEntity getInitialMenu(@RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return new ResponseEntity<>(GameMainMenu.create().toJson(), HttpStatus.OK);
    }

    // GET - current game state
    @RequestMapping(value = "/game/{gameId}", method = GET)
    public ResponseEntity getGameData(@PathVariable String gameId, @RequestParam(value = "showPopup", required = false) PopupType showPopup) {
        return new ResponseEntity<>("{}", HttpStatus.OK);

    }
}

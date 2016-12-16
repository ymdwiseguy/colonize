package com.ymdwiseguy.col.mapeditor;

import com.ymdwiseguy.col.Game;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR;
import static org.slf4j.LoggerFactory.getLogger;


@RestController
public class MapEditorController {

    private static final Logger LOGGER = getLogger(MapEditorController.class);

    private final MapEditorView mapEditorView;

    @Inject
    public MapEditorController(MapEditorView mapEditorView) {
        this.mapEditorView = mapEditorView;
    }

    // GET - HTML
    @RequestMapping(value = "/mapeditor")
    public ResponseEntity startMapEditor() {
        Game mapeditor = new Game();
        mapeditor.setGameScreen(MAPEDITOR);

        return new ResponseEntity<>(mapEditorView.render(mapeditor.toJson()), HttpStatus.CREATED);
    }


}

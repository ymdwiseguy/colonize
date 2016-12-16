package com.ymdwiseguy.col.mapeditor;

import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.ymdwiseguy.col.worldmap.HandlebarsTemplate;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MapEditorView {

    private static final Logger LOGGER = getLogger(MapEditorView.class);

    private final Handlebars handlebars;

    @Inject
    public MapEditorView(Handlebars handlebars) {
        this.handlebars = handlebars;
    }

    public String render(String game) {
        HandlebarsTemplate template;
        try {
            handlebars.with(EscapingStrategy.NOOP);
            handlebars.getEscapingStrategy();
            template = handlebars.compile("templates/game").as(HandlebarsTemplate.class);
            template.setGame(game);
            return template.apply(null);
        } catch (IOException e) {
            LOGGER.error("could not load template file", e);
            return "Error";
        }
    }
}

package com.ymdwiseguy.col.views;

import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.ymdwiseguy.col.worldmap.HandlebarsTemplate;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class WorldMapView {

    private static final Logger LOGGER = getLogger(WorldMapView.class);

    private final Handlebars handlebars;

    public WorldMapView(Handlebars handlebars) {
        this.handlebars = handlebars;
    }

    public String render(String game) {
        HandlebarsTemplate template;

        try {
            template = getIndexTemplate();
            template.setGame(game);
            return template.apply(null);
        } catch (IOException e) {
            LOGGER.error("could not load template file", e);
            return "Error";
        }
    }

    private HandlebarsTemplate getIndexTemplate() throws IOException {
        HandlebarsTemplate template;
        handlebars.with(EscapingStrategy.NOOP);
        handlebars.getEscapingStrategy();
        template = handlebars.compile("templates/game").as(HandlebarsTemplate.class);
        return template;
    }
}

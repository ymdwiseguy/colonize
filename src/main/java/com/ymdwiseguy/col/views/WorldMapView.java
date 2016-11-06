package com.ymdwiseguy.col.views;

import com.github.jknack.handlebars.EscapingStrategy;
import com.github.jknack.handlebars.Handlebars;
import com.ymdwiseguy.col.WorldMapTemplate;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class WorldMapView {

    private static final Logger LOGGER = getLogger(WorldMapView.class);

    private final Handlebars handlebars;

    @Inject
    private MapConfigurationReader mapConfigurationReader;

    public WorldMapView(Handlebars handlebars) {
        this.handlebars = handlebars;
    }

    public String render(String name) {
        WorldMapTemplate template;
        mapConfigurationReader.setFilename(name);
        String worldMapConfig = mapConfigurationReader.read().orElse("[]");
        try {
            template = getIndexTemplate();
            template.setWorldMap(worldMapConfig);
            return template.apply(null);
        } catch (IOException e) {
            LOGGER.error("could not load template file", e);
            return "Error";
        }
    }

    protected WorldMapTemplate getIndexTemplate() throws IOException {
        WorldMapTemplate template;
        handlebars.with(EscapingStrategy.NOOP);
        handlebars.getEscapingStrategy();
        template = handlebars.compile("templates/maps").as(WorldMapTemplate.class);
        return template;
    }
}

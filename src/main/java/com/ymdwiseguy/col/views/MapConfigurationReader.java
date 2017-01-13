package com.ymdwiseguy.col.views;

import com.ymdwiseguy.col.filehandling.MapFileHandler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MapConfigurationReader {

    private MapFileHandler mapFileHandler;
    private static final Logger LOGGER = getLogger(MapConfigurationReader.class);

    private String path;
    private String filename;
    private String suffix;

    @Autowired
    public MapConfigurationReader(
        MapFileHandler mapFileHandler, @Value("${maps.configPath}") String path,
        @Value("${maps.suffix}") String suffix) {
        this.mapFileHandler = mapFileHandler;
        this.path = path;
        this.suffix = suffix;
    }

    public MapConfigurationReader setFilename(final String filename) {
        Pattern pattern = Pattern.compile("[^-_A-Za-z0-9]");
        Matcher matcher = pattern.matcher(filename);
        if(!matcher.find()) {
            this.filename = filename;
        }
        return this;
    }

    public Optional<String> read() {
        Objects.nonNull(this.path);
        Objects.nonNull(this.filename);
        Objects.nonNull(this.suffix);
        return mapFileHandler.readDataFromFile(filename);
    }

}

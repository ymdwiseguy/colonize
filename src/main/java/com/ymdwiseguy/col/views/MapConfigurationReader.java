package com.ymdwiseguy.col.views;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MapConfigurationReader {

    private static final Logger LOGGER = getLogger(MapConfigurationReader.class);

    private String path;

    private String filename;

    private String suffix;

    @Autowired
    public MapConfigurationReader(
            @Value("${maps.configPath}") String path,
            @Value("${maps.suffix}") String suffix) {
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
        String configFile = path + filename + suffix;
        return getMapDataFromConfigFile(configFile);
    }

    private Optional<String> getMapDataFromConfigFile(final String configFile) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFile);
        if(inputStream!=null) {
            StringBuilder dashboardData = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String line = null;
                while((line = reader.readLine()) != null) {
                    dashboardData.append(line);
                }
                return Optional.of(dashboardData.toString());
            } catch (IOException e) {
                LOGGER.error("could not read config data from " +  configFile, e);
            }
        } else {
            LOGGER.error("could not read config data from {}", configFile);
        }
        return Optional.empty();
    }
}

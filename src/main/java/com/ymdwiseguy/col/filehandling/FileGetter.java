package com.ymdwiseguy.col.filehandling;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class FileGetter {

    private static final Logger LOGGER = getLogger(FileGetter.class);

    public Optional<String> readDataFromFile(final String fileName, String path) {

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path + fileName + ".json");
        if (inputStream != null) {
            StringBuilder worldmapData = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    worldmapData.append(line);
                }
                return Optional.of(worldmapData.toString());
            } catch (IOException e) {
                LOGGER.error("could not read config data from " + fileName, e);
            }
        } else {
            LOGGER.error("could not read config data from {}", fileName);
        }
        return Optional.empty();
    }
}

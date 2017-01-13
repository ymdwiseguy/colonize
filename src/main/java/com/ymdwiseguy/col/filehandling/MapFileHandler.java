package com.ymdwiseguy.col.filehandling;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class MapFileHandler {

    private static final Logger LOGGER = getLogger(MapFileHandler.class);
    private String mapPath;

    @Inject
    public MapFileHandler(@Value("${maps.configPath}") String mapPath) {
        this.mapPath = mapPath;
    }

    public Optional<String> readDataFromFile(final String fileName) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(mapPath + fileName + ".json");
        if (inputStream != null) {
            StringBuilder worldmapData = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                String line;
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

    public boolean writeDataToFile(final String fileName, final String content) {
        URL mapsPath = getClass().getClassLoader().getResource(mapPath);
        if (mapsPath != null) {
            String filePath = mapsPath.getPath() + fileName + ".json";
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(filePath);
                fileOutputStream.write(content.getBytes());
                fileOutputStream.close();
                return true;
            } catch (IOException e) {
                LOGGER.info("unable to write file, REASON: {}", e);
                return false;
            }
        }
        return false;
    }

    public boolean deleteFile(final String fileName) {
        URL mapsPath = getClass().getClassLoader().getResource(mapPath);
        if (mapsPath != null) {
            String filePath = mapsPath.getPath() + fileName + ".json";
            File file = new File(filePath);
            return file.delete();
        }
        return false;
    }

    public boolean fileExists(String fileName) {
        URL mapsPath = getClass().getClassLoader().getResource(mapPath);
        if (mapsPath != null) {
            String filePath = mapsPath.getPath() + fileName + ".json";
            File file = new File(filePath);
            return file.exists();
        }
        return false;
    }
}

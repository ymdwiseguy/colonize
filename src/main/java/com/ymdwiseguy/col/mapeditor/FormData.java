package com.ymdwiseguy.col.mapeditor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class FormData {

    private static final Logger LOGGER = getLogger(FormData.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private String name;
    private String title;
    private int width;
    private int height;

    public FormData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String toJson() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public FormData fromJson(String popupMenu) throws IOException {
        return mapper.reader().forType(FormData.class).readValue(popupMenu);
    }
}

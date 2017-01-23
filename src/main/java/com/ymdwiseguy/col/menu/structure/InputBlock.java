package com.ymdwiseguy.col.menu.structure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class InputBlock {

    private static final Logger LOGGER = getLogger(InputBlock.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private String name;
    private String title;

    public InputBlock() {
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


    public String toJson() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public InputBlock fromJson(String inputBlock) throws IOException {
        return mapper.reader().forType(InputBlock.class).readValue(inputBlock);
    }
}

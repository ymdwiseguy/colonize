package com.ymdwiseguy.col.menu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.springframework.http.HttpMethod;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.HttpMethod.GET;

public class MenuEntry {

    private static final Logger LOGGER = getLogger(MenuEntry.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private String entryName;
    private String endpointUrl;
    private HttpMethod method = GET;

    public MenuEntry() {
    }

    public MenuEntry(String entryName, String endpointUrl) {
        this.entryName = entryName;
        this.endpointUrl = endpointUrl;
    }



    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public String toJson() {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public MenuEntry fromJson(String menuEntry) throws IOException {
        return mapper.reader().forType(MenuEntry.class).readValue(menuEntry);
    }
}

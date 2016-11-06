package com.ymdwiseguy.col.crosscutting.index;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @param <T> T lets the RestObject be the type of its extended class and of its own type.
 *           So we can return the 'this' reference as a <T extends RestObject>.
 *           And: Any class, which extends RestObject can only be a RestObject of its own type. e.g:
 *           class Foo extends RestObject<Foo> is possible,
 *           class Foo extends RestObject<Bar> is not possible.
 *           This is called a recursive type bound.
 */
@SuppressWarnings("unchecked")
public class RestObject<T extends RestObject<T>> {

    private static final Logger LOGGER = getLogger(RestObject.class);

    protected final Map<String, Link> links;
    protected final Map<String, String> messages;

    public RestObject() {
        links = new HashMap<>();
        messages = new HashMap<>();
    }

    public T addLink(String rel, Link link) {
        links.put(rel, link);
        return (T) this;
    }

    public T addMessage(String key, String message) {
        messages.put(key, message);
        return (T) this;
    }

    @JsonProperty("_links")
    public Map<String, Link> getLinks() {
        return Collections.unmodifiableMap(links);
    }

    @JsonProperty("_messages")
    public Map<String, String> getMessages() {
        return Collections.unmodifiableMap(messages);
    }

}

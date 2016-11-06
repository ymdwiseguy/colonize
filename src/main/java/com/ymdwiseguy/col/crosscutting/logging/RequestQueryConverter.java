package com.ymdwiseguy.col.crosscutting.logging;

import ch.qos.logback.access.pattern.AccessConverter;
import ch.qos.logback.access.spi.IAccessEvent;

public class RequestQueryConverter extends AccessConverter {

    @Override
    public String convert(IAccessEvent event) {
        return event.getRequest().getQueryString();
    }

}

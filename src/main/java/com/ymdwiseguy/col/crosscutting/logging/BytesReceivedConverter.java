package com.ymdwiseguy.col.crosscutting.logging;

import ch.qos.logback.access.pattern.AccessConverter;
import ch.qos.logback.access.spi.IAccessEvent;

public class BytesReceivedConverter extends AccessConverter {

    @Override
    public String convert(IAccessEvent event) {
        return Long.toString(event.getRequest().getContentLengthLong());
    }

}

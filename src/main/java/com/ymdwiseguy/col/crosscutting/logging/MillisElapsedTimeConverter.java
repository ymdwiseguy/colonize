package com.ymdwiseguy.col.crosscutting.logging;

import ch.qos.logback.access.pattern.ElapsedTimeConverter;
import ch.qos.logback.access.spi.IAccessEvent;


public class MillisElapsedTimeConverter extends ElapsedTimeConverter {

    @Override
    public String convert(IAccessEvent accessEvent) {
        return Long.toString(accessEvent.getElapsedTime() * 1000);
    }

}

package com.ymdwiseguy.col.crosscutting.logging;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class LogMarker {
    private static final String BUSINESS_LOG_ID = "BUSINESS";
    private static final String AUDIT_LOG_ID = "AUDIT";

    public static final Marker BUSINESS = MarkerFactory.getMarker(BUSINESS_LOG_ID);
    public static final Marker AUDIT = MarkerFactory.getMarker(AUDIT_LOG_ID);
}

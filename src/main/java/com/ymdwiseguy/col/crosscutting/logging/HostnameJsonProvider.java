package com.ymdwiseguy.col.crosscutting.logging;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.composite.AbstractFieldJsonProvider;
import net.logstash.logback.composite.JsonWritingUtils;

import java.io.IOException;

public class HostnameJsonProvider extends AbstractFieldJsonProvider<DeferredProcessingAware> {

    public HostnameJsonProvider() {
        setFieldName("host");
    }

    @Override
    public void writeTo(JsonGenerator generator, DeferredProcessingAware deferredProcessingAware) throws IOException {
        JsonWritingUtils.writeStringField(generator, getFieldName(), context.getProperty(CoreConstants.HOSTNAME_KEY));
    }

}

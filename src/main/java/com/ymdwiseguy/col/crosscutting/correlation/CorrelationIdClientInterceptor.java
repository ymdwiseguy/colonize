package com.ymdwiseguy.col.crosscutting.correlation;

import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Optional;

public class CorrelationIdClientInterceptor implements ClientHttpRequestInterceptor {
    public static final String MDC_CORRELATION_ID = "correlationId";
    public static final String HTTP_CORRELATION_ID = "correlation-id";

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        Optional
            .ofNullable(MDC.get(MDC_CORRELATION_ID))
            .ifPresent(id -> request.getHeaders().add(HTTP_CORRELATION_ID, id));

        return execution.execute(request, body);
    }
}

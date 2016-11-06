package com.ymdwiseguy.col.crosscutting.correlation;

import org.slf4j.Logger;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

public class CorrelationIdFilter implements Filter {
    public static final String MDC_CORRELATION_ID = "correlationId";
    public static final String HTTP_CORRELATION_ID = "correlation-id";

    private static final Logger LOG = getLogger(CorrelationIdFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // NOOP
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if(servletRequest instanceof HttpServletRequest) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

            final String correlationId = Optional
                .ofNullable(httpServletRequest.getHeader(HTTP_CORRELATION_ID))
                .orElseGet(() -> {
                    final String newCorrelationId = UUID.randomUUID().toString();
                    LOG.debug("No correlationId found in the request. Generating a new one '{}'.", newCorrelationId);
                    return newCorrelationId;
                });

            LOG.debug("Using correlationID '{}' for this request.", correlationId);

            MDC.put(MDC_CORRELATION_ID, correlationId);
            httpServletRequest.setAttribute(HTTP_CORRELATION_ID, correlationId);
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(MDC_CORRELATION_ID);
        }
    }

    @Override
    public void destroy() {
        // NOOP
    }
}

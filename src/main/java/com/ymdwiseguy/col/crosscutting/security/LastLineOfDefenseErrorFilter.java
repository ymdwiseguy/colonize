package com.ymdwiseguy.col.crosscutting.security;

import com.ymdwiseguy.col.crosscutting.logging.LogUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;

import javax.servlet.Filter;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LastLineOfDefenseErrorFilter implements Filter {

    private static final Log LOGGER = LogFactory.getLog(LastLineOfDefenseErrorFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (Exception e) {
            LogUtil.error(e, () -> LOGGER.error("An unexpected error occurred and went through all layers to this filter.", e));

            if (res instanceof HttpServletResponse) {
                HttpServletResponse httpResponse = (HttpServletResponse) res;
                httpResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                httpResponse.getWriter().write(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
                httpResponse.getWriter().flush();
            }
        }
    }

    @Override
    public void destroy() {
    }
}

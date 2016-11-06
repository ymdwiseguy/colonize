package com.ymdwiseguy.col.crosscutting.index;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Component
public class LinkBuilder {

    public Link get(String path, boolean templated) {
        HttpServletRequest servletRequest = Objects.requireNonNull(
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(),
            "The LinkBuilder needs a running servlet context to automatically determine the ServletRequest"
        );

        return get(servletRequest, path, templated);
    }

    public Link get(HttpServletRequest servletRequest, String path, boolean templated) {
        final ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromServletMapping(servletRequest);

        final String pathPrefix = "api";
        final String slashPath = !path.startsWith("/") ? "/" + path : path;

        builder.path("/" + pathPrefix + slashPath);

        final String href = fixProtocol(builder.build(false).toUriString(), servletRequest);
        return new Link(href, templated);
    }

    private String fixProtocol(final String url, final HttpServletRequest servletRequest) {
        return isExternalCall(servletRequest) ? url.replaceFirst("http", "https") : url;
    }

    private boolean isExternalCall(final HttpServletRequest servletRequest) {
        return Optional.ofNullable(servletRequest.getHeader("Host"))
            .isPresent();
    }

}

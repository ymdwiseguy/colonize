package com.ymdwiseguy.col.crosscutting.index;

public class Link {
    public final String href;
    public final boolean templated;

    public Link(final String href, final boolean templated) {
        this.href = href;
        this.templated = templated;
    }
}

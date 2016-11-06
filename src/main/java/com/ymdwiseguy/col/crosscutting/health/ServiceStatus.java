package com.ymdwiseguy.col.crosscutting.health;

import java.util.Optional;

public class ServiceStatus {
    public final String name;
    public final Status status;
    public final String info;
    public final String error;

    public ServiceStatus(final String name, final Status status, final String info, final Optional<String> error) {
        this.name = name;
        this.status = status;
        this.info = info;
        this.error = error.orElse(null);
    }
}

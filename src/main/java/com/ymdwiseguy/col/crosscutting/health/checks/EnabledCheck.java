package com.ymdwiseguy.col.crosscutting.health.checks;

import com.ymdwiseguy.col.crosscutting.health.ServiceStatus;
import com.ymdwiseguy.col.crosscutting.health.Status;
import org.slf4j.Logger;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

import static org.slf4j.LoggerFactory.getLogger;

public class EnabledCheck extends HealthCheck {

    private static final Logger LOG = getLogger(EnabledCheck.class);

    private volatile boolean enabled = true;

    public EnabledCheck(int timeoutSeconds, ScheduledExecutorService executor) {
        super("enabled",  Duration.of(timeoutSeconds, ChronoUnit.SECONDS), executor);
    }

    @Override
    protected Supplier<ServiceStatus> getCheckSupplier() {
        return () -> {
            LOG.debug("Executing enabled check with actual status {}", enabled);
            return enabled ?
                new ServiceStatus("enabled", Status.UP, connectionInfo(), Optional.empty()) :
                new ServiceStatus("enabled", Status.DOWN, connectionInfo(), Optional.of("The service was disabled manually."));
        };
    }

    @Override
    protected String connectionInfo() {
        return "This service is " + (enabled ? "enabled" : "disabled");
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }
}

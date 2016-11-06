package com.ymdwiseguy.col.crosscutting.health.checks;

import com.codahale.metrics.Timer;
import com.ymdwiseguy.col.crosscutting.health.ServiceStatus;
import com.ymdwiseguy.col.crosscutting.health.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public abstract class HealthCheck {

    private static final Logger LOG = LoggerFactory.getLogger(HealthCheck.class);

    private final ScheduledExecutorService executor;

    private final String name;

    private final Duration timeoutDuration;

    public HealthCheck(final String name, final Duration timeoutDuration, final ScheduledExecutorService executor) {
        this.name = name;
        this.timeoutDuration = timeoutDuration;
        this.executor = executor;
    }

    protected abstract Supplier<ServiceStatus> getCheckSupplier();

    protected abstract String connectionInfo();

    /**
     * Returns a future of the check. This future will be computed aasynchronously sometime in the future. So the name.
     * The completed Future will contains either the check result or the timeout default.
     *
     * @return
     */
    public CompletableFuture<ServiceStatus> check() {
        ServiceStatus timeoutDefault = new ServiceStatus(
            name, Status.DOWN, connectionInfo(), Optional.of(name + " health check timed out after " + timeoutDuration.toMillis() + "ms.")
        );

        // Little concurrent excursion:
        // First we create our check future. Next, we define a timeout function. This function completes the original
        // future after 'timeoutDuration' seconds with the default ServiceStatus but only if the original future is not
        // completed in time. The scheduler will not block a thread. The only blocking will occur inside the check future.
        CompletableFuture<ServiceStatus> healthcheckFuture = CompletableFuture.supplyAsync(() -> {
            final Timer.Context timeContext  = new Timer().time();
            try {
                return getCheckSupplier().get();
            } finally {
                long duration = timeContext.stop();
                LOG.info("Healthcheck '{}' lasts {}ms - {}µs", name, duration / 1000 / 1000, duration / 1000);
            }
        }, executor);


        Runnable concurrentTimeout = () -> {
            if(!healthcheckFuture.isDone()) {
                LOG.error("Got a timeout on service '" + name + "' after " + timeoutDuration.toMillis() + "ms.");
            }

            healthcheckFuture.complete(timeoutDefault);
        };
        executor.schedule(concurrentTimeout, timeoutDuration.toMillis(), TimeUnit.MILLISECONDS);

        return healthcheckFuture;
    }
}

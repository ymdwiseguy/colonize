package com.ymdwiseguy.col.crosscutting.health;

import com.ymdwiseguy.col.crosscutting.health.checks.EnabledCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class HealthConfiguration {
    @Autowired
    ScheduledExecutorService executorService;

    @Value("${health.timeout_seconds}")
    int timeoutSeconds;

    @Bean
    public EnabledCheck enabledCheck() {
        return new EnabledCheck(timeoutSeconds, executorService);
    }

    @Bean
    public ScheduledExecutorService executorService() {
        // If we need a larger amount of threads, we can use an additional ThreadPool with 'newCachedThreadPool'
        return Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 4);
    }

}

package com.ymdwiseguy.col.crosscutting.health;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ymdwiseguy.col.crosscutting.health.checks.HealthCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static com.ymdwiseguy.col.crosscutting.health.Status.DOWN;
import static com.ymdwiseguy.col.crosscutting.health.Status.UP;

@Component
public class HealthService {

    private final List<HealthCheck> healthChecks;

    @Autowired
    public HealthService(HealthCheck... healthChecks) {
        this.healthChecks = ImmutableList.copyOf(healthChecks);
    }

    /**
     * Returns a future object of all performed checks.
     * The method first gets n Futures of the check. It combines this futures to one. The new resulting future will
     * be completed if all single futures are completed.
     *
     * @return
     */
    public CompletableFuture<List<ServiceStatus>> all() {
        List<CompletableFuture<ServiceStatus>> futureList = healthChecks.stream().map(HealthCheck::check).collect(Collectors.toList());
        return transformList(CompletableFuture.completedFuture(Lists.newArrayList()), futureList);
    }

    /**
     * Transforms a "list of futures of T" into a "future of list of T"
     *
     * @param accumulator
     * @param elems
     * @param <T>
     * @return
     */
    private <T> CompletableFuture<List<T>> transformList(CompletableFuture<List<T>> accumulator, List<CompletableFuture<T>> elems) {
        BiFunction<List<T>, T, List<T>> combinator = (list, element) -> { list.add(element); return list; };
        if(elems.isEmpty()) {
            return accumulator;
        } else {
            final CompletableFuture<List<T>> combination = accumulator.thenCombine(elems.get(0), combinator);
            return transformList(combination, elems.subList(1, elems.size()));
        }
    }

    public Status overall(List<ServiceStatus> statuses) {
        return statuses.stream().allMatch(s -> s.status.equals(UP)) ? UP : DOWN;
    }
 }

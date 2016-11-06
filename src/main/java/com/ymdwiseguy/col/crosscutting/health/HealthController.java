package com.ymdwiseguy.col.crosscutting.health;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ymdwiseguy.col.crosscutting.health.checks.EnabledCheck;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class HealthController {

    private static final Logger LOG = getLogger(HealthController.class);

    @Autowired
    HealthService healthService;

    @Autowired
    EnabledCheck enabledCheck;

    @RequestMapping(value = "/alive", method = RequestMethod.GET)
    public ResponseEntity<String> alive() {
        return new ResponseEntity<>("alive", HttpStatus.OK);
    }

    /**
     * Returns an spring specific asynchronous object. The DeferredResult ist some kind of future and will
     * be completed sometime in the future.
     *
     * We get a future object from the HealthService. This future we give a callback function, when the future
     * completes. In this callback we compute the JsonNode and set it as result of the DeferredResult, which
     * reference we have in this scope as closure.
     *
     * So the futures completes the deferred object, when itself completes and the spring MVC logic can then handle the
     * result.
     *
     * @return
     */
    @RequestMapping(value = "/admin/health", method = GET)
    public DeferredResult<ResponseEntity> health() {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();

        healthService.all().thenAccept((checkList) -> {
            final JsonNodeFactory factory = JsonNodeFactory.instance;
            final Status overall = healthService.overall(checkList);

            final ObjectNode node = factory.objectNode().put("status", overall.toString());
            checkList.forEach(service -> node.set(service.name, factory.pojoNode(service)));
            deferredResult.setResult(new ResponseEntity<>(node, HttpStatus.OK));
        });

        return deferredResult;
    }

    @RequestMapping(value = "/admin/healthcheck", method = GET)
    public  DeferredResult<ResponseEntity> healthCheck() {
        DeferredResult<ResponseEntity> deferredResult = new DeferredResult<>();

        healthService.all().thenAccept((checkList) -> {
            final Status overall = healthService.overall(checkList);
            if (overall.equals(Status.DOWN)) {
                final String errors = checkList.stream().map(status -> status.error).filter(err -> err != null).collect(joining(", "));
                LOG.error("SERVICE_UNAVAILABLE : {}", errors);
                deferredResult.setResult(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), HttpStatus.SERVICE_UNAVAILABLE));
            } else {
                deferredResult.setResult(new ResponseEntity<>(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK));
            }
        });

        return deferredResult;
    }

    @RequestMapping(value = "/admin/healthcheck", method = POST)
    public ResponseEntity changeStatus(@RequestBody final Map<String, String> body) {
        return Optional.ofNullable(body.get("status"))
            .filter(status -> status.equalsIgnoreCase("enabled") || status.equalsIgnoreCase("disabled"))
            .map(status -> {
                enabledCheck.setEnabled(status.equalsIgnoreCase("enabled"));
                LOG.info("Manually {} the health check", status);
                return new ResponseEntity<>(HttpStatus.OK);
            })
            .orElseGet(() -> {
                final String warn = "Status change failed. Valid payloads are : '{\"status\": \"(enabled|disabled)\"}'";
                LOG.warn(warn);
                return new ResponseEntity<>(warn, HttpStatus.BAD_REQUEST);
            });
    }

}

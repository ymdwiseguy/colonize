package com.ymdwiseguy.col.crosscutting.index;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class IndexController {

    private final LinkBuilder linkBuilder;

    @Inject
    public IndexController(final LinkBuilder linkBuilder) {
        this.linkBuilder = linkBuilder;
    }

    @RequestMapping(value = "/", method = GET, produces = "application/json")
    public ResponseEntity<RestObject> root() {
        return new ResponseEntity<>(
            new RestObject().addLink("index", linkBuilder.get("/", false)),
            HttpStatus.OK
        );
    }

    @RequestMapping(value = "/api", method = GET, produces = "application/json")
    public ResponseEntity<RestObject> index() {
        final RestObject links = new RestObject()
            .addLink("location", linkBuilder.get("/location?query={query}", true));
        return new ResponseEntity<>(links, HttpStatus.OK);
    }

}

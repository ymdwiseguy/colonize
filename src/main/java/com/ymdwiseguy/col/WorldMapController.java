package com.ymdwiseguy.col;

import com.ymdwiseguy.col.views.WorldMapView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WorldMapController {

    @Autowired
    WorldMapView worldMapView;


    @RequestMapping(value = "/maps/{name}")
    public String dashboard(@PathVariable String name) {
        return worldMapView.render(name);
    }
}

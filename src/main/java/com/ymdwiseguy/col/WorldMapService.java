package com.ymdwiseguy.col;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class WorldMapService {

    private static final Logger LOGGER = getLogger(WorldMapService.class);

    private final WorldMapsRepo WorldMapsRepo;

    @Autowired
    public WorldMapService(final WorldMapsRepo WorldMapsRepo) {
        this.WorldMapsRepo = WorldMapsRepo;
    }

    public Optional<WorldMap> getMonitor(String uuid) {
        return WorldMapsRepo.getWorldmap(uuid);
    }
}

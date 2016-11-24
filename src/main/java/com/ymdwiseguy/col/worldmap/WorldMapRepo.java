package com.ymdwiseguy.col.worldmap;

import org.slf4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

public class WorldMapRepo {

    private static final Logger LOGGER = getLogger(WorldMapRepo.class);
    private final JdbcTemplate jdbcTemplate;

    public WorldMapRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String createWorldmap(WorldMap worldMap) {

        final String sql = "INSERT INTO worldmap (worldmap_id,title,width,height) VALUES (?,?,?,?)";

        try {
            jdbcTemplate.update(sql, post -> {
                populateWorldmapStatement(worldMap, post);
            });
        } catch (ConstraintViolationException cve) {
            throw cve;
        }
        LOGGER.info("Created worldMap '{}'", worldMap.getWorldMapId());
        return worldMap.getWorldMapId();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Optional<WorldMap> getWorldmap(String worldmapID) {
        final String sql = "SELECT * FROM worldmap WHERE worldmap_id=?";

        RowMapper<WorldMap> worldmapRowMapper = (resultSet, rowNum) -> new WorldMap(
            resultSet.getString("worldmap_id"),
            resultSet.getString("title"),
            resultSet.getInt("width"),
            resultSet.getInt("height")
        );
        try {
            LOGGER.info("Fetched worldmap with worldmapID '{}'", worldmapID);
            WorldMap fetchedWorldmap = jdbcTemplate.queryForObject(sql, worldmapRowMapper, worldmapID);
            return Optional.ofNullable(fetchedWorldmap);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("WorldMap with id {} was not found in the database.", worldmapID);
            return Optional.empty();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<WorldMap> updateWorldmap(WorldMap worldMap) {
        final String sql = "UPDATE worldmap SET worldmap_id = ?, title = ?, width = ?, height = ? WHERE worldmap_id = ?";
        String worldMapId = worldMap.getWorldMapId();
        try {
            jdbcTemplate.update(sql, post -> {
                populateWorldmapStatement(worldMap, post);
                post.setString(5, worldMapId);
            });
            LOGGER.info("Updated worldMap '{}'", worldMap.getWorldMapId());
        } catch (ConstraintViolationException cve) {
            LOGGER.info("WorldMap with id {} was not found in the database.", worldMap.getWorldMapId());
            throw cve;
        }
        return getWorldmap(worldMapId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteWorldmap(String uuid) {
        final String sql = "DELETE FROM worldmap WHERE worldmap_id = ?";
        jdbcTemplate.update(sql, uuid);
        LOGGER.info("Deleted client '{}' in the database", uuid);
    }

    private void populateWorldmapStatement(WorldMap worldMap, PreparedStatement post) throws SQLException {
        if (worldMap.getWorldMapId() == null) {
            worldMap.setWorldMapId(UUID.randomUUID().toString());
        }
        post.setString(1, worldMap.getWorldMapId());
        post.setString(2, worldMap.getTitle());
        post.setInt(3, worldMap.getWidth());
        post.setInt(4, worldMap.getHeight());
    }
}

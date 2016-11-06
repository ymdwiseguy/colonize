package com.ymdwiseguy.col;

import org.slf4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class WorldMapsRepo {

    private static final Logger LOGGER = getLogger(WorldMapsRepo.class);
    private final JdbcTemplate jdbcTemplate;

    public WorldMapsRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public String createWorldmap(WorldMap worldMap) {
        final String sql = "INSERT INTO worldmap (worldmap_id) VALUES (?)";

        try {
            jdbcTemplate.update(sql, post -> {
                populateWorldmapStatement(worldMap, post);
            });
        } catch (ConstraintViolationException cve) {
            throw cve;
        }
        LOGGER.info("Created worldMap '{}'", worldMap.getWorldMapID());
        return worldMap.getWorldMapID();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Optional<WorldMap> getWorldmap(String worldmapID) {
        final String sql = "SELECT * FROM worldmap WHERE worldmap_id=?";

        RowMapper<WorldMap> worldmapRowMapper = (resultSet, rowNum) -> new WorldMap(
                resultSet.getString("worldmap_id")
        );
        try {
            LOGGER.info("Fetched worldmap with worldmapID '{}'", worldmapID);
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, worldmapRowMapper, worldmapID));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("WorldMap with id {} was not found in the database.", worldmapID);
            return Optional.empty();
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void updateWorldmap(WorldMap worldMap) {
        final String sql = "UPDATE worldmap SET worldmap_id = ? WHERE worldmap_id = ?";

        try {
            jdbcTemplate.update(sql, post -> {
                populateWorldmapStatement(worldMap, post);
                post.setString(5, worldMap.getWorldMapID());
            });
            LOGGER.info("Updated worldMap '{}'", worldMap.getWorldMapID());
        } catch (ConstraintViolationException cve) {
            LOGGER.info("WorldMap with id {} was not found in the database.", worldMap.getWorldMapID());
            throw cve;
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void deleteWorldmap(String uuid) {
        final String sql = "DELETE FROM worldmap WHERE worldmap_id = ?";
        jdbcTemplate.update(sql, uuid);
        LOGGER.info("Deleted client '{}' in the database", uuid);
    }

    private void populateWorldmapStatement(WorldMap worldMap, PreparedStatement post) throws SQLException {
        post.setString(1, worldMap.getWorldMapID());
    }


    private PreparedStatementSetter populateRelationStatement(String worldmapUUID, String relation) {
        return (PreparedStatement ps) -> {
            ps.setString(1, worldmapUUID);
            ps.setString(2, relation);
        };
    }

    private final RowMapper<WorldMap> worldmapRowMapper = (ResultSet rs, int rowNum) -> {
        WorldMap worldMap = new WorldMap();
        worldMap.setWorldMapID(rs.getString("worldmap_id"));
        return worldMap;
    };
}

package com.ymdwiseguy.col.worldmap.unit;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

public class UnitRepo {

    private static final Logger LOGGER = getLogger(UnitRepo.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UnitRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public String createUnit(Unit unit) {
        if (unit.getUnitId() == null) {
            unit.setUnitId(UUID.randomUUID().toString());
        }
        final String sql = "INSERT INTO unit (unit_id,world_map_id,unit_type,active,x_position,y_position) VALUES (?,?,?,?,?,?)";

        try {
            jdbcTemplate.update(sql, post -> {
                populateUnitStatement(unit, post);
            });
        } catch (ConstraintViolationException cve) {
            LOGGER.error("Error updating unit {}", cve);
        }
        LOGGER.info("Created worldMap '{}'", unit.getUnitId());
        return unit.getUnitId();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void createUnits(List<Unit> units) {

        final String sql = "INSERT INTO unit (unit_id,world_map_id,unit_type,active,x_position,y_position) VALUES (?,?,?,?,?,?)";

        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Unit unit = units.get(i);
                    if (unit.getUnitId() == null) {
                        unit.setUnitId(UUID.randomUUID().toString());
                    }
                    ps.setString(1, unit.getUnitId());
                    ps.setString(2, unit.getWorldMapId());
                    ps.setString(3, unit.getUnitType().toString());
                    ps.setBoolean(4, unit.isActive());
                    ps.setInt(5, unit.getxPosition());
                    ps.setInt(6, unit.getyPosition());
                }

                @Override
                public int getBatchSize() {
                    return units.size();
                }
            });
        } catch (ConstraintViolationException cve) {
            LOGGER.error("Error updating unit {}", cve);
        }
        LOGGER.info("Created units '{}'", units.size());
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Optional<Unit> getUnit(String unitId) {
        final String sql = "SELECT * FROM unit WHERE unit_id=?";

        RowMapper<Unit> unitRowMapper = (resultSet, rowNum) -> new Unit(
            resultSet.getString("unit_id"),
            resultSet.getString("world_map_id"),
            UnitType.valueOf(resultSet.getString("unit_type")),
            resultSet.getBoolean("active"),
            resultSet.getInt("x_position"),
            resultSet.getInt("y_position")
        );
        try {
            LOGGER.info("Fetched unit with unit_id '{}'", unitId);
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, unitRowMapper, unitId));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("Unit with id {} was not found in the database.", unitId);
            return Optional.empty();
        }
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Unit> getUnits(String worldMapId) {
        final String sql = "SELECT * FROM unit WHERE world_map_id=? ORDER BY y_position, x_position ASC";

        RowMapper<Unit> unitRowMapper = (resultSet, rowNum) -> new Unit(
            resultSet.getString("unit_id"),
            resultSet.getString("world_map_id"),
            UnitType.valueOf(resultSet.getString("unit_type")),
            resultSet.getBoolean("active"),
            resultSet.getInt("x_position"),
            resultSet.getInt("y_position")
        );

        try {
            LOGGER.info("Fetched units with world_map_id '{}'", worldMapId);
            return jdbcTemplate.query(sql, unitRowMapper, worldMapId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("Unit with id {} was not found in the database.", worldMapId);
            return new ArrayList<>();
        }
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUnit(Unit unit) {
        final String sql = "UPDATE unit SET unit_id = ?, world_map_id = ?, unit_type = ?, active = ?, x_position = ?, y_position = ? WHERE unit_id = ?";

        try {
            jdbcTemplate.update(sql, post -> {
                populateUnitStatement(unit, post);
                post.setString(6, unit.getUnitId());
            });
            LOGGER.info("Updated unit '{} {}'", unit.getxPosition(), unit.getyPosition());
        } catch (ConstraintViolationException cve) {
            LOGGER.info("Unit with id {} was not found in the database.", unit.getUnitId());
            throw cve;
        }
    }

    private void populateUnitStatement(Unit unit, PreparedStatement post) throws SQLException {
        post.setString(1, unit.getUnitId());
        post.setString(2, unit.getWorldMapId());
        post.setString(3, unit.getUnitType().toString());
        post.setBoolean(4, unit.isActive());
        post.setInt(5, unit.getxPosition());
        post.setInt(6, unit.getyPosition());

    }

    public List<Unit> updateUnits(List<Unit> units) {
        final String sql = "UPDATE unit SET unit_id = ?, world_map_id = ?, unit_type = ?, active = ?, x_position = ?, y_position = ? WHERE unit_id = ?";

        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Unit unit = units.get(i);
                    ps.setString(1, unit.getUnitId());
                    ps.setString(2, unit.getWorldMapId());
                    ps.setString(3, unit.getUnitType().toString());
                    ps.setBoolean(4, unit.isActive());
                    ps.setInt(5, unit.getxPosition());
                    ps.setInt(6, unit.getyPosition());
                    ps.setString(7, unit.getUnitId());
                }

                @Override
                public int getBatchSize() {
                    return units.size();
                }
            });
        } catch (ConstraintViolationException cve) {
            LOGGER.error("Error updating unit {}", cve);
        }
        LOGGER.info("Created units '{}'", units.size());
        return units;
    }
}

package com.ymdwiseguy.col.worldmap.tile;

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

public class TileRepo {

    private static final Logger LOGGER = getLogger(TileRepo.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TileRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public String createTile(Tile tile) {
        if (tile.getTileId() == null) {
            tile.setTileId(UUID.randomUUID().toString());
        }
        final String sql = "INSERT INTO tile (tile_id,world_map_id,x_coordinate,y_coordinate,type) VALUES (?,?,?,?,?)";

        try {
            jdbcTemplate.update(sql, post -> {
                populateTileStatement(tile, post);
            });
        } catch (ConstraintViolationException cve) {
            LOGGER.error("Error updating tile {}", cve);
        }
        LOGGER.info("Created worldMap '{}'", tile.getTileId());
        return tile.getTileId();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void createTiles(List<Tile> tiles) {

        final String sql = "INSERT INTO tile (tile_id,world_map_id,x_coordinate,y_coordinate,type) VALUES (?,?,?,?,?)";

        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Tile tile = tiles.get(i);
                    if (tile.getTileId() == null) {
                        tile.setTileId(UUID.randomUUID().toString());
                    }
                    ps.setString(1, tile.getTileId());
                    ps.setString(2, tile.getWorldMapId());
                    ps.setInt(3, tile.getxCoordinate());
                    ps.setInt(4, tile.getyCoordinate());
                    ps.setString(5, tile.getType().toString());
                }

                @Override
                public int getBatchSize() {
                    return tiles.size();
                }
            });
        } catch (ConstraintViolationException cve) {
            LOGGER.error("Error updating tile {}", cve);
        }
        LOGGER.info("Created tiles '{}'", tiles.size());
    }


    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Optional<Tile> getTile(String tileId) {
        final String sql = "SELECT * FROM tile WHERE tile_id=?";

        RowMapper<Tile> tileRowMapper = (resultSet, rowNum) -> new Tile(
            resultSet.getString("tile_id"),
            resultSet.getString("world_map_id"),
            resultSet.getInt("x_coordinate"),
            resultSet.getInt("y_coordinate"),
            TileType.valueOf(resultSet.getString("type"))
        );
        try {
            LOGGER.info("Fetched tile with tile_id '{}'", tileId);
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, tileRowMapper, tileId));
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("Tile with id {} was not found in the database.", tileId);
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Tile> getTilesLimited(String worldMapId, int limitXLower, int limitXUpper, int limitYLower, int limitYUpper) {
        final String sql = "SELECT * FROM tile " +
            "WHERE world_map_id=? " +
            "AND x_coordinate<=? AND x_coordinate>=? " +
            "AND y_coordinate<=? AND y_coordinate>=? " +
            "ORDER BY y_coordinate, x_coordinate ASC ";

        try {
            LOGGER.info("Fetched tiles with world_map_id '{}'", worldMapId);
            return jdbcTemplate.query(sql, getRowmapper(), worldMapId, limitXUpper, limitXLower, limitYUpper, limitYLower);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("Tile with id {} was not found in the database.", worldMapId);
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Tile> getTiles(String worldMapId) {
        final String sql = "SELECT * FROM tile WHERE world_map_id=? ORDER BY y_coordinate, x_coordinate ASC ";

        try {
            LOGGER.info("Fetched tiles with world_map_id '{}'", worldMapId);
            return jdbcTemplate.query(sql, getRowmapper(), worldMapId);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("Tile with id {} was not found in the database.", worldMapId);
            return new ArrayList<>();
        }
    }

    private RowMapper<Tile> getRowmapper() {
        return (resultSet, rowNum) -> new Tile(
            resultSet.getString("tile_id"),
            resultSet.getString("world_map_id"),
            resultSet.getInt("x_coordinate"),
            resultSet.getInt("y_coordinate"),
            TileType.valueOf(resultSet.getString("type"))
        );
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTile(Tile tile) {
        final String sql = "UPDATE tile SET tile_id = ?, world_map_id = ?, x_coordinate = ?, y_coordinate = ?, type = ? WHERE tile_id = ?";

        try {
            jdbcTemplate.update(sql, post -> {
                populateTileStatement(tile, post);
                post.setString(6, tile.getTileId());
            });
            LOGGER.info("Updated tile '{} {}'", tile.getxCoordinate(), tile.getyCoordinate());
        } catch (ConstraintViolationException cve) {
            LOGGER.info("Tile with id {} was not found in the database.", tile.getTileId());
            throw cve;
        }
    }

    public List<Tile> updateTiles(List<Tile> tiles) {
        final String sql = "UPDATE tile SET tile_id = ?, world_map_id = ?, x_coordinate = ?, y_coordinate = ?, type = ? WHERE tile_id = ?";

        try {
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Tile tile = tiles.get(i);
                    populateTileStatement(tile, ps);
                    ps.setString(6, tile.getTileId());
                }

                @Override
                public int getBatchSize() {
                    return tiles.size();
                }
            });
        } catch (ConstraintViolationException cve) {
            LOGGER.error("Error updating tile {}", cve);
        }
        LOGGER.info("Created tiles '{}'", tiles.size());
        return tiles;
    }

    private void populateTileStatement(Tile tile, PreparedStatement post) throws SQLException {
        post.setString(1, tile.getTileId());
        post.setString(2, tile.getWorldMapId());
        post.setInt(3, tile.getxCoordinate());
        post.setInt(4, tile.getyCoordinate());
        post.setString(5, tile.getType().toString());

    }

}

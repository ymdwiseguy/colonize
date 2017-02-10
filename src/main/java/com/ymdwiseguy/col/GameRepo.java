package com.ymdwiseguy.col;

import com.ymdwiseguy.col.cursor.Cursor;
import org.slf4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class GameRepo {

    private static final Logger LOGGER = getLogger(GameRepo.class);
    private final JdbcTemplate jdbcTemplate;

    public GameRepo(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Game createGame(Game game) {

        final String sql = "INSERT INTO game_state (game_id,game_screen,world_map_id,cursor_x,cursor_y,tile_type) VALUES (?,?,?,?,?,?)";

        try {
            jdbcTemplate.update(sql, post -> {
                populateGameStatement(game, post);
            });
        } catch (ConstraintViolationException cve) {
            LOGGER.error("Unable to save game: {}", cve);
        }
        LOGGER.info("Created game state '{}'", game.getGameId());
        return game;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Optional<Game> getGame(String gameID) {
        final String sql = "SELECT * FROM game_state WHERE game_id=?";

        RowMapper<Game> gameRowMapper = (resultSet, rowNum) -> new Game(
            resultSet.getString("game_id"),
            resultSet.getString("game_screen"),
            resultSet.getString("world_map_id"),
            new Cursor(resultSet.getInt("cursor_x"), resultSet.getInt("cursor_y")),
            resultSet.getString("tile_type")
        );
        try {
            LOGGER.info("Fetched game with gameID '{}'", gameID);
            Game fetchedGame = jdbcTemplate.queryForObject(sql, gameRowMapper, gameID);
            return Optional.ofNullable(fetchedGame);
        } catch (EmptyResultDataAccessException e) {
            LOGGER.info("Game with id {} was not found in the database.", gameID);
            return Optional.empty();
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<Game> updateGame(Game game) {
        final String sql = "UPDATE game_state SET game_id = ?, game_screen = ?, world_map_id = ?, cursor_x = ?, cursor_y = ?, tile_type = ? WHERE game_id = ?";
        String gameId = game.getGameId();
        try {
            jdbcTemplate.update(sql, post -> {
                populateGameStatement(game, post);
                post.setString(7, gameId);
            });
            LOGGER.info("Updated game '{}'", game.getGameId());
            return Optional.of(game);
        } catch (ConstraintViolationException cve) {
            LOGGER.info("Game with id {} was not found in the database.", game.getGameId());
            throw cve;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteGame(String uuid) {
        final String sql = "DELETE FROM game_state WHERE game_id = ?";
        jdbcTemplate.update(sql, uuid);
        LOGGER.info("Deleted game with id '{}' in the database", uuid);
    }

    private void populateGameStatement(Game game, PreparedStatement post) throws SQLException {
        if (game.getGameId() == null) {
            game.setGameId(UUID.randomUUID().toString());
        }
        String worldMapId = null;
        if (game.getWorldMap() != null) {
            worldMapId = game.getWorldMap().getWorldMapId();
        }
        post.setString(1, game.getGameId());
        post.setString(2, (game.getGameScreen() != null ? game.getGameScreen().toString() : null));
        post.setString(3, worldMapId);
        post.setInt(4, (game.getCursor() != null ? game.getCursor().getxPosition() : 0));
        post.setInt(5, (game.getCursor() != null ? game.getCursor().getyPosition() : 0));
        post.setString(6, (game.getSelectedTileType() != null ? game.getSelectedTileType().toString() : null));
    }
}

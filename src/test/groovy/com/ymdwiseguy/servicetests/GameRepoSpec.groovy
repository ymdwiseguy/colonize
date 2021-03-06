package com.ymdwiseguy.servicetests

import com.ymdwiseguy.Colonization
import com.ymdwiseguy.col.Game
import com.ymdwiseguy.col.GameRepo
import com.ymdwiseguy.col.GameScreen
import com.ymdwiseguy.col.cursor.Cursor
import com.ymdwiseguy.col.worldmap.WorldMap
import com.ymdwiseguy.col.worldmap.tile.TileType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR
import static com.ymdwiseguy.col.GameScreen.WORLDMAP
import static com.ymdwiseguy.col.worldmap.tile.TileType.LAND_GRASS

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [Colonization])
@WebAppConfiguration
class GameRepoSpec extends Specification {


    @Autowired
    def JdbcTemplate jdbcTemplate

    Game GAME
    Game GAME_WITH_NULLS
    WorldMap WORLD_MAP
    String GAME_ID
    String ANOTHER_GAME_ID
    GameScreen GAME_SCREEN
    Cursor CURSOR
    TileType SELECTED_TILE_TYPE

    def setup() {
        GAME_ID = UUID.randomUUID().toString()
        ANOTHER_GAME_ID = UUID.randomUUID().toString()
        WORLD_MAP = new WorldMap(UUID.randomUUID().toString())
        GAME_SCREEN = MAPEDITOR
        CURSOR = new Cursor(1, 1)
        SELECTED_TILE_TYPE = LAND_GRASS
        GAME = new Game(GAME_ID, GAME_SCREEN, WORLD_MAP, CURSOR)
        GAME.setSelectedTileType(SELECTED_TILE_TYPE)
        GAME_WITH_NULLS = new Game()
        GAME_WITH_NULLS.setGameId(ANOTHER_GAME_ID)
    }

    def "Game CRUD"() {
        given: "a game repo"
        GameRepo gameRepo = new GameRepo(jdbcTemplate)

        when: "the game is saved"
        Game createdGame = gameRepo.createGame(GAME)

        then: "a game id is returned"
        createdGame.getGameId() == GAME_ID

        when: "the game is fetched"
        Optional<Game> fetchedGame = gameRepo.getGame(GAME_ID)

        then: "it has the expected parameters"
        assertGame(fetchedGame)

        when: "the game is changed"
        Game updatedGame = fetchedGame.get()
        updatedGame.setGameScreen(WORLDMAP)

        and: "an update is done"
        gameRepo.updateGame(updatedGame)

        and: "the changed game is fetched"
        Optional<Game> fetchedUpdatedGame = gameRepo.getGame(GAME_ID)

        then: "it has the expected parameters"
        assertGame(fetchedUpdatedGame, updatedGame)
    }

    def "Game CRUD with null values"() {
        given: "a game repo"
        GameRepo gameRepo = new GameRepo(jdbcTemplate)

        when: "the game is saved"
        Game createdGame = gameRepo.createGame(GAME_WITH_NULLS)

        then: "a game id is returned"
        createdGame.getGameId() == ANOTHER_GAME_ID

        when: "the game is fetched"
        Optional<Game> fetchedGame = gameRepo.getGame(ANOTHER_GAME_ID)

        then: "it has the expected parameters"
        assertGame(fetchedGame, GAME_WITH_NULLS)

        when: "the game is changed"
        Game updatedGame = fetchedGame.get()
        updatedGame.setGameScreen(MAPEDITOR)

        and: "an update is done"
        gameRepo.updateGame(updatedGame)

        and: "the changed game is fetched"
        Optional<Game> fetchedUpdatedGame = gameRepo.getGame(ANOTHER_GAME_ID)

        then: "it has the expected parameters"
        assertGame(fetchedUpdatedGame, updatedGame)
    }

    def assertGame(Optional<Game> result, Game comparator = GAME) {
        assert result.isPresent()
        Game resultGame = result.get()
        assert resultGame.getGameId() == comparator.getGameId()
        assert resultGame.getGameScreen() == comparator.getGameScreen()
        if (resultGame.getWorldMap() != null && comparator.getWorldMap() != null) {
            assert resultGame.getWorldMap().getWorldMapId() == comparator.getWorldMap().getWorldMapId()
        } else {
            assert resultGame.getWorldMap() == comparator.getWorldMap()
        }
        assert resultGame.getSelectedTileType() == comparator.getSelectedTileType()
        return true
    }
}

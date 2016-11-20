package com.ymdwiseguy.col.worldmap

import com.ymdwiseguy.Colonization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification


@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [Colonization])
@WebAppConfiguration
class TileRepoSpec extends Specification {

    @Autowired
    def JdbcTemplate jdbcTemplate

    Tile TILE
    Tile TILE2
    Tile TILE3
    String WORLD_MAP_ID
    String TILE_ID
    String TILE_ID2
    String TILE_ID3
    int X_COORDINATE
    int Y_COORDINATE
    TileType TILE_TYPE

    def setup() {
        TILE_ID = UUID.randomUUID().toString()
        TILE_ID2 = UUID.randomUUID().toString()
        TILE_ID3 = UUID.randomUUID().toString()
        WORLD_MAP_ID = UUID.randomUUID().toString()
        X_COORDINATE = 5
        Y_COORDINATE = 6
        TILE_TYPE = TileType.OCEAN_DEEP
        TILE = new Tile(TILE_ID, WORLD_MAP_ID, X_COORDINATE, Y_COORDINATE, TILE_TYPE)
        TILE2 = new Tile(TILE_ID2, WORLD_MAP_ID, X_COORDINATE, Y_COORDINATE, TILE_TYPE)
        TILE3 = new Tile(TILE_ID3, WORLD_MAP_ID, X_COORDINATE, Y_COORDINATE, TILE_TYPE)
    }

    def "Tile CRUD"() {
        given: "a tile repo"
        TileRepo tileRepo = new TileRepo(jdbcTemplate)

        when: "the tile is saved"
        String tileId = tileRepo.createTile(TILE)

        then: "a tile id is returned"
        tileId == TILE_ID

        when: "the tile is fetched"
        Optional<Tile> fetchedTile = tileRepo.getTile(tileId)

        then: "it has the expected parameters"
        assertTile(fetchedTile)

        when: "the tile is changed"
        Tile updatedTile = fetchedTile.get()
        updatedTile.setxCoordinate(20)
        updatedTile.setyCoordinate(-2)
        updatedTile.setType(TileType.OCEAN_SHALLOW)

        and: "an update is done"
        tileRepo.updateTile(updatedTile)

        and: "the changed tile is fetched"
        Optional<Tile> fetchedUpdatedTile = tileRepo.getTile(TILE_ID)

        then: "it has the expected parameters"
        assertTile(fetchedUpdatedTile, updatedTile)
    }

    def "save several new Tiles"() {
        given: "a tile repo"
        TileRepo tileRepo = new TileRepo(jdbcTemplate)

        and: "a list of tiles"
        ArrayList<Tile> tiles = [TILE,TILE2,TILE3]

        when: "the list is beeing saved"
        tileRepo.createTiles(tiles)

        and: "the tiles are beeing fetched"
        List<Tile> fetchedTileList = tileRepo.getTiles(WORLD_MAP_ID)

        then: "the items can be called from the db"
        fetchedTileList.size() == 3
        fetchedTileList.get(0).getTileId() == TILE.getTileId()
        fetchedTileList.get(1).getTileId() == TILE2.getTileId()
        fetchedTileList.get(2).getTileId() == TILE3.getTileId()
    }

    def assertTile(Optional<Tile> result, Tile comparator = TILE) {
        assert result.isPresent()
        Tile resultTile = result.get()
        assert resultTile.getTileId() == comparator.getTileId()
        assert resultTile.getxCoordinate() == comparator.getxCoordinate()
        assert resultTile.getyCoordinate() == comparator.getyCoordinate()
        assert resultTile.getType() == comparator.getType()
        return true
    }
}

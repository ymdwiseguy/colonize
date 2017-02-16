package com.ymdwiseguy.col.worldmap

import com.ymdwiseguy.Colonization
import com.ymdwiseguy.col.worldmap.tile.Tile
import com.ymdwiseguy.col.worldmap.tile.TileRepo
import com.ymdwiseguy.col.worldmap.tile.TileType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification
import spock.lang.Subject


@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [Colonization])
@WebAppConfiguration
class TileRepoSpec extends Specification {

    @Autowired
    def JdbcTemplate jdbcTemplate

    @Subject
    TileRepo tileRepo

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

        tileRepo = new TileRepo(jdbcTemplate)
    }

    def "Tile CRUD"() {
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
        given: "a list of tiles"
        ArrayList<Tile> tiles = [TILE, TILE2, TILE3]

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

    def "getting a limited number of tiles by radius"() {
        given: "a map with 6x6 tiles"
        String worldMapId = UUID.randomUUID().toString()
        List<Tile> allTiles = generateTiles(worldMapId, 6, 6)

        and: "a tile repo"
        TileRepo tileRepo = new TileRepo(jdbcTemplate)

        when: "the tiles are fetched with limit"
        List<Tile> partialTiles = tileRepo.getTilesLimited(worldMapId, 1, 3, 1, 3)

        then: "a limited list is returned"
        partialTiles.size() == 9
        assertPartialTiles(partialTiles)
    }

    boolean assertPartialTiles(List<Tile> tiles) {
        assert tiles.get(0).getxCoordinate() == 1
        assert tiles.get(0).getyCoordinate() == 1
        assert tiles.get(1).getxCoordinate() == 2
        assert tiles.get(1).getyCoordinate() == 1
        assert tiles.get(2).getxCoordinate() == 3
        assert tiles.get(2).getyCoordinate() == 1
        assert tiles.get(3).getxCoordinate() == 1
        assert tiles.get(3).getyCoordinate() == 2
        assert tiles.get(4).getxCoordinate() == 2
        assert tiles.get(4).getyCoordinate() == 2
        assert tiles.get(5).getxCoordinate() == 3
        assert tiles.get(5).getyCoordinate() == 2
        assert tiles.get(6).getxCoordinate() == 1
        assert tiles.get(6).getyCoordinate() == 3
        assert tiles.get(7).getxCoordinate() == 2
        assert tiles.get(7).getyCoordinate() == 3
        assert tiles.get(8).getxCoordinate() == 3
        assert tiles.get(8).getyCoordinate() == 3
        return true
    }

    List<Tile> generateTiles(String worldMapId, int width, int height) {
        List<Tile> tiles = new ArrayList<>()
        for (int y = 1; y <= width; y++) {
            for (int x = 1; x <= width; x++) {
                Tile tile = new Tile(
                    'tile'+x.toString()+y.toString(), worldMapId,
                    x, y,
                    TILE_TYPE
                )
                tiles.add(tile)
            }
        }
        tileRepo.createTiles(tiles)

        return tiles
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

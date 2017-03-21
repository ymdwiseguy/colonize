package com.ymdwiseguy.col.worldmap.tile

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static com.ymdwiseguy.col.worldmap.tile.TileAssetType.*
import static com.ymdwiseguy.col.worldmap.tile.TileType.LAND_ARCTIC
import static com.ymdwiseguy.col.worldmap.tile.TileType.LAND_GRASS
import static com.ymdwiseguy.col.worldmap.tile.TileType.LAND_MOUNTAINS
import static com.ymdwiseguy.col.worldmap.tile.TileType.OCEAN_DEEP
import static com.ymdwiseguy.col.worldmap.tile.TileType.OCEAN_SHALLOW

class TileAssetTogglerSpec extends Specification {

    @Subject
    TileAssetToggler tileAssetToggler

    String TILE_ID = "123"
    String WORLD_MAP_ID = "123"

    def setup() {
        tileAssetToggler = new TileAssetToggler()
    }

    @Unroll
    "setting #assetToSet has side effects (f:#forest,h:#hill,r:#river) => (f:#resultForest,h:#resultHill,r:#resultRiver)"() {
        given: "a tile with assets set"
        TileAssets tileAssets = new TileAssets(forest, hill, river)
        Tile tile = tileWithAsset(tileAssets)

        when: "an asset is set"
        tileAssetToggler.toggleAsset(tile, assetToSet)

        then: "the tile has the expected combination of assets"
        tile.getAssets().getForest() == resultForest
        tile.getAssets().getHill() == resultHill
        tile.getAssets().getRiver() == resultRiver

        where: "combinations"
        forest | hill  | river | assetToSet || resultForest | resultHill | resultRiver

        // Toggle just one asset
        false  | false | false | FOREST     || true         | false      | false
        false  | false | false | HILL       || false        | true       | false
        false  | false | false | RIVER      || false        | false      | true
        true   | false | false | FOREST     || false        | false      | false
        false  | true  | false | HILL       || false        | false      | false
        false  | false | true  | RIVER      || false        | false      | false

        // set asset with other existing assets
        false  | true  | false | FOREST     || true         | false      | false
        false  | false | true  | FOREST     || true         | false      | true

        true   | false | false | HILL       || false        | true       | false
        false  | false | true  | HILL       || false        | true       | false
        true   | false | true  | HILL       || false        | true       | false

        true   | false | false | RIVER      || true         | false      | true
        false  | true  | false | RIVER      || false        | false      | true
    }

    @Unroll "setting assets is #expected on #landtype"() {
        given: "a tile without assets"
        TileAssets tileAssets = new TileAssets(false, false, false)
        Tile tile = new Tile(TILE_ID, WORLD_MAP_ID, 1, 1, landtype, tileAssets)

        when: "an asset should be set"
        tileAssetToggler.toggleAsset(tile, FOREST)

        then: "result is as #expected"
        tileAssets.getForest() == expected

        where: "tile types"
        landtype       | expected
        LAND_ARCTIC    | true
        OCEAN_DEEP     | false
        OCEAN_SHALLOW  | false
        LAND_MOUNTAINS | false

    }

    Tile tileWithAsset(TileAssets tileAssets) {
        new Tile(TILE_ID, WORLD_MAP_ID, 1, 1, LAND_GRASS, tileAssets)
    }
}

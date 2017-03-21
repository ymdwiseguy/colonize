package com.ymdwiseguy.col.worldmap.tile;

import org.springframework.stereotype.Component;

@Component
public class TileAssetToggler {

    public Tile toggleAsset(Tile tile, TileAssetType assetType) {

        switch (tile.getType()){
            case OCEAN_DEEP:
            case OCEAN_SHALLOW:
            case LAND_MOUNTAINS:
                tile.setAssets(new TileAssets());
                return tile;
        }

        TileAssets tA = tile.getAssets();
        switch (assetType) {
            case FOREST:
                if (tA.getForest()) {
                    tA.setForest(false);
                } else {
                    tA.setForest(true);
                    tA.setHill(false);
                }
                break;
            case HILL:
                if (tA.getHill()) {
                    tA.setHill(false);
                } else {
                    tA.setHill(true);
                    tA.setForest(false);
                    tA.setRiver(false);
                }
                break;
            case RIVER:
                if (tA.getRiver()) {
                    tA.setRiver(false);
                } else {
                    tA.setRiver(true);
                    tA.setHill(false);
                }
                break;
        }

        return tile;
    }
}

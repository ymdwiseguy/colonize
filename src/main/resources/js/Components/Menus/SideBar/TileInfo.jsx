import React from 'react';

export const TileInfo = ({tiles, x, y}) => {

    let cursorX = x || 1;
    let cursorY = y || 1;

    let activeTile;
    tiles.map((tile) => {
        if (tile.xCoordinate === cursorX && tile.yCoordinate === cursorY) {
            activeTile = tile;
        }
    });

    let assets = '';
    if (activeTile.assets !== null) {
        if (activeTile.assets.hill) {
            assets += 'Hills ';
        }
        if (activeTile.assets.forest) {
            assets += 'Forest ';
        }
        if (activeTile.assets.river) {
            assets += 'River ';
        }
    }

    return (
        <div className="tile-info">
            <p>
                <span>Current Tile: {activeTile.type}</span><br/>
                <span>Tile assets: {assets}</span>
            </p>
        </div>
    );
};

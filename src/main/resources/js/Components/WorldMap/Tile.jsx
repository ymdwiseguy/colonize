import React from 'react';

// TODO: create connected redux version of this component

const Tile = ({tile}) => {
    let className = 'map-tile map-tile__' + tile.type +
        ' map-tile__x-' + tile.xCoordinate + ' map-tile__y-' + tile.yCoordinate;

    let forest = '';
    let hill = '';
    let river = '';
    if(tile.assets !== null){
        if(tile.assets.hill){
            hill = <span className="tileasset tileasset_hill"/>;
        }
        if(tile.assets.forest){
            forest = <span className="tileasset tileasset_forest"/>;
        }
        if(tile.assets.river){
            river = <span className="tileasset tileasset_river"/>;
        }
    }
    return (
        <div className={className}>
            {hill}
            {forest}
            {river}
        </div>
    )
};

export default Tile;

import React, {Component} from 'react';

class Tile extends Component {

    constructor(props) {
        super(props);
    }

    shouldComponentUpdate(nextProps, nextState) {
        return !Tile.tilesAreEqual(nextProps.tile, this.props.tile);
    }

    static tilesAreEqual(tile1, tile2) {
        if (tile1.assets.forest !== tile2.assets.forest) { return false; }
        if (tile1.assets.hill !== tile2.assets.hill) { return false; }
        if (tile1.assets.river !== tile2.assets.river) { return false; }
        if (tile1.tileId !== tile2.tileId) { return false; }
        if (tile1.type !== tile2.type) { return false; }
        if (tile1.worldMapId !== tile2.worldMapId) { return false; }
        if (tile1.xCoordinate !== tile2.xCoordinate) { return false; }
        return tile1.yCoordinate === tile2.yCoordinate;
    }

    render() {
        const {tile} = this.props;

        let className = 'map-tile map-tile__' + tile.type +
            ' map-tile__x-' + tile.xCoordinate + ' map-tile__y-' + tile.yCoordinate;

        let forest = '';
        let hill = '';
        let river = '';
        if (tile.assets !== null) {
            if (tile.assets.hill) {
                hill = <span className="tileasset tileasset_hill"/>;
            }
            if (tile.assets.forest) {
                forest = <span className="tileasset tileasset_forest"/>;
            }
            if (tile.assets.river) {
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
    }

}

export default Tile;
import React from 'react';
import TileAssets from './TileAssets.jsx';


class Tile extends React.Component {
    render() {
        let className = 'map-tile map-tile__' + this.props.tile.type +
            ' map-tile__x-' + this.props.tile.xCoordinate + ' map-tile__y-' + this.props.tile.yCoordinate;

        let forest = '';
        let hill = '';
        let river = '';
        if(this.props.tile.assets != null){
            if(this.props.tile.assets.hill){
                hill = <span className="tileasset tileasset_hill"/>;
            }
            if(this.props.tile.assets.forest){
                forest = <span className="tileasset tileasset_forest"/>;
            }
            if(this.props.tile.assets.river){
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

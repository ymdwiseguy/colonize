import React from 'react';
import TileAssets from './TileAssets.jsx';


class Tile extends React.Component {
    render() {
        let className = 'map-tile map-tile__' + this.props.tile.type +
            ' map-tile__x-' + this.props.tile.xCoordinate + ' map-tile__y-' + this.props.tile.yCoordinate;

        let forest = '';
        if(this.props.tile.assets != null){
            if(this.props.tile.assets.forest){
                forest = <span className="forest"/>;
            }
        }
        return (
            <div className={className}>
                {forest}
            </div>
        )
    }
}

export default Tile;

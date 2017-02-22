import React from 'react';


class Tile extends React.Component {
    render() {
        let className = 'map-tile map-tile__' + this.props.tile.type +
            ' map-tile__x-' + this.props.tile.xCoordinate + ' map-tile__y-' + this.props.tile.yCoordinate;

        return (
            <div className={className}/>
        )
    }
}

export default Tile;

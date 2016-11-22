import React from 'react';


class Tile extends React.Component{
    render () {
        let className = "map-tile map-tile__" + this.props.tile.type;

        return (
            <div className={className} />
        )
    }
}

export default Tile;

import React from 'react';
import Unit from '../unit.jsx';
import Tile from '../tile.jsx';
import Cursor from './Cursor.jsx';


class Map extends React.Component {

    render() {
        if (this.props.data) {
            let units = '';
            let cursor = null;
            let tileList;

            if (this.props.data.tiles) {
                if (this.props.cursor) {
                    cursor = <Cursor cursorX={this.props.cursor.xPosition} cursorY={this.props.cursor.yPosition} cursorActive={this.props.cursor.active}/>;
                }

                tileList = this.props.data.tiles.map((tile, i) => {
                    let key = 'tile_' + tile.xCoordinate + '_' + tile.yCoordinate;
                    return <Tile tile={tile} key={key}/>
                });
            }

            if (this.props.data.units) {
                units = this.props.data.units.map((unit, i) => {
                    return <Unit unit={unit} key={i}/>
                });
            }
            let classname = 'map-main-wrapper map-main-wrapper--width-' + this.props.data.width;
            return (
                <div className={classname}>
                    {tileList}
                    {units}
                    {cursor}
                </div>
            )
        } else {
            return null;
        }

    }
}

export default Map;

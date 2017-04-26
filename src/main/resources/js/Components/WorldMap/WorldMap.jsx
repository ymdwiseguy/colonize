import React from 'react';
import Unit from '../../unit.jsx';
import Tile from './Tile.jsx';
import Cursor from './Cursor.jsx';

const WorldMap = ({data, cursor}) => {

    if(data === null){
        return null;
    }

    let units = '';
    let cursorElement = null;
    let tileList;

    if (data.tiles) {
        if (cursor) {
            cursorElement =
                <Cursor cursorX={cursor.xPosition} cursorY={cursor.yPosition} cursorActive={cursor.active}/>;
        }

        tileList = data.tiles.map((tile, i) => {
            let key = 'tile_' + tile.xCoordinate + '_' + tile.yCoordinate;
            return <Tile tile={tile} key={key}/>
        });
    }

    if (data.units) {
        units = data.units.map((unit, i) => {
            return <Unit unit={unit} key={i}/>
        });
    }
    let classname = 'map-main-wrapper map-main-wrapper--width-' + data.width;
    return (
        <div className={classname}>
            {tileList}
            {units}
            {cursorElement}
        </div>
    )
};

export default WorldMap;

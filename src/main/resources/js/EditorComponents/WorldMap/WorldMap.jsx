import React from 'react';
import PropTypes from 'prop-types';
import Unit from '../Units/Unit.jsx';
import Tile from './Tile.jsx';
import Cursor from './Cursor.jsx';

const WorldMap = ({data, cursor}) => {

    if (data === null) {
        return <div className="map-main-wrapper">&nbsp;</div>;
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

WorldMap.propTypes = {
    data: PropTypes.object
};

export default WorldMap;

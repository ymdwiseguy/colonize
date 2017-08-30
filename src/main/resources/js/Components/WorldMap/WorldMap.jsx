import React from 'react';
import {connect} from 'react-redux'
import PropTypes from 'prop-types';

import Unit from '../../EditorComponents/Units/Unit.jsx';
import Tile from '../../EditorComponents/WorldMap/Tile.jsx';
import Cursor from './Cursor.jsx';


const mapGameStateToProps = (state) => ({
    worldMap: state.worldMap.mapData
});


const WorldMap = ({worldMap}) => {

    if (worldMap === null) {
        return <div className="map-main-wrapper">&nbsp;</div>;
    }

    let units = '';
    let tileList;

    if (worldMap.tiles) {
        tileList = worldMap.tiles.map((tile, i) => {
            let key = 'tile_' + tile.xCoordinate + '_' + tile.yCoordinate;
            return <Tile tile={tile} key={key}/>
        });
    }

    if (worldMap.units) {
        units = worldMap.units.map((unit, i) => {
            return <Unit unit={unit} key={i}/>
        });
    }
    let classname = 'map-main-wrapper map-main-wrapper--width-' + worldMap.width;
    return (
        <div className={classname}>
            {tileList}
            {units}
            <Cursor/>
        </div>
    )
};

WorldMap.propTypes = {
    worldMap: PropTypes.object
};

const ConnectedWorldMap = connect(mapGameStateToProps, null)(WorldMap);


export default ConnectedWorldMap;

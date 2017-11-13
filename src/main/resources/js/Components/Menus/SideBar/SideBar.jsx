import React from 'react';
import {connect} from 'react-redux'
import {CoordinateInfo} from './CoordinateInfo.jsx'
import UnitInfo from './UnitInfo.jsx';
import {TileInfo} from "./TileInfo.jsx";

const mapGameStateToProps = (state) => ({
    worldMap: state.worldMap
});

const SubMenu = ({worldMap}) => {

    let content;
    let tileInfo;
    let activeCoordinates = getActiveCoordinates(worldMap);

    if (worldMap.whoIsActive > 0) {
        content = <UnitInfo/>;
    }

    if (worldMap.mapData !== undefined && worldMap.mapData.tiles !== undefined && worldMap.mapData.tiles.length > 0) {
        tileInfo = <TileInfo tiles={worldMap.mapData.tiles} x={activeCoordinates[0]} y={activeCoordinates[1]}/>;
    }

    return (
        <div className="sidebar--inner">
            <h2 className="sidebar__header">Info</h2>
            <CoordinateInfo x={activeCoordinates[0]} y={activeCoordinates[1]}/>
            {tileInfo}
            {content}
        </div>
    );
};


function getActiveCoordinates(worldMap) {

    if (worldMap.whoIsActive === 0) {
        return [worldMap.cursor.cursorX, worldMap.cursor.cursorY]
    }
    let activeUnit;
    worldMap.units.map((unit) => {
        if (unit.active === true) {
            activeUnit = unit;
        }
    });

    return [activeUnit.xPosition, activeUnit.yPosition]
}

export default connect(mapGameStateToProps, null)(SubMenu);

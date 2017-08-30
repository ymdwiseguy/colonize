import React from 'react';
import {connect} from 'react-redux'

import FileSubmenu from '../Menus/Implementations/FileSubmenu.jsx'
import WorldMap from '../../Components/WorldMap/WorldMap.jsx';


const mapGameStateToProps = (state) => ({
    worldMap: state.worldMap.mapData
});

const Game = ({worldMap}) => {
    return (
        <div className="frame">
            <div className="main-menu">
                <ul className="main-menu__menulevel-one">
                    <FileSubmenu/>
                </ul>
            </div>
            <div className="map-outer-wrapper">
                <WorldMap/>
            </div>
            <div className="sidebar">
                <div className="sidebar--inner">
                    <h2 className="sidebar__header">Sidebar</h2>
                </div>
            </div>
        </div>
    );
};


const GameFrame = connect(mapGameStateToProps, null)(Game);

export default GameFrame;

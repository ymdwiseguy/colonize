import React from 'react';
import {connect} from 'react-redux'

import FileSubmenu from '../Menus/Implementations/FileSubmenu.jsx'
import WorldMap from '../../EditorComponents/WorldMap/WorldMap.jsx';


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
            Render game for map &quot;{worldMap.title}&quot;
        </div>
    );
};


const GameFrame = connect(mapGameStateToProps, null)(Game);

export default GameFrame;

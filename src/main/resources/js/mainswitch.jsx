import React from 'react';

import EditorFrame from './EditorFrame.jsx';
import GameFrame from './gameframe.jsx';


const MainSwitch = ({game}) => {
    let output;
    switch(game.gameScreen){
        case 'MAPEDITOR':
            output = <EditorFrame game={game}/>;
            break;
        case 'WORLDMAP':
            output = <GameFrame game={game}/>;
            break;

        default:
            output = <GameFrame game={game}/>;
    }
    return (
        output
    );
};

export default MainSwitch;

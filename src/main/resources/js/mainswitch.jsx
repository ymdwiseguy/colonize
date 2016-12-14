import React from 'react';
import {render} from 'react-dom';

import EditorFrame from './editorframe.jsx';
import GameFrame from './gameframe.jsx';

class MainSwitch extends React.Component {

    render() {
        let output;
        switch(this.props.game.gameScreen.toLowerCase()){
            case 'MAPEDITOR':
                output = <EditorFrame mapdata={this.props.game.worldMap}/>;
                break;
            case 'WORLDMAP':
                output = <GameFrame mapdata={this.props.game.worldMap}/>;
                break;

            default:
                output = <GameFrame mapdata={this.props.game.worldMap}/>;
        }
        return (
            output
        );

    }
}

export default MainSwitch;

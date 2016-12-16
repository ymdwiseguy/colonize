import React from 'react';
import {render} from 'react-dom';

import EditorFrame from './editorframe.jsx';
import GameFrame from './gameframe.jsx';

class MainSwitch extends React.Component {

    render() {
        let output;
        switch(this.props.game.gameScreen){
            case 'MAPEDITOR':
                output = <EditorFrame game={this.props.game}/>;
                break;
            case 'WORLDMAP':
                output = <GameFrame game={this.props.game}/>;
                break;

            default:
                output = <GameFrame game={this.props.game}/>;
        }
        return (
            output
        );

    }
}

export default MainSwitch;

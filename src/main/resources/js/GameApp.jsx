import {render} from 'react-dom';
import React from 'react';

// import MainSwitch from './mainswitch.jsx';
// TODO: build main menu -> map editor, new game, load game ...


const MainMenu = () => {
    return <div><p>render game here</p></div>;
};


const Game = () => {
    return <MainMenu/>;
};

render(<Game />, document.getElementById('col-body'));

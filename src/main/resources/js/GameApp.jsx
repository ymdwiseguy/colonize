import {render} from 'react-dom';
import React from 'react';
import Link from './Components/BaseComponents/Link.jsx';

import MenuEntry from './Components/Menus/MenuEntry/MenuEntry.jsx';

// import MainSwitch from './mainswitch.jsx';
// TODO: build main menu -> map editor, new game, load game ...

const mainMenu = {
    "header": "Menu",
    "entries": [{
        "entryName": "Start a new game",
        "endpointUrl": "/",
        "method": "GET",
        "active": false
    }, {
        "entryName": "Load a game",
        "endpointUrl": "/",
        "method": "GET",
        "active": false
    }, {
        "entryName": "Map editor",
        "endpointUrl": "/mapeditor",
        "method": "GET",
        "active": false
    }]
};

const MainMenu = ({mainMenu}) => {



    let menuentries = mainMenu.entries.map((entry, i) => {
        return <Link href={entry.endpointUrl} key={i} >{entry.entryName}</Link>
    });

    return (
        <div className="popup loadGamePopup">
            <h2>{mainMenu.header}</h2>
            <ul className="popup__entryList">
                {menuentries}
            </ul>
        </div>
    )
};


const Game = () => {
    return <MainMenu mainMenu={mainMenu}/>;
};

render(<Game />, document.getElementById('col-body'));



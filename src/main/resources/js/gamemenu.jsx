import React from 'react';
import {render} from 'react-dom';

import Submenu from './submenu.jsx';

class GameMenu extends React.Component {

    constructor(props) {
        super(props);
    }

    handleGameMenuClick(event, method) {
        this.props.onClickFrame(event, method);
    }

    render() {
        let submenus = this.props.menu.submenus.map((submenu, i) => {
            return <Submenu submenu={submenu} key={i} onClickMainMenu={(e, m) => this.handleGameMenuClick(e, m)}/>
        });

        return (
            <div className="main-menu">
                <ul className="main-menu__menulevel-one">
                    {submenus}
                </ul>
            </div>
        );
    }
}

export default GameMenu;

import React from 'react';
import {render} from 'react-dom';

import Submenu from './submenu.jsx';

class GameMenu extends React.Component {

    constructor(props) {
        super(props);
        this.handleGameMenuClick = this.handleGameMenuClick.bind(this);
    }

    handleGameMenuClick(event) {
        this.props.onClickFrame(event);
    }

    render() {
        let submenus = this.props.menu.submenus.map((submenu, i) => {
            return <Submenu submenu={submenu} key={i} onClickMainMenu={this.handleGameMenuClick}/>
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

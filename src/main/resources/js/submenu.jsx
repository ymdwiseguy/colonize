import React from 'react';
import {render} from 'react-dom';

import MenuEntry from './menuentry.jsx';

class Submenu extends React.Component {

    constructor(props) {
        super(props);
        this.handleSubmenuClick = this.handleSubmenuClick.bind(this);
    }

    handleSubmenuClick(event) {
        this.props.onClickMainMenu(event);
    }

    render() {
        let menuentries = this.props.submenu.menuEntries.map((entry, i) => {
            return <MenuEntry entry={entry} key={i} onClickSubmenu={this.handleSubmenuClick}/>
        });

        return (
            <li>
                <span className="main-menu__menulevel-two--headline">{this.props.submenu.entryName}</span>
                <ul className="main-menu__menulevel-two">
                    {menuentries}
                </ul>
            </li>
        );
    }
}

export default Submenu;





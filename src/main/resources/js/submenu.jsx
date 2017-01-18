import React from 'react';
import {render} from 'react-dom';

import MenuEntry from './menuentry.jsx';

class Submenu extends React.Component {

    constructor(props) {
        super(props);
    }

    handleSubmenuClick(event, method) {
        this.props.onClickMainMenu(event, method);
    }

    render() {
        let menuentries = this.props.submenu.entries.map((entry, i) => {
            return <MenuEntry entry={entry} key={i} onClickSubmenu={(e, m) => this.handleSubmenuClick(e, m)}/>
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





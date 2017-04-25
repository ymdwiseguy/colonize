import React from 'react';

import MenuEntry from './Components/MenuEntry/MenuEntry.jsx';

class Submenu extends React.Component {

    constructor(props) {
        super(props);
        // this.handleSubmenuClick = this.handleSubmenuClick.bind(this)
    }

    handleSubmenuClick(event, method) {
        this.props.onClickMainMenu(event, method);
    }

    render() {
        let menuentries = this.props.submenu.entries.map((entry, i) => {
            return <MenuEntry
                entry={entry} key={i}
                onClickSubmenu={(e, m) => this.handleSubmenuClick(e, entry.method)}
            />
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





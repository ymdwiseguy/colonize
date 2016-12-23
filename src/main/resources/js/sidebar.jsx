import React from 'react';
import {render} from 'react-dom';

import MenuEntry from './menuentry.jsx';

class SideBar extends React.Component {

    constructor(props) {
        super(props);
        this.handleSideBarClick = this.handleSideBarClick.bind(this);
    }

    handleSideBarClick(event) {
        this.props.onClickFrame(event);
    }

    render() {
        if (this.props.sidebar) {
            let entries = this.props.sidebar.entries.map((entry, i) => {
                return <MenuEntry entry={entry} key={i} onClickSubmenu={this.handleSideBarClick}/>
            });

            return (
                <div className="sidebar">
                    <h2 className="sidebar__header">{this.props.sidebar.header}</h2>
                    <ul className="main-menu__menulevel-one">
                        {entries}
                    </ul>
                </div>
            );
        } else {
            return (
                <div className="sidebar">&nbsp;</div>
            )
        }
    }
}

export default SideBar;

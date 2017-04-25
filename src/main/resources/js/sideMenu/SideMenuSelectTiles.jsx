import React from 'react';

import SelectableTiles from './SelectableTiles.jsx';

class SideMenuSelectTiles extends React.Component {

    constructor(props) {
        super(props);
        this.handleSideBarClick = this.handleSideBarClick.bind(this);
    }

    handleSideBarClick(event, method) {
        this.props.onClickFrame(event, method);
    }

    render() {
        if (this.props.sidebar) {
            let entries = this.props.sidebar.entries.map((entry, i) => {
                return <SelectableTiles entry={entry} selected={this.props.selected} key={i}
                                        onClickSubmenu={(e, m) => this.handleSideBarClick(e, m)}/>
            });

            return (
                <div className="sidebar sidebar--select_tiles">
                    <div className="sidebar--inner">
                        <h2 className="sidebar__header">{this.props.sidebar.header}</h2>
                        <ul className="sidebar-menu__menulevel-one">
                            {entries}
                        </ul>
                    </div>
                </div>
            );
        } else {
            return (
                <div className="sidebar">&nbsp;</div>
            )
        }
    }
}

export default SideMenuSelectTiles;

import React from 'react';

import MenuEntry from '../MenuEntry/MenuEntry.jsx';

const MapEditorSidebar = ({sidebar, onClickFrame}) => {
    if (sidebar) {
        let entries = sidebar.entries.map((entry, i) => {
            return <MenuEntry entry={entry} key={i} onClickSubmenu={onClickFrame}/>
        });

        return (
            <div className="sidebar">
                <div className="sidebar--inner">
                    <h2 className="sidebar__header">{sidebar.header}</h2>
                    <ul className="main-menu__menulevel-one">
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
};

export default MapEditorSidebar;

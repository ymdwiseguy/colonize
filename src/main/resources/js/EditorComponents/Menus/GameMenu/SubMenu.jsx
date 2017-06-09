import React from 'react';

import MenuEntry from '../MenuEntry/MenuEntry.jsx';

const SubMenu = ({submenu, onClickMainMenu}) => {
    let menuentries = submenu.entries.map((entry, i) => {
        return <MenuEntry
            entry={entry} key={i}
            onClickSubmenu={(e, m) => onClickMainMenu(e, entry.method)}
        />
    });

    return (
        <li>
            <span className="main-menu__menulevel-two--headline">{submenu.entryName}</span>
            <ul className="main-menu__menulevel-two">
                {menuentries}
            </ul>
        </li>
    );
};

export default SubMenu;





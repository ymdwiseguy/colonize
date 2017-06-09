import React from 'react';
import MenuEntry from '../MenuEntry.jsx';

const SubMenu = ({entries, header, onClickMainMenu}) => {
    let menuentries = entries.map((entry, i) => {
        return (
            <MenuEntry key={i}
                       onClickSubmenu={(e, t) => onClickMainMenu(entry.action)}>
                {entry.entryName}
            </MenuEntry>
        );
    });

    return (
        <li>
            <span className="main-menu__menulevel-two--headline">
                {header}
            </span>
            <ul className="main-menu__menulevel-two">
                {menuentries}
            </ul>
        </li>
    );
};

export default SubMenu;





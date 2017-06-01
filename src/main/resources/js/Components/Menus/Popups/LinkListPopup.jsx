import React from 'react';

const MenuEntry = ({onClickFunction, classname, children}) => {
    return (
        <li
            className={classname}
            onClick={onClickFunction}>{children}
        </li>
    )
};


const LinkListPopup = ({entries, header, onMenuItemClick}) => {

    let menuentries = entries.map((entry, i) => {
        return (
            <MenuEntry key={i} onClickFunction={(e, t) => onMenuItemClick(entry.action)} classname="link">
                {entry.entryName}
            </MenuEntry>
        );
    });

    return (
        <div className="popup loadGamePopup">
            <h2>{header}</h2>
            <ul className="popup__entryList">
                {menuentries}
            </ul>
        </div>
    )
};


export default LinkListPopup;

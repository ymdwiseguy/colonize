import React from 'react';
import MenuEntry from '../MenuEntry.jsx'


const LinkListPopup = ({entries, header, onMenuItemClick}) => {

    let menuentries = entries.map((entry, i) => {
        return (
            <MenuEntry key={i}
                       onClickFunction={(e, t) => onMenuItemClick(entry.action)}>
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

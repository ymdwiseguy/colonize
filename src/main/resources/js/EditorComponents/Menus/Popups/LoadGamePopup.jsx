import React from 'react';

import MenuEntry from '../MenuEntry/MenuEntry.jsx';

const LoadGamePopup = ({popupMenu, onClickFrame}) => {
    if (popupMenu.entries.length === 0) {
        return null;
    }

    let menuentries = popupMenu.entries.map((entry, i) => {
        return <MenuEntry
            entry={entry}
            key={i}
            onClickSubmenu={
                (e, m) => onClickFrame(e, entry.method)
            }/>
    });

    return (
        <div className="popup loadGamePopup">
            <h2>{popupMenu.header}</h2>
            <ul className="popup__entryList">
                {menuentries}
            </ul>
        </div>
    )

};

export default LoadGamePopup;

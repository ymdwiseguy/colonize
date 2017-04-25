import React from 'react';


const MenuEntry = ({entry, onClickSubmenu}) => {
    return (
        <li>
            <a
                href={entry.endpointUrl}
                method={entry.method}
                onClick={onClickSubmenu}>{entry.entryName}
            </a>
        </li>
    );
};


export default MenuEntry;

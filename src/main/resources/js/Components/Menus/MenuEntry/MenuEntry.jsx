import React from 'react';
import AnchorLink from '../../BaseComponents/AnchorLink.jsx';

const MenuEntry = ({entry, onClickSubmenu}) => {
    return (
        <li>
            <AnchorLink
                href={entry.endpointUrl}
                method={entry.method}
                onClickFunction={onClickSubmenu}>{entry.entryName}
            </AnchorLink>
        </li>
    );
};


export default MenuEntry;

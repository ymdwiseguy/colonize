import React from 'react';
import Link from '../../BaseComponents/Link.jsx';

const MenuEntry = ({entry, onClickSubmenu}) => {
    return (
        <li>
            <Link
                href={entry.endpointUrl}
                method={entry.method}
                onClickFunction={onClickSubmenu}>{entry.entryName}
            </Link>
        </li>
    );
};


export default MenuEntry;

import React from 'react';

const SelectableTiles = ({entry, selected, onClickSubmenu}) => {
    let isActive = (selected === entry.entryName);
    let className = "tile map-tile__" + entry.entryName + (isActive ? ' active' : '');
    return (
        <li className={className}>
            <a
                href={entry.endpointUrl}
                method={entry.method}
                onClick={onClickSubmenu}>
                {entry.entryName}
            </a>
        </li>
    );
};

export default SelectableTiles;

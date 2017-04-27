import React from 'react';

import SubMenu from './SubMenu.jsx';

const GameMenu = ({menu, onClickFrame}) => {
    if (!menu) {
        return <div className="main-menu">&nbsp;</div>;
    }
    let submenus = menu.submenus.map((submenu, i) => {
        return <SubMenu
            submenu={submenu}
            key={i}
            onClickMainMenu={onClickFrame}
        />
    });

    return (
        <div className="main-menu">
            <ul className="main-menu__menulevel-one">
                {submenus}
            </ul>
        </div>
    );
};

export default GameMenu;

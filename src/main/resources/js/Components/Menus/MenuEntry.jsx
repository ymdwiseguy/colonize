import React from 'react';
import PropTypes from 'prop-types';

const MenuEntry = ({onClickFunction, classname, children}) => {
    return (
        <li
            className={classname}
            onClick={onClickFunction}>{children}
        </li>
    )
};

MenuEntry.propTypes = {
    classname: PropTypes.string
};

MenuEntry.defaultProps = {
    classname: 'link'
};

export default MenuEntry;

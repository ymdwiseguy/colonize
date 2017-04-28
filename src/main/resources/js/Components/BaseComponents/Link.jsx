import React from 'react';
import PropTypes from 'prop-types';

const Link = ({href, method, onClickFunction, classname, children}) => {
    return (
        <a
            href={href}
            className={classname}
            method={method}
            onClick={onClickFunction}>{children}
        </a>
    )
};

Link.propTypes = {
    classname: PropTypes.string
};

Link.defaultProps = {
   classname: 'link'
};

export default Link;

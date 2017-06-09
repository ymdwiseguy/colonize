import React from 'react';
import PropTypes from 'prop-types';

const AnchorLink = ({href, method, onClickFunction, classname, children}) => {
    return (
        <a
            href={href}
            className={classname}
            method={method}
            onClick={onClickFunction}>{children}
        </a>
    )
};

AnchorLink.propTypes = {
    classname: PropTypes.string
};

AnchorLink.defaultProps = {
   classname: 'link'
};

export default AnchorLink;

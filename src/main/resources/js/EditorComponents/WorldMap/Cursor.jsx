import React from 'react';

const Cursor = ({cursorX, cursorY, cursorActive}) => {
    let className = 'cursor cursor-x' + cursorX + ' cursor-y' + cursorY;
    if(cursorActive === true){
        className += ' cursor--active';
    }
    return <div className={className}>&nbsp;</div>;
};

export default Cursor;

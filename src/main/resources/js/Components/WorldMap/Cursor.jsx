import React from 'react';
import {connect} from 'react-redux'

const mapStateToProps = (state) => ({
    cursor: state.cursor
});


const Cursor = ({cursor}) => {
    let className = 'cursor cursor-x' + cursor.cursorX + ' cursor-y' + cursor.cursorY;
    if(cursor.cursorActive === true){
        className += ' cursor--active';
    }
    return <div className={className}>&nbsp;</div>;
};

const ConnectedCursor = connect(mapStateToProps, null)(Cursor);

export default ConnectedCursor;

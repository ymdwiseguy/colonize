import React from 'react';
import {connect} from 'react-redux'

const mapStateToProps = (state) => ({
    cursorActive: state.cursor.cursorActive,
    cursorX: state.cursor.cursorX,
    cursorY: state.cursor.cursorY
});

const Cursor = ({cursorActive, cursorX, cursorY}) => {
    let className = 'cursor cursor-x' + cursorX + ' cursor-y' + cursorY;
    if(cursorActive === true){
        className += ' cursor--active';
    }
    return <div className={className}>&nbsp;</div>;
};

const ConnectedCursor = connect(mapStateToProps, null)(Cursor);

export default ConnectedCursor;

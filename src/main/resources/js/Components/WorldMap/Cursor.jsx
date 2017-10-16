import React, {Component} from 'react';
import {connect} from 'react-redux'

const mapStateToProps = (state) => ({
    cursorActive: state.worldMap.cursor.cursorActive,
    cursorX: state.worldMap.cursor.cursorX,
    cursorY: state.worldMap.cursor.cursorY
});

const Cursor = ({cursorActive, cursorX, cursorY}) => {
    let className = 'cursor cursor-x' + cursorX + ' cursor-y' + cursorY;
    if (cursorActive === true) {
        className += ' cursor--active';
    }
    return <div className={className}>&nbsp;</div>;
};

export default connect(mapStateToProps, null)(Cursor);

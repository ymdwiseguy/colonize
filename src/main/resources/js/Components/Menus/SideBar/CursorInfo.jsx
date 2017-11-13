import React from 'react';
import {connect} from 'react-redux'


const mapGameStateToProps = (state) => ({
    cursor: state.worldMap.cursor
});

const CursorInfo = ({cursor}) => {

    let cursorX = cursor.cursorX || 0;
    let cursorY = cursor.cursorY || 0;

    return (
        <div>
            <span>X: {cursorX}</span>
            <span>Y: {cursorY}</span>
        </div>
    ) ;
};

export default connect(mapGameStateToProps, null)(CursorInfo);

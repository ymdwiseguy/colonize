import React from 'react';
import {connect} from 'react-redux'
import CursorInfo from './CursorInfo.jsx';
import UnitInfo from './UnitInfo.jsx';

const mapGameStateToProps = (state) => ({
    worldMap: state.worldMap
});

const SubMenu = ({worldMap}) => {

    let content;
    if (worldMap.whoIsActive === 0) {
        content = <CursorInfo/>;
    }else{
        content = <UnitInfo/>;
    }

    return (
        <div className="sidebar--inner">
            <h2 className="sidebar__header">Info</h2>
            {content}
        </div>
    );
};


export default connect(mapGameStateToProps, null)(SubMenu);

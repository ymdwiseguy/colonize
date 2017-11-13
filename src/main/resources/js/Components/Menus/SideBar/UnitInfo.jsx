import React from 'react';
import {connect} from 'react-redux'


const mapGameStateToProps = (state) => ({
    units: state.worldMap.units
});

const UnitInfo = ({units}) => {

    let activeUnit;
    units.map((unit) => {
        if (unit.active === true) {
            activeUnit = unit;
        }
    });

    return (
        <div className="unit-info">
            <p>
                <span>X: {activeUnit.xPosition} </span>
                <span>Y: {activeUnit.yPosition} </span>
            </p>
            <p>
                <span>{activeUnit.unitType} ({activeUnit.unitId})</span>
            </p>
        </div>
    );
};

export default connect(mapGameStateToProps, null)(UnitInfo);

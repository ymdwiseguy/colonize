import React from 'react';

const Unit = ({unit}) => {
    let className = 'unit unit__' + unit.unitType + ' unit__xpos-' + unit.xPosition + ' unit__ypos-' + unit.yPosition;
    let unitId = 'unit_' + unit.unitId;

    if (unit.active) {
        className += ' unit--active';
    }

    return (
        <div className={className} id={unitId}>&nbsp;</div>
    );
};

export default Unit;

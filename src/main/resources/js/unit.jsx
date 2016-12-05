import React from 'react';

class Unit extends React.Component {

    constructor() {
        super();
    }

    componentWillUnMount() {
        $(document.body).off('keydown', this.handleKeyDown);
    }

    render() {
        let className = 'unit unit__' + this.props.unit.unitType + ' unit__xpos-' + this.props.unit.xPosition + ' unit__ypos-' + this.props.unit.yPosition;
        let unitId = 'unit_' + this.props.unit.unitId;

        if (this.props.unit.active) {
            className += ' unit--active';
        }

        return (
            <div className={className} id={unitId}>&nbsp;</div>
        );
    }
}

export default Unit;

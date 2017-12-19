import React, {Component} from 'react';
import {connect} from 'react-redux'

import {bindActionCreators} from 'redux'
import * as actions from '../../Actions/Actions.jsx'
import {factions} from "../../Reducers/SimpleReducers.jsx";

const mapStateToProps = (state, ownProps) => {
    return {ownProps}
};

const mapDispatchToProps = (dispatch) => ({
    clickUnitAction: bindActionCreators(actions.unitClicked, dispatch)
});

class Unit extends Component {

    constructor(props) {
        super(props);
        this.handleClickEvent = this.handleClickEvent.bind(this);
    }

    handleClickEvent() {
        const {unit} = this.props;
        this.props.clickUnitAction(unit.xPosition, unit.yPosition, unit.unitId);
    }

    shouldComponentUpdate(nextProps, nextState) {
        return !Unit.unitsAreEqual(nextProps.unit, this.props.unit);
    }

    static unitsAreEqual(unit1, unit2) {
        if (unit1.unitType !== unit2.unitType) {
            return false;
        }
        if (unit1.active !== unit2.active) {
            return false;
        }
        if (unit1.xPosition !== unit2.xPosition) {
            return false;
        }
        return unit1.yPosition === unit2.yPosition;
    }

    render() {
        const {unit} = this.props;

        let className = 'unit unit__' + unit.unitType + ' unit__xpos-' + unit.xPosition + ' unit__ypos-' + unit.yPosition + ' unit__faction';
        if (factions.includes(unit.faction)) {
            className += ' unit__faction--' + unit.faction
        }
        let unitId = 'unit_' + unit.unitId;

        if (unit.active) {
            className += ' unit--active';
        }

        return (
            <div className={className} id={unitId} onClick={this.handleClickEvent}>&nbsp;</div>
        );
    }
}

// export default Unit;
export default connect(mapStateToProps, mapDispatchToProps)(Unit);


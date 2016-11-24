import React from 'react';

class Unit extends React.Component {

    constructor() {
        super();
        this.state = {
            move: ""
        };
    }

    componentDidMount() {
        this.setState({ unit: this.props.unit });
        $(document.body).on('keydown', this.handleKeyDown.bind(this));
    }

    componentWillUnMount() {
        $(document.body).off('keydown', this.handleKeyDown);
    }

    handleKeyDown(event) {

        var newUnit = this.props.unit;
        switch (event.keyCode) {
            case 37: // left
                newUnit.xPosition -= 1;
                break;
            case 38: // up
                newUnit.yPosition -= 1;
                break;
            case 39: // right
                newUnit.xPosition += 1;
                break;
            case 40: // down
                newUnit.yPosition += 1;
                break;
        }
        this.setState({unit: newUnit});
    }

    render() {
        if(this.state.unit){

            let className = 'unit unit__' + this.state.unit.unitType + ' unit__xpos-' + this.state.unit.xPosition + ' unit__ypos-' + this.state.unit.yPosition;
            let unitId = 'unit_' + this.state.unit.unitId;

            if (this.state.unit.active) {
                className += ' unit--active';
            }

            return (
                <div className={className} id={unitId} onKeyDown={this.handleKeyDown}>&nbsp;</div>
            )
        }else {
            return (
                <div/>
            )
        }
    }
}

export default Unit;

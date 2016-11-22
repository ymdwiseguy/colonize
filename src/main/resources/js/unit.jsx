const Unit = React.createClass({

    getInitialState: function () {
        return {
            move: ""
        };
    },
    componentDidMount: function () {
        $(document.body).on('keydown', this.handleKeyDown);
    },

    componentWillUnMount: function () {
        $(document.body).off('keydown', this.handleKeyDown);
    },

    handleKeyDown: function (event) {
        let unit = $('#unit_'+this.props.unit.unitId);
        switch (event.keyCode) {
            case 37: // left
                unit.animate({left:'-=100'});
                break;
            case 38: // up
                unit.animate({top:'-=100'});
                break;
            case 39: // right
                unit.animate({left:'+=100'});
                break;
            case 40: // down
                unit.animate({top:'+=100'});
                break;
        }
    },

    render: function () {
        let className = 'unit unit__' + this.props.unit.type + ' unit__xpos-'+this.props.unit.x_position + ' unit__ypos-'+this.props.unit.y_position;
        let unitId = 'unit_'+this.props.unit.unitId;

        if (this.props.unit.active) {
            className += ' unit--active';
        }

        return (
            <div className={className} id={unitId} onKeyDown={this.handleKeyDown}>&nbsp;</div>
        )
    }
});

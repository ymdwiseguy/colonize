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
        switch (event.keyCode) {
            case 37:
                this.setState({move: 'left'});
                break;
            case 38:
                this.setState({move: 'up'});
                break;
            case 39:
                this.setState({move: 'right'});
                break;
            case 40:
                this.setState({move: 'down'});
                break;
        }
    },

    render: function () {
        let className = "unit unit__" + this.props.unit.type;
        if (this.props.unit.active) {
            className += ' unit--active';
        }
        if (this.state.move.length > 0) {
            className += ' unit--move unit--move-' + this.state.move;
        }

        return (
            <div className={className} onKeyDown={this.handleKeyDown}>&nbsp;</div>
        )
    }
});

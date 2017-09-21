import React, {Component} from 'react';
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import * as actions from '../../Actions/Actions.jsx'

const mapStateToProps = (state) => ({
    cursorActive: state.viewPort.cursorActive,
    cursorX: state.viewPort.cursorX,
    cursorY: state.viewPort.cursorY
});

const mapDispatchToProps = (dispatch) => ({
    viewPortUpdate: bindActionCreators(actions.viewPortChangeCanvasSize, dispatch),
    adjustViewPort: bindActionCreators(actions.adjustViewPort, dispatch)
});

class Cursor extends Component {
    constructor() {
        super();
        this.handleViewPortUpdate = this.handleViewPortUpdate.bind(this);
    }

    handleViewPortUpdate() {
        this.getViewPortValues()
    }

    getViewPortValues() {
        let mapFrame = $('.map-outer-wrapper');
        let frameWidth = parseInt(mapFrame.css('width'));
        let frameHeight = parseInt(mapFrame.css('height'));
        this.props.viewPortUpdate(frameWidth, frameHeight);
    }

    componentDidUpdate() {
        this.props.adjustViewPort(this.props.cursorX, this.props.cursorY);
    }

    componentDidMount() {
        this.handleViewPortUpdate();
        window.addEventListener('resize', this.handleViewPortUpdate);

    }

    componentWillUnmount() {
        window.removeEventListener('resize', this.handleViewPortUpdate);
    }

    render(){
        const {cursorActive, cursorX, cursorY} = this.props;

        let className = 'cursor cursor-x' + cursorX + ' cursor-y' + cursorY;
        if(cursorActive === true){
            className += ' cursor--active';
        }
        return <div className={className}>&nbsp;</div>;
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(Cursor);

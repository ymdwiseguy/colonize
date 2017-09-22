import React, {Component} from 'react';
import {connect} from 'react-redux'
import PropTypes from 'prop-types';
import {bindActionCreators} from 'redux'

import Unit from '../../EditorComponents/Units/Unit.jsx';
import Tile from './Tile.jsx';
import Cursor from './Cursor.jsx';
import * as actions from '../../Actions/Actions.jsx'


const mapGameStateToProps = (state) => ({
    worldMap: state.worldMap.mapData,
    viewPort: state.viewPort
});

const mapDispatchToProps = (dispatch) => ({
    keyPressAction: bindActionCreators(actions.cursorMove, dispatch),
    viewPortUpdate: bindActionCreators(actions.viewPortChangeCanvasSize, dispatch)
});


class WorldMap extends Component {

    constructor() {
        super();
        this.handleKeyPress = this.handleKeyPress.bind(this);
        this.handleViewPortUpdate = this.handleViewPortUpdate.bind(this);
    }

    handleKeyPress(event) {

        switch (event.keyCode) {
            case 37: // left
                this.props.keyPressAction('LEFT');
                break;
            case 38: // up
                this.props.keyPressAction('UP');
                break;
            case 39: // right
                this.props.keyPressAction('RIGHT');
                break;
            case 40: // down
                this.props.keyPressAction('DOWN');
                break;
        }

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

    componentDidMount() {
        this.handleViewPortUpdate();
        window.addEventListener('resize', this.handleViewPortUpdate);
        document.addEventListener('keydown', this.handleKeyPress);
    }

    componentWillUnmount() {
        window.removeEventListener('resize', this.handleViewPortUpdate);
        document.removeEventListener('keydown', this.handleKeyPress);
    }

    render() {
        const {worldMap, viewPort} = this.props;

        if (worldMap === null) {
            return <div className="map-main-wrapper">&nbsp;</div>;
        }

        let tileList;
        if (worldMap.tiles) {
            tileList = worldMap.tiles.map((tile, i) => {
                let key = 'tile_' + tile.xCoordinate + '_' + tile.yCoordinate;
                return <Tile tile={tile} key={key}/>
            });
        }

        let units = '';
        if (worldMap.units) {
            units = worldMap.units.map((unit, i) => {
                return <Unit unit={unit} key={i}/>
            });
        }

        let classname = 'map-main-wrapper map-main-wrapper--width-' + worldMap.width + ' ' +
            'map-main-wrapper--left-' + viewPort.mapOffsetX + ' ' +
            'map-main-wrapper--top-' + viewPort.mapOffsetY;
        return (
            <div className={classname}>
                {tileList}
                {units}
                <Cursor/>
            </div>
        )
    }
}

WorldMap.propTypes = {
    worldMap: PropTypes.object
};

export default connect(mapGameStateToProps, mapDispatchToProps)(WorldMap);

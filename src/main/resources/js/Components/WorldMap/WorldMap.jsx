import React, {Component} from 'react';
import {connect} from 'react-redux'
import PropTypes from 'prop-types';
import {bindActionCreators} from 'redux'

import Unit from './Unit.jsx';
import Tile from './Tile.jsx';
import Cursor from './Cursor.jsx';
import * as actions from '../../Actions/Actions.jsx'


const mapGameStateToProps = (state) => ({
    worldMap: state.worldMap,
    units: state.units
});

const mapDispatchToProps = (dispatch) => ({
    keyPressCursorMove: bindActionCreators(actions.cursorMove, dispatch),
    keyPressUnitMove: bindActionCreators(actions.unitMove, dispatch),
    viewPortUpdate: bindActionCreators(actions.viewPortChangeCanvasSize, dispatch)
});


class WorldMap extends Component {

    constructor() {
        super();
        this.handleKeyPress = this.handleKeyPress.bind(this);
        this.handleViewPortUpdate = this.handleViewPortUpdate.bind(this);
    }

    handleKeyPress(event) {
        let direction = '';
        switch (event.keyCode) {
            case 37:
                direction = 'LEFT';
                break;
            case 38:
                direction = 'UP';
                break;
            case 39:
                direction = 'RIGHT';
                break;
            case 40:
                direction = 'DOWN';
                break;
        }

        if(direction !== ''){
            if (this.props.worldMap.whoIsActive === 0) {
                this.props.keyPressCursorMove(direction);
            }else {
                this.props.keyPressUnitMove(direction, this.props.worldMap.whoIsActive);
            }

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
        const {worldMap, units} = this.props;

        if (worldMap.mapData === null) {
            return <div className="map-main-wrapper">&nbsp;</div>;
        }

        let tileList;
        if (worldMap.mapData.tiles) {
            tileList = worldMap.mapData.tiles.map((tile, i) => {
                let key = 'tile_' + tile.xCoordinate + '_' + tile.yCoordinate;
                return <Tile tile={tile} key={key}/>
            });
        }

        let renderUnits = '';
        if (worldMap.units) {
            renderUnits = worldMap.units.map((unit, i) => {
                return <Unit unit={unit} key={i}/>
            });
        }

        let classname = 'map-main-wrapper map-main-wrapper--width-' + worldMap.mapData.width + ' ' +
            'map-main-wrapper--left-' + worldMap.mapOffsetX + ' ' +
            'map-main-wrapper--top-' + worldMap.mapOffsetY;
        return (
            <div className={classname}>
                {tileList}
                {renderUnits}
                <Cursor/>
            </div>
        )
    }
}

WorldMap.propTypes = {
    worldMap: PropTypes.object
};

export default connect(mapGameStateToProps, mapDispatchToProps)(WorldMap);

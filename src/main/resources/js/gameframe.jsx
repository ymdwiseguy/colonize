import React from 'react';
import {render} from 'react-dom';

import Map from './worldMap/WorldMap.jsx';

class GameFrame extends React.Component {

    constructor() {
        super();
        this.state = {};
    }

    componentDidMount() {
        const url = "/api/maps/" + this.props.game.worldMap.worldMapId;
        this.updateState(url, 'GET');
        $(document.body).on('keydown', this.handleKeyDown.bind(this));
    }

    componentWillUnMount() {
        $(document.body).off('keydown', this.handleKeyDown);
    }

    updateState(url, method) {
        $.ajax({
            url: url,
            method: method,
            dataType: 'json',
            cache: false,
            success: function (restData) {
                this.setState({mapdata: restData});
                GameFrame.checkViewPort();
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(url, status, err.toString());
            }.bind(this)
        });
    }

    static checkViewPort() {
        let activeUnit = $('.unit--active');
        let mapFrame = $('.map-outer-wrapper');
        let mapInner = $('.map-main-wrapper');

        if (activeUnit.length == 1) {
            let unitWidth = parseInt(activeUnit.css('width'));
            let unitLeft = parseInt(activeUnit.css('left'));
            let frameWidth = parseInt(mapFrame.css('width'));
            let mapLeft = parseInt(mapInner.css('left'));
            let unitHeight = parseInt(activeUnit.css('height'));
            let unitTop = parseInt(activeUnit.css('top'));
            let frameHeight = parseInt(mapFrame.css('height'));
            let mapTop = parseInt(mapInner.css('top'));

            if (this.unitOutsideViewPort(frameWidth, frameHeight, mapLeft, mapTop, unitLeft, unitTop, unitWidth, unitHeight)) {
                mapLeft = frameWidth / 2 - unitLeft - unitWidth / 2;
                mapTop = frameHeight / 2 - unitTop - unitHeight / 2;
                mapInner.css({'left': mapLeft, 'top': mapTop});
            }
        }
    }

    static unitOutsideViewPort(frameWidth, frameHeight, mapLeft, mapTop, unitLeft, unitTop, unitWidth, unitHeight) {
        let rightOutside = ((frameWidth - (mapLeft + unitLeft)) <= (unitWidth * 2));
        let leftOuside = (unitLeft <= (unitWidth - mapLeft));
        let topOuside = (unitTop <= (unitHeight - mapTop));
        let bottomOuside = ((frameHeight - (mapTop + unitTop)) <= (unitHeight * 2));

        return (rightOutside || leftOuside || topOuside || bottomOuside);
    }

    handleKeyDown(event) {
        let url = '/api/maps/' + this.props.game.worldMap.worldMapId + '/move/';

        switch (event.keyCode) {
            case 37: // left
                this.updateState(url + 'left', 'PUT');
                break;
            case 38: // up
                this.updateState(url + 'up', 'PUT');
                break;
            case 39: // right
                this.updateState(url + 'right', 'PUT');
                break;
            case 40: // down
                this.updateState(url + 'down', 'PUT');
                break;
        }
    }

    render() {
        if (this.state.mapdata) {
            return (
                <div className="frame" onKeyDown={this.handleKeyDown}>
                    <div className="main-menu">
                        <ul>
                            <li>Load Map...</li>
                        </ul>
                    </div>
                    <div className="map-outer-wrapper">
                        <Map data={this.state.mapdata}/>
                    </div>
                    <div className="sidebar">
                        Sidebar
                    </div>
                </div>
            );
        } else {
            return null;
        }
    }
}

export default GameFrame;


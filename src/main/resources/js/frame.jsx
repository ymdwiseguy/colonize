import React from 'react';
import {render} from 'react-dom';

import Map from './map.jsx';

class Frame extends React.Component {

    constructor() {
        super();
        this.state = {};
    }

    componentDidMount() {
        const url = "/api/maps/" + this.props.mapdata.worldMapId;
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
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(url, status, err.toString());
            }.bind(this)
        });
    }

    handleKeyDown(event) {
        let url = '/api/maps/' + this.props.mapdata.worldMapId + '/move/';

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

export default Frame;


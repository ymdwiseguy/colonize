import React from 'react';
import {render} from 'react-dom';

import Map from './map.jsx';

class EditorFrame extends React.Component {

    constructor() {
        super();
        this.state = {};
    }

    componentDidMount() {
        $(document.body).on('click', this.handleClick.bind(this));
    }

    componentWillUnMount() {
        $(document.body).off('click', this.handleClick);
    }

    handleClick(event) {
        event.preventDefault();
        let url = event.target.href;
        if (url) {
            this.updateState(url);
        }
    }


    updateState(url) {
        console.log('get data from url ' + url);
        $.ajax({
            url: url,
            method: 'GET',
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


    render() {
        return (
            <div className="frame">
                <div className="main-menu">
                    <ul>
                        <li>
                            <a onClick={this.handleClick} href="/api/maps/largerMap">Load Map...</a></li>
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
    }
}

export default EditorFrame;


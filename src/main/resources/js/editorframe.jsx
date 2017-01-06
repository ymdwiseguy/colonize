import React from 'react';
import {render} from 'react-dom';

import Map from './map.jsx';
import GameMenu from './gamemenu.jsx';
import SaveGamePopup from './savegamepopup.jsx';
import SideBar from './sidebar.jsx';

class EditorFrame extends React.Component {

    constructor() {
        super();
        this.state = {};
        this.handleClickFrame = this.handleClickFrame.bind(this);
    }

    handleClickFrame(event) {
        event.preventDefault();
        let url = event.target.href;
        if (url) {
            this.updateState(url);
        }
    }

    componentDidMount() {
        this.updateState('/api/mapeditor/' + this.props.game.gameId);
    }

    updateState(url, method = 'GET') {
        console.log('get data from url ' + url + ' -- METHOD: ' + method);
        $.ajax({
            url: url,
            method: method,
            dataType: 'json',
            cache: false,
            success: function (restData) {
                this.setState({game: restData});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(url, status, err.toString());
            }.bind(this)
        });
    }

    render() {
        if (this.state.game) {

            let popup = null;
            if (this.state.game.popupMenu != null) {
                popup = <SaveGamePopup onClickFrame={this.handleClickFrame} game={this.state.game}/>;
            }

            return (
                <div className="frame">
                    <GameMenu menu={this.state.game.gameMenu} onClickFrame={this.handleClickFrame}/>
                    <div className="map-outer-wrapper">
                        <Map data={this.state.game.worldMap}/>
                    </div>
                    <SideBar sidebar={this.state.game.sideMenu} onClickFrame={this.handleClickFrame}/>
                    {popup}
                </div>
            );
        } else {
            return null;
        }
    }
}

export default EditorFrame;

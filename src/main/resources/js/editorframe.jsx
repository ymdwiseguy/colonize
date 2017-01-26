import React from 'react';
import {render} from 'react-dom';

import Map from './map.jsx';
import GameMenu from './gamemenu.jsx';
import LoadGamePopup from './loadgamepopup.jsx';
import SaveGamePopup from './savegamepopup.jsx';
import GenerateMapPopup from './generateMapPopup.jsx';

import SideBar from './sidebar.jsx';

class EditorFrame extends React.Component {

    constructor() {
        super();
        this.state = {};
    }

    handleClickFrame(event, method) {
        event.preventDefault();
        let url = event.target.href;
        if (url) {
            this.updateState(url, method);
        }
    }

    updateFormData(value, fieldName) {
        let jsonData = this.state.formData || {};
        jsonData[fieldName] = value;

        this.setState({formData: jsonData});
    }

    formSubmit(event) {
        event.preventDefault();
        let url = event.target.href;
        let formDataJson;
        if (this.state.formData) {
            formDataJson = JSON.stringify(this.state.formData);
        }
        if (url) {
            $.ajax({
                url: url,
                method: 'POST',
                dataType: 'json',
                contentType: 'application/json; charset=UTF-8',
                data: formDataJson,
                cache: false,
                success: function (restData) {
                    this.setState({game: restData});
                }.bind(this),
                error: function (xhr, status, err) {
                    console.error(url, status, err.toString());
                }.bind(this)
            });
        }else {
            console.log('no url defined');
        }
    }

    componentDidMount() {
        this.updateState('/api/mapeditor/' + this.props.game.gameId);
    }

    updateState(url, method = 'GET') {
        let gameJson = '{}';
        if (this.state.game) {
            gameJson = JSON.stringify(this.state.game);
        }
        $.ajax({
            url: url,
            method: method,
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: (method == 'GET' ? '' : gameJson),
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
                switch (this.state.game.popupMenu.type) {
                    case 'GENERATE_MAP' :
                        popup = <GenerateMapPopup formSubmit={(e) => this.formSubmit(e)}
                                                  onChangeFrame={(v, f) => this.updateFormData(v, f)}
                                                  game={this.state.game}
                        />;
                        break;
                    case 'SAVE_MAPEDITOR':
                        popup = <SaveGamePopup onClickFrame={(e, m) => this.handleClickFrame(e, m)}
                                               game={this.state.game}/>;
                        break;
                    case 'SHOW_MAPLIST' :
                        popup = <LoadGamePopup onClickFrame={(e, m) => this.handleClickFrame(e, m)}
                                               game={this.state.game}/>;
                        break;
                }
            }

            return (
                <div className="frame">
                    <GameMenu menu={this.state.game.gameMenu} onClickFrame={(e, m) => this.handleClickFrame(e, m)}/>
                    <div className="map-outer-wrapper">
                        <Map data={this.state.game.worldMap}/>
                    </div>
                    <SideBar sidebar={this.state.game.sideMenu} onClickFrame={(e, m) => this.handleClickFrame(e, m)}/>
                    {popup}
                </div>
            );
        } else {
            return null;
        }
    }
}

export default EditorFrame;


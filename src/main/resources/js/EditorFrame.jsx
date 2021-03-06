import React from 'react';

import WorldMap from './EditorComponents/WorldMap/WorldMap.jsx';

import GameMenu from './EditorComponents/Menus/GameMenu/GameMenu.jsx';

import MapEditorSidebar from './EditorComponents/Menus/SideMenu/MapEditorSidebar.jsx';
import SideMenuSelectTiles from './EditorComponents/Menus/sideMenu/SideMenuSelectTiles.jsx';

import LoadGamePopup from './EditorComponents/Menus/Popups/LoadGamePopup.jsx';
import SaveGamePopup from './EditorComponents/Menus/Popups/SaveGamePopup.jsx';
import GenerateMapPopup from './EditorComponents/Menus/Popups/GenerateMapPopup.jsx';

class EditorFrame extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
        this.handleClickFrame = this.handleClickFrame.bind(this);
        this.updateFormData = this.updateFormData.bind(this);
    }


    static checkViewPort() {
        let cursor = $('.cursor');
        let mapFrame = $('.map-outer-wrapper');
        let mapInner = $('.map-main-wrapper');

        if (cursor.length === 1) {
            let cursorWidth = parseInt(cursor.css('width'));
            let cursorLeft = parseInt(cursor.css('left'));
            let frameWidth = parseInt(mapFrame.css('width'));
            let mapLeft = parseInt(mapInner.css('left'));
            const mapWidth = parseInt(mapInner.css('width'));
            let cursorHeight = parseInt(cursor.css('height'));
            let cursorTop = parseInt(cursor.css('top'));
            let frameHeight = parseInt(mapFrame.css('height'));
            let mapTop = parseInt(mapInner.css('top'));

            if (this.unitOutsideViewPort(frameWidth, frameHeight, mapLeft, mapTop, cursorLeft, cursorTop, cursorWidth, cursorHeight)) {
                mapLeft = frameWidth / 2 - cursorLeft - cursorWidth / 2;
                mapLeft = this.limitHorizontally(mapLeft, mapWidth, frameWidth);
                mapTop = frameHeight / 2 - cursorTop - cursorHeight / 2;
                mapTop = this.limitVertically(mapTop, frameHeight);
                mapInner.css({'left': mapLeft, 'top': mapTop});
            }
        }
    }

    static limitHorizontally(mapLeft, mapWidth, frameWidth) {
        if (mapLeft > 0) {
            mapLeft = 0;
        }
        let limitRight = (mapWidth + mapLeft);
        if (limitRight < frameWidth) {
            mapLeft = -1 * (mapWidth - frameWidth);
        }
        return mapLeft;
    }

    static limitVertically(mapTop, mapHeight, frameHeight) {
        if (mapTop > 0) {
            mapTop = 0;
        }
        let limitBottom = (mapHeight + mapTop);
        if (limitBottom < frameHeight) {
            mapLeft = -1 * (mapHeight - frameHeight);
        }
        return mapTop;
    }

    static unitOutsideViewPort(frameWidth, frameHeight, mapLeft, mapTop, cursorLeft, cursorTop, cursorWidth, cursorHeight) {

        const maxPaddingHorizontal = 7 * cursorWidth;
        let paddingHorizontal = 2 * cursorWidth;
        while ((frameWidth / 2 - cursorWidth) > paddingHorizontal && paddingHorizontal < maxPaddingHorizontal) {
            paddingHorizontal += cursorWidth;
        }

        const maxPaddingVertical = 7 * cursorWidth;
        let paddingVertical = cursorHeight * 2;
        while ((frameHeight / 2 - cursorHeight) > paddingVertical && paddingVertical < maxPaddingVertical) {
            paddingVertical += cursorWidth;
        }

        const rightOutside = ((frameWidth - (mapLeft + cursorLeft)) < (paddingHorizontal));
        const leftOuside = (cursorLeft <= (paddingHorizontal - mapLeft));
        const topOuside = (cursorTop <= (paddingVertical - mapTop));
        const bottomOuside = ((frameHeight - (mapTop + cursorTop)) < (paddingVertical));

        return (rightOutside || leftOuside || topOuside || bottomOuside);
    }

    handleKeyDown(event) {

        let putTileUrl = '/api/mapeditor/' + this.props.game.gameId + '/movecursor/';
        let putAssetUrl = '/api/mapeditor/' + this.props.game.gameId + '/toggleasset/';

        switch (event.keyCode) {
            case 32: // space
                this.updateState('/api/mapeditor/' + this.props.game.gameId + '/activetile', 'PUT');
                break;
            case 37: // left
                this.updateState(putTileUrl + 'LEFT', 'PUT');
                break;
            case 38: // up
                this.updateState(putTileUrl + 'UP', 'PUT');
                break;
            case 39: // right
                this.updateState(putTileUrl + 'RIGHT', 'PUT');
                break;
            case 40: // down
                this.updateState(putTileUrl + 'DOWN', 'PUT');
                break;

            case 70: // f
                this.updateState(putAssetUrl + 'FOREST', 'PUT');
                break;
            case 72: // h
                this.updateState(putAssetUrl + 'HILL', 'PUT');
                break;
            case 82: // r
                this.updateState(putAssetUrl + 'RIVER', 'PUT');
                break;
        }
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
                    EditorFrame.checkViewPort();
                }.bind(this),
                error: function (xhr, status, err) {
                    console.error(url, status, err.toString());
                }.bind(this)
            });
        } else {
            console.log('no url defined');
        }
    }

    componentDidMount() {
        this.updateState('/api/mapeditor/' + this.props.game.gameId);
        $(document.body).on('keydown', this.handleKeyDown.bind(this));
    }

    componentWillUnMount() {
        $(document.body).off('keydown', this.handleKeyDown);
    }

    updateState(url, method = 'GET', gameJson = '{}') {
        if (this.state.game) {
            gameJson = JSON.stringify(this.state.game);
        }
        $.ajax({
            url: url,
            method: method,
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            data: (method === 'GET' ? '' : gameJson),
            cache: false,
            success: function (restData) {
                this.setState({game: restData});
                EditorFrame.checkViewPort();
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(url, status, err.toString());
            }.bind(this)
        });
    }

    render() {
        if (this.state.game) {

            let popup = null;
            if (this.state.game.popupMenu !== null) {
                switch (this.state.game.popupMenu.type) {
                    case 'GENERATE_MAP' :
                        popup = <GenerateMapPopup formSubmit={(e) => this.formSubmit(e)}
                                                  onChangeFrame={this.updateFormData}
                                                  game={this.state.game}/>;
                        break;
                    case 'SAVE_MAPEDITOR':
                        popup = <SaveGamePopup onClickFrame={this.handleClickFrame}
                                               game={this.state.game}/>;
                        break;
                    case 'SHOW_MAPLIST' :
                        popup = <LoadGamePopup onClickFrame={this.handleClickFrame}
                                               popupMenu={this.state.game.popupMenu}/>;
                        break;
                }
            }

            let sidebar = null;
            if (this.state.game.sideMenu !== null) {
                switch (this.state.game.sideMenu.type) {
                    case 'EDITOR_SELECT_TILES':
                        sidebar = <SideMenuSelectTiles sidebar={this.state.game.sideMenu}
                                                       selected={this.state.game.selectedTileType}
                                                       onClickFrame={this.handleClickFrame}/>;
                        break;
                    default:
                        sidebar = <MapEditorSidebar sidebar={this.state.game.sideMenu}
                                                    onClickFrame={this.handleClickFrame}/>;
                        break;

                }
            }

            return (
                <div className="frame" onKeyDown={this.handleKeyDown}>
                    <GameMenu menu={this.state.game.gameMenu} onClickFrame={this.handleClickFrame}/>
                    <div className="map-outer-wrapper">
                        <WorldMap data={this.state.game.worldMap} cursor={this.state.game.cursor}/>
                    </div>
                    {sidebar}
                    {popup}
                </div>
            );
        } else {
            return null;
        }
    }
}

export default EditorFrame;


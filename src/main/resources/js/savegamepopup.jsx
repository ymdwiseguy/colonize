import React from 'react';
import {render} from 'react-dom';

import MenuEntry from './menuentry.jsx';

class SaveGamePopup extends React.Component {

    constructor(props) {
        super(props);
        this.handlePopupClick = this.handlePopupClick.bind(this);
    }

    handlePopupClick(event) {
        this.props.onClickFrame(event);
    }

    render() {
        let entry1 = {
           'endpointUrl' : '/api/mapeditor/' + this.props.game.gameId,
           'entryName' : 'Abort'
        };
        console.log(this.props);
        if(this.props.game.worldMap){
            let mapName = this.props.game.worldMap.worldMapName;
            return (
                <div className="popup">
                    <h2>Overwrite map &quot;{mapName}&quot;</h2>
                    <MenuEntry entry={entry1} key="example" onClickSubmenu={this.handlePopupClick}/>
                </div>
            )
        }else {
            return null;
        }

    }
}

export default SaveGamePopup;

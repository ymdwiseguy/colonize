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
        console.log(this.props);
        if (this.props.game.popupMenu.menuEntries.length > 0 && this.props.game.worldMap.worldMapName) {
            let menuentries = this.props.game.popupMenu.menuEntries.map((entry, i) => {
                return <MenuEntry entry={entry} key={i} onClickSubmenu={this.handlePopupClick}/>
            });

            let mapName = this.props.game.worldMap.worldMapName;
            return (
                <div className="popup">
                    <h2>Overwrite map &quot;{mapName}&quot;</h2>
                    {menuentries}
                </div>
            )
        } else {
            return null;
        }

    }
}

export default SaveGamePopup;

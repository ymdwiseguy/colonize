import React from 'react';
import {render} from 'react-dom';

import MenuEntry from './MenuEntry/MenuEntry.jsx';

class SaveGamePopup extends React.Component {

    constructor(props) {
        super(props);
    }

    handlePopupClick(event, method) {
        this.props.onClickFrame(event, method);
    }

    render() {
        if (this.props.game.popupMenu.entries.length > 0 && this.props.game.worldMap.worldMapName) {
            let menuentries = this.props.game.popupMenu.entries.map((entry, i) => {
                return <MenuEntry entry={entry} key={i} onClickSubmenu={(e, m) => this.handlePopupClick(e, m)}/>
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

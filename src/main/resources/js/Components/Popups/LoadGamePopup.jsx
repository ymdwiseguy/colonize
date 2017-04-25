import React from 'react';

import MenuEntry from '../MenuEntry/MenuEntry.jsx';


class LoadGamePopup extends React.Component {
    constructor(props) {
        super(props);
    }

    handlePopupClick(event, method) {
        this.props.onClickFrame(event, method);
    }

    render() {
        if (this.props.game.popupMenu.entries.length > 0) {
            let menuentries = this.props.game.popupMenu.entries.map((entry, i) => {
                return <MenuEntry
                    entry={entry}
                    key={i}
                    onClickSubmenu={
                        (e, m) => this.handlePopupClick(e, entry.method)
                    }/>
            });

            return (
                <div className="popup loadGamePopup">
                    <h2>{this.props.game.popupMenu.header}</h2>
                    {menuentries}
                </div>
            )
        } else {
            return null;
        }
    }
}

export default LoadGamePopup;

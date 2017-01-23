import React from 'react';
import {render} from 'react-dom';

import MenuEntry from './menuentry.jsx';

class GenerateMapPopup extends React.Component {

    constructor(props) {
        super(props);
    }

    handlePopupClick(event, method) {
        this.props.onClickFrame(event, method);
    }

    render() {
        console.log(this.props);
        let menuentries = null;

        return (
            <div className="popup generateMapPopup">
                <h2>{this.props.game.popupMenu.header}</h2>
                {menuentries}
            </div>
        )

    }
}

export default GenerateMapPopup;

import React from 'react';
import {render} from 'react-dom';

import MenuEntry from './menuentry.jsx';

class GenerateMapPopup extends React.Component {

    constructor(props) {
        super(props);
    }

    handlePopupClick(event, method) {
        this.props.formSubmit(event);
    }

    handleInputChange(e, fieldName) {
        this.props.onChangeFrame(e.target.value, fieldName);
    }

    render() {
        let inputs = this.props.game.popupMenu.inputs.map((input, i) => {
            let className = 'inputblock inputblock_' + input.name;
            const value = input.value;
            return (
                <div className={className}>
                    <label>{input.title}</label>
                    <input name={input.name} value={value} onChange={(e, fieldName) => this.handleInputChange(e, input.name)}/>
                </div>
            )
        });

        return (
            <div className="popup generateMapPopup">
                <h2>{this.props.game.popupMenu.header}</h2>
                {inputs}
                <MenuEntry entry={this.props.game.popupMenu.submitButton}
                           onClickSubmenu={(e, m) => this.handlePopupClick(e, m)}/>
            </div>
        )

    }
}

export default GenerateMapPopup;

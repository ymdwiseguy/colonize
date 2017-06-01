import React from 'react';
import AnchorLink from '../../BaseComponents/AnchorLink.jsx';

class GenerateMapPopup extends React.Component {

    constructor(props) {
        super(props);
        this.handlePopupClick = this.handlePopupClick.bind(this);
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
            return (
                <div className={className}>
                    <label>{input.title}</label>
                    <input name={input.name}
                           onChange={(e, fieldName) => this.handleInputChange(e, input.name)}/>
                </div>
            )
        });

        const entry = this.props.game.popupMenu.submitButton;
        return (

            <div className="popup generateMapPopup">
                <h2>{this.props.game.popupMenu.header}</h2>
                {inputs}
                <AnchorLink
                    href={entry.endpointUrl}
                    method={entry.method}
                    onClickFunction={this.handlePopupClick}>{entry.entryName}
                </AnchorLink>
            </div>
        )

    }
}

export default GenerateMapPopup;

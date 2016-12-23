import React from 'react';
import {render} from 'react-dom';

class MenuEntry extends React.Component {

    constructor(props) {
        super(props);
        this.handleClick = this.handleClick.bind(this);
    }

    handleClick(event) {
        this.props.onClickSubmenu(event);
    }

    render() {
        return (
            <li>
                <a href={this.props.entry.endpointUrl} onClick={this.handleClick}>{this.props.entry.entryName}</a>
            </li>
        );
    }

}

export default MenuEntry;

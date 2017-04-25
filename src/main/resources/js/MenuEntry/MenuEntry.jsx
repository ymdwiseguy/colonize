import React from 'react';
import {render} from 'react-dom';

class MenuEntry extends React.Component {

    constructor(props) {
        super(props);
    }

    handleClick(event, method) {
        this.props.onClickSubmenu(event, method);
    }

    render() {
        return (
            <li>
                <a href={this.props.entry.endpointUrl} method={this.props.entry.method} onClick={(e, m) => this.handleClick(e, this.props.entry.method)}>{this.props.entry.entryName}</a>
            </li>
        );

    }

}

export default MenuEntry;

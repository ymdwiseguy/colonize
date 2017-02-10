import React from 'react';
import {render} from 'react-dom';

class SelectableTiles extends React.Component {

    constructor(props) {
        super(props);
    }

    handleClick(event, method) {
        this.props.onClickSubmenu(event, method);
    }

    render() {
        let isActive = (this.props.selected == this.props.entry.entryName);
        let className = "tile map-tile__" + this.props.entry.entryName + (isActive ? ' active' : '');
        return (
            <li className={className}>
                <a
                    href={this.props.entry.endpointUrl}
                    method={this.props.entry.method}
                    onClick={(e, m) => this.handleClick(e, this.props.entry.method)}>
                    {this.props.entry.entryName}
                </a>
            </li>
        );

    }

}

export default SelectableTiles;

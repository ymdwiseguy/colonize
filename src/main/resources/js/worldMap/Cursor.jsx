import React from 'react';
import {render} from 'react-dom';

class Cursor extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        let className = 'cursor cursor-x' + this.props.cursorX + ' cursor-y' + this.props.cursorY;
        return (
            <div className={className}>
                &nbsp;
            </div>
        );

    }

}

export default Cursor;

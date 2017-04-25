import React from 'react';

class Cursor extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        let className = 'cursor cursor-x' + this.props.cursorX + ' cursor-y' + this.props.cursorY;
        if(this.props.cursorActive === true){
            className += ' cursor--active';
        }
        return (
            <div className={className}>
                &nbsp;
            </div>
        );

    }

}

export default Cursor;

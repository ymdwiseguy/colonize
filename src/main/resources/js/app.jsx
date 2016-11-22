import React from 'react';
import {render} from 'react-dom';

import Map from './map.jsx';

class Frame extends React.Component {
    render() {
        return (
            <div className="frame">
                <Map data={this.props.mapdata}/>
            </div>
        );
    }
}

render(<Frame mapdata={mapdata}/>, document.getElementById('col-body'));


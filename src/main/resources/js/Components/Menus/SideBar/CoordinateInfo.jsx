import React from 'react';

export const CoordinateInfo = ({x,y}) => {

    return (
        <div className="coordinate-info">
            <p>
                <span>Coordinates:</span><br/>
                <span>X: {x} / </span><span>Y: {y} </span>
            </p>
        </div>
    );
};


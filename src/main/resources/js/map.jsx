import React from 'react';
import Unit from './unit.jsx';
import Tile from './tile.jsx';



class Map extends React.Component{
    render () {
        if(this.props.data){
            let units = '';
            let tileList = this.props.data.tiles.map((tile, i) => {
                let key = 'tile_' + tile.xCoordinate + '_' + tile.yCoordinate;
                return <Tile tile={tile} key={key}/>
            });
            if (this.props.data.units) {
                units = this.props.data.units.map((unit, i) => {
                    return <Unit unit={unit} key={i}/>
                });
            }
            let classname = 'map-main-wrapper map-main-wrapper--width-'+this.props.data.width;
            return (
                <div className={classname}>
                    {tileList}
                    {units}
                </div>
            )
        }else{
            return null;
        }

    }
}

export default Map;

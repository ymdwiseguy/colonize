const Tile = React.createClass({
    render: function () {
        let className = "map-tile map-tile__" + this.props.tile.type;
        let units = '';
        if (this.props.tile.units) {
            units = this.props.tile.units.map((unit, i) => {
                return <Unit unit={unit} key={i}/>
            });
        }

        return (
            <div className={className}>
                {units}
            </div>
        )
    }
});

const Map = React.createClass({
    render: function () {
        let tileList = this.props.data.tiles.map((tile, i) => {
            let key = 'tile_' + tile.x_coordinate + '_' + tile.y_coordinate;
            return <Tile tile={tile} key={key}/>
        });
        let classname = 'map-main-wrapper map-main-wrapper--width-'+this.props.data.width;
        return (
            <div className={classname}>
                {tileList}
            </div>
        )
    }
});


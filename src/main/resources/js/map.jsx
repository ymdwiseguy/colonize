const Row = React.createClass({
    render: function () {
        let tileList = this.props.rowdata.tiles.map((tile, i) => {
            return <Tile tile={tile} key={i}/>
        });
        return (
            <div className="map-row">
                {tileList}
            </div>
        )
    }
});

const Tile = React.createClass({
    render: function () {
        let className = "map-row-tile map-row-tile__"+this.props.tile.type;
        let units = '';
        if(this.props.tile.units){
            units = this.props.tile.units.map((unit, i) => {
                return <Unit unit={unit} key={i} />
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
        let rowList = this.props.data.rows.map((row, i) => {
            return <Row rowdata={row} key={i}/>
        });
        return (
            <div className="map-main-wrapper">
                {rowList}
            </div>
        )
    }
});


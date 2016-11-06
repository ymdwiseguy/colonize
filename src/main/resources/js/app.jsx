


const Frame = React.createClass({
    render: function () {
        return (
            <div className="frame">
                <Map data={this.props.mapdata}/>
            </div>
        )
    }
});

ReactDOM.render(
    <Frame mapdata={mapdata}/>,
    document.getElementById('col-body')
);

import React from 'react';
import {render} from 'react-dom';
import {createStore, combineReducers} from 'redux'
import {Provider, connect} from 'react-redux'

import * as reducers from './Reducers/Reducers.jsx'
import ChooseFactionPopup from './Components/Menus/Popups/ChooseFactionPopup.jsx'
import InitialPopup from './Components/Menus/Popups/InitialPopup.jsx'

const mapGameStateToProps = (state) => ({
    screen: state.screen,
    faction: state.faction
});

const Game = () => {
    return <div>Render game</div>
};

const MainFrameComponent = ({screen, faction}) => {
    let content = null;
    switch (screen) {
        case 'CHOOSE_FACTION':
            content = <ChooseFactionPopup />;
            break;
        case 'GAME':
            content = <Game />;
            break;
        default:
            content = <InitialPopup />;
    }
    return (
        <div>
            {content}
        </div>
    );
};

const ConnectedGame = connect(mapGameStateToProps, null)(MainFrameComponent);


MainFrameComponent.propTypes = {
    // data: PropTypes.object
};


const store = createStore(
    combineReducers({
        ...reducers
    })
);


render(
    <Provider store={store}>
        <ConnectedGame />
    </Provider>, document.getElementById('col-body')
);

import 'babel-polyfill'
import React from 'react';
import {render} from 'react-dom';
import {createStore, combineReducers, applyMiddleware} from 'redux'
import {Provider, connect} from 'react-redux'
import thunk from 'redux-thunk'

import * as reducers from './Reducers/Reducers.jsx'
import ChooseFactionPopup from './Components/Menus/Implementations/ChooseFactionPopup.jsx'
import InitialPopup from './Components/Menus/Implementations/InitialPopup.jsx'
import GameFrame from './Components/MainFrames/GameFrame.jsx'


const mapMainFrameStateToProps = (state) => ({
    screen: state.screen
});

const MainFrameComponent = ({screen}) => {
    let content = null;
    switch (screen) {
        case 'CHOOSE_FACTION':
            content = <ChooseFactionPopup />;
            break;
        case 'GAME':
            content = <GameFrame />;
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

const MainFrame = connect(mapMainFrameStateToProps, null)(MainFrameComponent);


const store = createStore(
    combineReducers({
        ...reducers
    }),
    applyMiddleware(thunk)
);


render(
    <Provider store={store}>
        <MainFrame />
    </Provider>, document.getElementById('col-body')
);

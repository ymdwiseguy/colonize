import 'babel-polyfill'
import React, {Component} from 'react';
import {render} from 'react-dom';
import {createStore, combineReducers, applyMiddleware, bindActionCreators} from 'redux'
import {Provider, connect} from 'react-redux'
import thunk from 'redux-thunk'

import * as reducers from './Reducers/Reducers.jsx'
import * as actions from './Actions/Actions.jsx'

import ChooseFactionPopup from './Components/Menus/Implementations/ChooseFactionPopup.jsx'
import InitialPopup from './Components/Menus/Implementations/InitialPopup.jsx'
import GameFrame from './Components/MainFrames/GameFrame.jsx'


const mapMainFrameStateToProps = (state) => ({
    screen: state.screen
});

const mapDispatchToProps = (dispatch) => ({
    keyPressAction: bindActionCreators(actions.cursorMove, dispatch)
});

class MainFrameComponent extends Component {
    constructor(){
        super();
        this.handleKeyPress = this.handleKeyPress.bind(this);
    }

    handleKeyPress(event) {

        switch (event.keyCode) {
            case 37: // left
                this.props.keyPressAction('LEFT');
                break;
            case 38: // up
                this.props.keyPressAction('UP');
                break;
            case 39: // right
                this.props.keyPressAction('RIGHT');
                break;
            case 40: // down
                this.props.keyPressAction('DOWN');
                break;
        }

    }

    componentDidMount() {
        document.addEventListener('keydown', this.handleKeyPress);

    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.handleKeyPress);
    }

    render() {
        let content = null;
        const {screen} = this.props;

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
            <div className="col-body">
                {content}
            </div>
        );
    }
}

const MainFrame = connect(mapMainFrameStateToProps, mapDispatchToProps)(MainFrameComponent);

const combinedReducers = combineReducers({...reducers});
const middleWare = applyMiddleware(thunk);

const store = (
    (undefined === window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__) ?
    createStore(combinedReducers, middleWare) :
    createStore(
        combinedReducers,
        window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__(middleWare)
    )
);

render(
    <Provider store={store}>
        <MainFrame />
    </Provider>, document.getElementById('col-body')
);

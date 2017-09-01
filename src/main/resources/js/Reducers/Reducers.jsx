import {
    GOTO_PAGE,
    CHOOSE_FACTION,
    REQUEST_WORLD_MAP,
    RECEIVE_WORLD_MAP,
    INVALIDATE_WORLD_MAP,
    CURSOR_MOVE
} from '../ActionTypes/ActionTypes.jsx'

export function screen(state = "START", action) {
    switch (action.type) {
        case GOTO_PAGE:
            return action.text;
        case CHOOSE_FACTION:
            return 'GAME';
        default:
            return state
    }
}

const factions = ['Netherlands', 'France', 'Spain', 'England'];

export function faction(state = null, action) {
    switch (action.type) {
        case CHOOSE_FACTION:
            if (factions.indexOf(action.text) !== 0) {
                return action.text;
            } else {
                return state;
            }

        default:
            return state;
    }
}

export function cursor(state = {
    cursorActive: true,
    cursorX: 1,
    cursorY: 1
}, action) {
    console.log(action);
    switch (action.type) {
        case CURSOR_MOVE:
            if (action.direction === 'RIGHT') {
                return {...state, cursorX: state.cursorX + 1}
            }
            if (action.direction === 'LEFT') {
                return {...state, cursorX: state.cursorX - 1}
            }
            if (action.direction === 'UP') {
                return {...state, cursorY: state.cursorY - 1}
            }
            if (action.direction === 'DOWN') {
                return {...state, cursorY: state.cursorY + 1}
            }
            return state;
        default:
            return state;
    }
}

export function worldMap(state = {
    isFetching: false,
    didInvalidate: false,
    mapData: {}
}, action) {

    switch (action.type) {
        case INVALIDATE_WORLD_MAP:
            return Object.assign({}, state, {
                didInvalidate: true
            });
        case REQUEST_WORLD_MAP:
            return Object.assign({}, state, {
                isFetching: true,
                didInvalidate: false
            });
        case RECEIVE_WORLD_MAP:
            return Object.assign({}, state, {
                isFetching: false,
                didInvalidate: false,
                mapData: action.mapData,
                lastUpdated: action.receivedAt
            });
        default:
            return state;
    }
}

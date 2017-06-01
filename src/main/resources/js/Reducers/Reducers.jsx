import * as actionTypes from '../ActionTypes/ActionTypes.jsx'

export function screen(state = "START", action) {
    switch (action.type) {
        case actionTypes.GOTO_PAGE:
            return action.text;
        case actionTypes.CHOOSE_FACTION:
            return 'GAME';
        default:
            return state
    }
}


const factions = ['Netherlands', 'France', 'Spain', 'England'];

export function faction(state = null, action) {
    switch (action.type) {
        case actionTypes.CHOOSE_FACTION:
            if (factions.indexOf(action.text) !== 0) {
                return action.text;
            } else {
                return state;
            }

        default:
            return state;
    }
}

import {
    CHOOSE_FACTION,
    CURSOR_GOTO,
    GOTO_PAGE,
    INVALIDATE_WORLD_MAP,
    RECEIVE_WORLD_MAP,
    REQUEST_WORLD_MAP,
    UNIT_CLICKED
} from '../ActionTypes/ActionTypes.jsx';

export function didInvalidate(state = false, action) {
    switch (action.type) {
        case INVALIDATE_WORLD_MAP:
            return true;

        case REQUEST_WORLD_MAP:
            return false;

        case RECEIVE_WORLD_MAP:
            return false;

        default:
            return state;
    }
}

export function isFetching(state = false, action) {
    switch (action.type) {
        case REQUEST_WORLD_MAP:
            return true;

        case RECEIVE_WORLD_MAP:
            return false;

        default:
            return state;
    }
}

export function whoIsActive(state = 1, action) {
    switch (action.type) {
        case CURSOR_GOTO:
            return 0;

        case UNIT_CLICKED:
            return action.unitId;

        default:
            return state;
    }
}

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

import {
    CURSOR_GOTO,
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

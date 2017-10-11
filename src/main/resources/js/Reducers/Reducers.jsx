import {
    GOTO_PAGE,
    CHOOSE_FACTION,
    CURSOR_GOTO,
    REQUEST_WORLD_MAP,
    RECEIVE_WORLD_MAP,
    INVALIDATE_WORLD_MAP,
    UNIT_CLICKED
} from '../ActionTypes/ActionTypes.jsx';

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

export function worldMap(state = {
    isFetching: false,
    didInvalidate: false,
    mapData: {},
    units: unitsInitalState(),
    whoIsActive: 0 // 0 == cursor, number x == unit with id x
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
        case CURSOR_GOTO:
            return {
                ...state,
                whoIsActive: 0,
                units: state.units.map((unit) => {
                    if (unit.active) {
                        return {
                            ...unit,
                            active: false
                        }
                    }
                    return unit
                })
            };
        case UNIT_CLICKED:
            return {
                ...state,
                whoIsActive: action.unitId,
                units: state.units.map((unit) => {
                    if (unit.unitId === action.unitId) {
                        return {
                            ...unit,
                            active: true
                        }
                    }
                    return unit
                })
            };
        default:
            return state;
    }
}

function unitsInitalState() {
    return [
        {
            "unitType": "KARAVELLE",
            "unitId": 1,
            "active": false,
            "xPosition": 48,
            "yPosition": 33
        }
    ]
}

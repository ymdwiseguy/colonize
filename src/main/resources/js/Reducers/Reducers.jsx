import {
    GOTO_PAGE,
    CHOOSE_FACTION,
    CURSOR_GOTO,
    REQUEST_WORLD_MAP,
    RECEIVE_WORLD_MAP,
    INVALIDATE_WORLD_MAP,
    UNIT_CLICKED,
    UNIT_MOVE
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
                    } else {
                        return {
                            ...unit,
                            active: false
                        }
                    }
                })
            };
        case UNIT_MOVE:

            const mapWidth = state.mapData.width || 0;
            const mapHeight = state.mapData.height || 0;
            return {
                ...state,
                units: state.units.map((unit) => {
                    if (unit.unitId === action.unitId) {
                        let newUnitGotoX = unit.xPosition;
                        let newUnitGotoY = unit.yPosition;

                        switch (action.direction) {
                            case 'LEFT':
                                if (newUnitGotoX > 1) {
                                    newUnitGotoX = newUnitGotoX - 1;
                                }
                                break;
                            case 'RIGHT':
                                if (newUnitGotoX < mapWidth) {
                                    newUnitGotoX = newUnitGotoX + 1;
                                }
                                break;
                            case 'UP':
                                if (newUnitGotoY > 1) {
                                    newUnitGotoY = newUnitGotoY - 1;
                                }
                                break;
                            case 'DOWN':
                                if (newUnitGotoY < mapHeight) {
                                    newUnitGotoY = newUnitGotoY + 1;
                                }
                                break;
                        }
                        return {
                            ...unit,
                            xPosition: newUnitGotoX,
                            yPosition: newUnitGotoY
                        }
                    }
                    return unit;
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
        },
        {
            "unitType": "KARAVELLE",
            "unitId": 2,
            "active": false,
            "xPosition": 49,
            "yPosition": 33
        }
    ]
}

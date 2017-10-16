import {
    CHOOSE_FACTION,
    CURSOR_GOTO,
    CURSOR_MOVE,
    GOTO_PAGE,
    INVALIDATE_WORLD_MAP,
    RECEIVE_WORLD_MAP,
    REQUEST_WORLD_MAP,
    UNIT_CLICKED,
    UNIT_MOVE,
    VIEWPORT_SET_CANVAS_SIZE
} from '../ActionTypes/ActionTypes.jsx';
import {getMapOffset, outsideViewPort} from '../GameLogic/ViewPortCalc.jsx'
import {moveUnit} from '../GameLogic/UnitMovement.jsx';

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
    cursor: {
        cursorActive: false,
        cursorX: 1,
        cursorY: 1
    },
    didInvalidate: false,
    isFetching: false,
    mapData: {},
    mapOffsetX: 0,
    mapOffsetY: 0,
    units: unitsInitalState(),
    viewPort: {
        canvasWidth: 0,
        canvasHeight: 0
    },
    whoIsActive: 1 // 0 == cursor, number x == unit with id x
}, action) {

    let newCursorGotoX;
    let newCursorGotoY;
    let mapOffsetsGoto;

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

            newCursorGotoX = 48;
            newCursorGotoY = 33;

            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);
            return {
                ...state,
                isFetching: false,
                didInvalidate: false,
                mapData: action.mapData,
                lastUpdated: action.receivedAt,
                cursor: {
                    ...state.cursor,
                    cursorX: newCursorGotoX,
                    cursorY: newCursorGotoY
                },
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset']
            };

        case CURSOR_GOTO:
            newCursorGotoX = action.xPosition;
            newCursorGotoY = action.yPosition;
            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);

            return {
                ...state,
                cursor: {
                    cursorX: newCursorGotoX,
                    cursorY: newCursorGotoY,
                    cursorActive: true
                },
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset'],
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
            newCursorGotoX = action.xPosition;
            newCursorGotoY = action.yPosition;
            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);

            return {
                ...state,
                cursor: {
                    cursorX: newCursorGotoX,
                    cursorY: newCursorGotoY,
                    cursorActive: false
                },
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset'],
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
            newCursorGotoX = state.cursor.cursorX;
            newCursorGotoY = state.cursor.cursorY;

            switch (action.direction) {
                case 'LEFT':
                    if (newCursorGotoX > 1) {
                        newCursorGotoX = newCursorGotoX - 1;
                    }
                    break;
                case 'RIGHT':
                    if (newCursorGotoX < state.mapData.width) {
                        newCursorGotoX = newCursorGotoX + 1;
                    }
                    break;
                case 'UP':
                    if (newCursorGotoY > 1) {
                        newCursorGotoY = newCursorGotoY - 1;
                    }
                    break;
                case 'DOWN':
                    if (newCursorGotoY < state.mapData.height) {
                        newCursorGotoY = newCursorGotoY + 1;
                    }
                    break;
            }

            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);
            return {
                ...state,
                cursor: {
                    ...state.cursor,
                    cursorX: newCursorGotoX,
                    cursorY: newCursorGotoY
                },
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset'],
                units: moveUnit(action.unitId, state.units, state.mapData, action.direction)
            };

        case CURSOR_MOVE:
            newCursorGotoX = state.cursor.cursorX;
            newCursorGotoY = state.cursor.cursorY;

            switch (action.direction) {
                case 'LEFT':
                    if (newCursorGotoX > 1) {
                        newCursorGotoX = newCursorGotoX - 1;
                    }
                    break;
                case 'RIGHT':
                    if (newCursorGotoX < state.mapData.width) {
                        newCursorGotoX = newCursorGotoX + 1;
                    }
                    break;
                case 'UP':
                    if (newCursorGotoY > 1) {
                        newCursorGotoY = newCursorGotoY - 1;
                    }
                    break;
                case 'DOWN':
                    if (newCursorGotoY < state.mapData.height) {
                        newCursorGotoY = newCursorGotoY + 1;
                    }
                    break;
            }

            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);
            return {
                ...state,
                cursor: {
                    ...state.cursor,
                    cursorX: newCursorGotoX,
                    cursorY: newCursorGotoY
                },
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset']
            };


        case VIEWPORT_SET_CANVAS_SIZE:
            return {
                ...state,
                viewPort: {
                    canvasWidth: action.canvasWidth,
                    canvasHeight: action.canvasHeight
                }
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
            "active": true,
            "xPosition": 48,
            "yPosition": 33
        }
    ]
}




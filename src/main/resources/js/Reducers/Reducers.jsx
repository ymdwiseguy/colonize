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
import {
    getMapOffset,
    getMapOffsetByCursor,
    getMapOffsetByUnits,
    reduceVisibleTiles
} from '../GameLogic/ViewPortCalc.jsx'
import {cursor} from './CursorReducer.jsx';
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
        cursorX: 48,
        cursorY: 33
    },
    didInvalidate: false,
    isFetching: false,
    mapData: {},
    units: unitsInitalState(),
    viewPort: {
        canvasWidth: 0,
        canvasHeight: 0,
        mapOffsetX: 0,
        mapOffsetY: 0
    },
    whoIsActive: 1 // 0 == cursor, number x == unit with id x
}, action) {

    let newCursorGotoX;
    let newCursorGotoY;
    let mapOffsetsGoto;
    let viewPortUpdate;

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

            mapOffsetsGoto = getMapOffsetByCursor(state, state.cursor);

            viewPortUpdate = {
                ...state.viewPort,
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset']
            };

            return {
                ...state,
                cursor: cursor(state.cursor, action),
                didInvalidate: false,
                isFetching: false,
                lastUpdated: action.receivedAt,
                mapData: {
                    ...action.mapData,
                    tiles: reduceVisibleTiles(action.mapData.tiles, viewPortUpdate, action)
                },
                viewPort: viewPortUpdate
            };


        case CURSOR_GOTO:

            newCursorGotoX = action.xPosition;
            newCursorGotoY = action.yPosition;
            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);

            viewPortUpdate = {
                ...state.viewPort,
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset']
            };

            return {
                ...state,
                cursor: cursor(state.cursor, action),
                mapData: {
                    ...state.mapData,
                    tiles: reduceVisibleTiles(state.mapData.tiles, viewPortUpdate, action)
                },
                units: deactivateAllUnits(state.units),
                viewPort: viewPortUpdate,
                whoIsActive: 0
            };


        case UNIT_CLICKED:

            newCursorGotoX = action.xPosition;
            newCursorGotoY = action.yPosition;
            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);

            viewPortUpdate = {
                ...state.viewPort,
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset']
            };

            return {
                ...state,
                cursor: cursor(state.cursor, action),
                mapData: {
                    ...state.mapData,
                    tiles: reduceVisibleTiles(state.mapData.tiles, viewPortUpdate, action)
                },
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
                }),
                viewPort: viewPortUpdate,
                whoIsActive: action.unitId
            };


        case UNIT_MOVE:

            if (state.whoIsActive > 0) {

                const movedUnit = moveUnit(action.unitId, state.units, state.mapData, action.direction);
                mapOffsetsGoto = getMapOffsetByUnits(state, movedUnit);

                viewPortUpdate = {
                    ...state.viewPort,
                    mapOffsetX: mapOffsetsGoto['xOffset'],
                    mapOffsetY: mapOffsetsGoto['yOffset']
                };

                return {
                    ...state,
                    mapData: {
                        ...state.mapData,
                        tiles: reduceVisibleTiles(state.mapData.tiles, viewPortUpdate, action)
                    },
                    units: movedUnit,
                    viewPort: viewPortUpdate
                };
            }
            return state;


        case CURSOR_MOVE:
            if (state.whoIsActive === 0) {

                const movedCursor = cursor(state.cursor, action, state.mapData);
                mapOffsetsGoto = getMapOffsetByCursor(state, movedCursor);

                viewPortUpdate = {
                    ...state.viewPort,
                    mapOffsetX: mapOffsetsGoto['xOffset'],
                    mapOffsetY: mapOffsetsGoto['yOffset']
                };

                return {
                    ...state,
                    cursor: movedCursor,
                    mapData: {
                        ...state.mapData,
                        tiles: reduceVisibleTiles(state.mapData.tiles, viewPortUpdate, action)
                    },
                    viewPort: viewPortUpdate
                };
            }
            return state;


        case VIEWPORT_SET_CANVAS_SIZE:
            viewPortUpdate = {
                ...state.viewPort,
                canvasWidth: action.canvasWidth,
                canvasHeight: action.canvasHeight
            };

            let mapUpdate = state.mapData;
            if (state.mapData.tiles !== undefined && state.mapData.tiles.length > 0) {
                mapUpdate = {
                    ...state.mapData,
                    tiles: reduceVisibleTiles(state.mapData.tiles, viewPortUpdate, action)
                };
            }
            return {
                ...state,
                mapData: mapUpdate,
                viewPort: viewPortUpdate
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


function deactivateAllUnits(units) {
    return units.map((unit) => {
        if (unit.active) {
            return {
                ...unit,
                active: false
            }
        }
        return unit
    })
}


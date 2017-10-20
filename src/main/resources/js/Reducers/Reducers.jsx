import {
    CURSOR_GOTO,
    CURSOR_MOVE,
    RECEIVE_WORLD_MAP,
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
import {didInvalidate, isFetching, whoIsActive} from './SimpleReducers.jsx';
import {moveUnit} from '../GameLogic/UnitMovement.jsx';




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
                didInvalidate: didInvalidate(state.didInvalidate, action),
                isFetching: isFetching(state.isFetching, action),
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
                whoIsActive: whoIsActive(state.whoIsActive, action)
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
                whoIsActive: whoIsActive(state.whoIsActive, action)
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
            return {
                ...state,
                cursor: cursor(state.cursor, action),
                didInvalidate: didInvalidate(state.didInvalidate, action),
                isFetching: isFetching(state.isFetching, action),
                mapData: {},
                units: unitsInitalState(),
                viewPort: {
                    canvasWidth: 0,
                    canvasHeight: 0,
                    mapOffsetX: 0,
                    mapOffsetY: 0
                },
                whoIsActive: whoIsActive(state.whoIsActive, action)
            };
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


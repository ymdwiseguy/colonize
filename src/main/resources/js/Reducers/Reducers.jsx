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
import {mapData} from './MapDataReducer.jsx';
import {units, unitsInitialState} from './UnitsReducer.jsx';
import {didInvalidate, lastUpdated, isFetching, whoIsActive} from './SimpleReducers.jsx';
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
    units: unitsInitialState,
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
                lastUpdated: lastUpdated(state.lastUpdated, action),
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
                units: units(state.units, action),
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
                units: units(state.units, action),
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
                lastUpdated: lastUpdated(state.lastUpdated, action),
                mapData: {},
                units: units(state.units, action),
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




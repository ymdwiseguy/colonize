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

    let returnMapData = state.mapData;
    let returnViewPort = state.viewPort;
    let returnCursor = cursor(state.cursor, action);
    let returnUnits = units(state.units, action);


    if (action.type === RECEIVE_WORLD_MAP) {
        mapOffsetsGoto = getMapOffsetByCursor(state, state.cursor);

        returnViewPort = {
            ...state.viewPort,
            mapOffsetX: mapOffsetsGoto['xOffset'],
            mapOffsetY: mapOffsetsGoto['yOffset']
        };
    }

    if (action.type === CURSOR_GOTO || action.type === UNIT_CLICKED) {
        newCursorGotoX = action.xPosition;
        newCursorGotoY = action.yPosition;
        mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);
        returnViewPort = {
            ...state.viewPort,
            mapOffsetX: mapOffsetsGoto['xOffset'],
            mapOffsetY: mapOffsetsGoto['yOffset']
        };
    }


    if (action.type === UNIT_MOVE && state.whoIsActive > 0) {
        const movedUnit = moveUnit(action.unitId, state.units, state.mapData, action.direction);
        mapOffsetsGoto = getMapOffsetByUnits(state, movedUnit);
        returnViewPort = {
            ...state.viewPort,
            mapOffsetX: mapOffsetsGoto['xOffset'],
            mapOffsetY: mapOffsetsGoto['yOffset']
        };
        returnUnits = movedUnit;
    }

    if (action.type === CURSOR_MOVE && state.whoIsActive === 0) {
        const movedCursor = cursor(state.cursor, action, state.mapData);
        mapOffsetsGoto = getMapOffsetByCursor(state, movedCursor);
        returnViewPort = {
            ...state.viewPort,
            mapOffsetX: mapOffsetsGoto['xOffset'],
            mapOffsetY: mapOffsetsGoto['yOffset']
        };
        returnCursor = movedCursor;
    }

    if (action.type === VIEWPORT_SET_CANVAS_SIZE) {
        returnViewPort = {
            ...state.viewPort,
            canvasWidth: action.canvasWidth,
            canvasHeight: action.canvasHeight
        };
    }

    returnMapData = mapData(state.mapData, action, returnViewPort);

    return {
        ...state,
        cursor: returnCursor,
        didInvalidate: didInvalidate(state.didInvalidate, action),
        isFetching: isFetching(state.isFetching, action),
        lastUpdated: lastUpdated(state.lastUpdated, action),
        mapData: returnMapData,
        units: returnUnits,
        viewPort: returnViewPort,
        whoIsActive: whoIsActive(state.whoIsActive, action)
    };
}




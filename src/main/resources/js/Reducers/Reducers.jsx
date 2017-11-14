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
    getMapOffsetByUnits
} from '../GameLogic/ViewPortCalc.jsx';
import {moveUnit} from '../GameLogic/UnitMovement.jsx';

import {cursor} from './CursorReducer.jsx';
import {mapData} from './MapDataReducer.jsx';
import {didInvalidate, lastUpdated, isFetching, whoIsActive} from './SimpleReducers.jsx';
import {units, unitsInitialState} from './UnitsReducer.jsx';
import {viewPort} from './ViewPortReducer.jsx';


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

    let returnViewPort = state.viewPort;
    let returnCursor = cursor(state.cursor, action);
    let returnUnits = units(state.units, action);

    if (action.type === RECEIVE_WORLD_MAP) {
        mapOffsetsGoto = getMapOffset(state, state.cursor.cursorX, state.cursor.cursorY);
        returnViewPort = viewPort(state.viewPort, action, mapOffsetsGoto);
    }

    if (action.type === CURSOR_GOTO || action.type === UNIT_CLICKED) {
        newCursorGotoX = action.xPosition;
        newCursorGotoY = action.yPosition;
        mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);
        returnViewPort = viewPort(state.viewPort, action, mapOffsetsGoto);
    }

    if (action.type === UNIT_MOVE) {
        const movedUnit = moveUnit(action.unitId, state.units, state.mapData, action.direction);
        mapOffsetsGoto = getMapOffsetByUnits(state, movedUnit);
        returnViewPort = viewPort(state.viewPort, action, mapOffsetsGoto, state.whoIsActive);
        returnUnits = movedUnit;
    }

    if (action.type === CURSOR_MOVE) {
        const movedCursor = cursor(state.cursor, action, state.mapData);
        mapOffsetsGoto = getMapOffset(state, movedCursor.cursorX, movedCursor.cursorY);
        returnViewPort = viewPort(state.viewPort, action, mapOffsetsGoto);
        returnCursor = movedCursor;
    }

    if (action.type === VIEWPORT_SET_CANVAS_SIZE) {
        returnViewPort = viewPort(state.viewPort, action, mapOffsetsGoto);
    }

    const returnMapData = mapData(state.mapData, action, returnViewPort);

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




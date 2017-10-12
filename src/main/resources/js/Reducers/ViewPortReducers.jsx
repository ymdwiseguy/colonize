import {
    RECEIVE_WORLD_MAP,
    CURSOR_MOVE,
    CURSOR_GOTO,
    UNIT_CLICKED,
    UNIT_MOVE,
    VIEWPORT_SET_CANVAS_SIZE
} from '../ActionTypes/ActionTypes.jsx';
import {getMapOffset} from '../GameLogic/ViewPortCalc.jsx'

export function viewPort(state = {
    mapWidth: 0,
    mapHeight: 0,
    canvasWidth: 0,
    canvasHeight: 0,
    cursorActive: true,
    cursorX: 1,
    cursorY: 1,
    mapOffsetX: 0,
    mapOffsetY: 0
}, action) {

    let newCursorGotoX;
    let newCursorGotoY;
    let mapOffsetsGoto;

    switch (action.type) {

        case VIEWPORT_SET_CANVAS_SIZE:
            return Object.assign({}, state, {
                canvasWidth: action.canvasWidth,
                canvasHeight: action.canvasHeight
            });

        case UNIT_CLICKED:
            newCursorGotoX = action.xPosition;
            newCursorGotoY = action.yPosition;
            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);

            return {
                ...state,
                cursorX: newCursorGotoX,
                cursorY: newCursorGotoY,
                cursorActive: false,
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset']
            };

        case CURSOR_GOTO:
            newCursorGotoX = action.xPosition;
            newCursorGotoY = action.yPosition;
            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);

            return {
                ...state,
                cursorX: newCursorGotoX,
                cursorY: newCursorGotoY,
                cursorActive: true,
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset']
            };

        case UNIT_MOVE:
        case CURSOR_MOVE:
            newCursorGotoX = state.cursorX;
            newCursorGotoY = state.cursorY;

            switch (action.direction) {
                case 'LEFT':
                    if (newCursorGotoX > 1) {
                        newCursorGotoX = newCursorGotoX - 1;
                    }
                    break;
                case 'RIGHT':
                    if (newCursorGotoX < state.mapWidth) {
                        newCursorGotoX = newCursorGotoX + 1;
                    }
                    break;
                case 'UP':
                    if (newCursorGotoY > 1) {
                        newCursorGotoY = newCursorGotoY - 1;
                    }
                    break;
                case 'DOWN':
                    if (newCursorGotoY < state.mapHeight) {
                        newCursorGotoY = newCursorGotoY + 1;
                    }
                    break;
            }

            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);
            return {
                ...state,
                cursorX: newCursorGotoX,
                cursorY: newCursorGotoY,
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset']
            };

        case RECEIVE_WORLD_MAP:
            newCursorGotoX = 48;
            newCursorGotoY = 33;

            let newState = {...state, mapWidth: action.mapData.width, mapHeight: action.mapData.height};
            mapOffsetsGoto = getMapOffset(newState, newCursorGotoX, newCursorGotoY);

            return Object.assign({}, newState, {
                mapWidth: action.mapData.width,
                mapHeight: action.mapData.height,
                cursorX: newCursorGotoX,
                cursorY: newCursorGotoY,
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset']
            });

        default:
            return state;
    }
}


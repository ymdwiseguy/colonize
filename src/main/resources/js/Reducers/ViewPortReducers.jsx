import {
    RECEIVE_WORLD_MAP,
    CURSOR_MOVE,
    CURSOR_GOTO,
    VIEWPORT_SET_CANVAS_SIZE
} from '../ActionTypes/ActionTypes.jsx';


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

        case CURSOR_GOTO:

            newCursorGotoX = action.xPosition;
            newCursorGotoY = action.yPosition;
            mapOffsetsGoto = getMapOffset(state, newCursorGotoX, newCursorGotoY);

            return {
                ...state,
                cursorX: newCursorGotoX,
                cursorY: newCursorGotoY,
                mapOffsetX: mapOffsetsGoto['xOffset'],
                mapOffsetY: mapOffsetsGoto['yOffset']
            };

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

            let newState = {... state, mapWidth: action.mapData.width, mapHeight: action.mapData.height };
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


// TODO: move to separate file?
// helpers
function getMapOffset(state, cursorLeft, cursorTop) {

    const GRID_FACTOR = 100;

    const canvasWidth = state.canvasWidth === 0 ? 0 : Math.floor(state.canvasWidth / GRID_FACTOR);
    const canvasHeight = state.canvasHeight === 0 ? 0 : Math.floor(state.canvasHeight / GRID_FACTOR);
    const mapWidth = state.mapWidth;
    const mapHeight = state.mapHeight;
    let mapLeft = state.mapOffsetX;
    let mapTop = state.mapOffsetY;

    if (cursorOutsideViewPort(canvasWidth, canvasHeight, mapLeft, mapTop, cursorLeft, cursorTop)) {
        mapLeft = cursorLeft - canvasWidth / 2;
        mapLeft = limitHorizontally(mapLeft, mapWidth, canvasWidth);
        mapTop = cursorTop - canvasHeight / 2;
        mapTop = limitVertically(mapTop, mapHeight, canvasHeight);

        return {
            'xOffset': (mapLeft === 0 ? 0 : Math.floor(mapLeft)),
            'yOffset': (mapTop === 0 ? 0 : Math.floor(mapTop))
        };
    }
    return {'xOffset': state.mapOffsetX, 'yOffset': state.mapOffsetY};
}

function cursorOutsideViewPort(frameWidth, frameHeight, mapLeft, mapTop, cursorLeft, cursorTop) {
    const PADDING = 2;

    const rightOutside = (frameWidth + mapLeft - PADDING < cursorLeft);
    const leftOuside = (cursorLeft <= (PADDING + mapLeft));
    const topOuside = (cursorTop <= (PADDING + mapTop));
    const bottomOuside = (frameHeight + mapTop - PADDING < cursorTop);

    return (rightOutside || leftOuside || topOuside || bottomOuside);
}

function limitHorizontally(mapLeft, mapWidth, frameWidth) {
    if (mapLeft < 0) {
        mapLeft = 0;
    }
    let limitRight = (mapWidth - mapLeft);
    if (limitRight < frameWidth) {
        mapLeft = mapWidth - frameWidth;
    }
    return mapLeft;
}

function limitVertically(mapTop, mapHeight, frameHeight) {
    if (mapTop < 0) {
        mapTop = 0;
    }
    let limitBottom = (mapHeight - mapTop);
    if (limitBottom < frameHeight) {
        mapTop = mapHeight - frameHeight;
    }
    return mapTop;
}

import {
    GOTO_PAGE,
    CHOOSE_FACTION,
    REQUEST_WORLD_MAP,
    RECEIVE_WORLD_MAP,
    INVALIDATE_WORLD_MAP,
    CURSOR_MOVE,
    ADJUST_VIEWPORT,
    VIEWPORT_SET_CANVAS_SIZE
} from '../ActionTypes/ActionTypes.jsx'

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
    mapData: {}
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
        default:
            return state;
    }
}

export function viewPort(state = {
    mapWith: 0,
    mapHeight: 0,
    canvasWidth: 0,
    canvasHeight: 0,
    cursorActive: true,
    cursorX: 1,
    cursorY: 1,
    mapOffsetX: 0,
    mapOffsetY: 0
}, action) {
    switch (action.type) {
        case VIEWPORT_SET_CANVAS_SIZE:
            return Object.assign({}, state, {
                canvasWidth: action.canvasWidth,
                canvasHeight: action.canvasHeight
            });
        case CURSOR_MOVE:
            if (action.direction === 'RIGHT') {
                return {...state, cursorX: state.cursorX + 1}
            }
            if (action.direction === 'LEFT') {
                return {...state, cursorX: state.cursorX - 1}
            }
            if (action.direction === 'UP') {
                return {...state, cursorY: state.cursorY - 1}
            }
            if (action.direction === 'DOWN') {
                return {...state, cursorY: state.cursorY + 1}
            }
            return state;
        case ADJUST_VIEWPORT:
            let mapOffsets = getMapOffset(state);
            return Object.assign({}, state, {
                mapOffsetX: mapOffsets['xOffset'],
                mapOffsetY: mapOffsets['yOffset']
            });
        case RECEIVE_WORLD_MAP:
            return Object.assign({}, state, {
                mapWith: action.mapData.width,
                mapHeight: action.mapData.height
            });
        default:
            return state;
    }
}

function getMapOffset(state) {

    const GRID_FACTOR = 100;

    const canvasWidth = state.canvasWidth;
    const canvasHeight = state.canvasHeight;
    const cursorLeft = state.cursorX * GRID_FACTOR;
    const cursorTop = state.cursorY * GRID_FACTOR;
    const mapWidth = state.mapWith * GRID_FACTOR;
    const mapHeight = state.mapHeight * GRID_FACTOR;
    let mapLeft = state.mapOffsetX * GRID_FACTOR;
    let mapTop = state.mapOffsetY * GRID_FACTOR;

    if (cursorOutsideViewPort(canvasWidth, canvasHeight, mapLeft, mapTop, cursorLeft, cursorTop)) {
        mapLeft = canvasWidth / 2 - cursorLeft - GRID_FACTOR / 2;
        mapLeft = limitHorizontally(mapLeft, mapWidth, canvasWidth);
        mapTop = canvasHeight / 2 - cursorTop - GRID_FACTOR / 2;
        mapTop = limitVertically(mapTop, mapHeight, canvasHeight);

        return {
            'xOffset': (mapLeft === 0 ? 0 : Math.floor(mapLeft / GRID_FACTOR) * -1),
            'yOffset': (mapTop === 0 ? 0 : Math.floor(mapTop / GRID_FACTOR) * -1)
        };
    }
    return {'xOffset': state.mapOffsetX, 'yOffset': state.mapOffsetY};
}

function cursorOutsideViewPort(frameWidth, frameHeight, mapLeft, mapTop, cursorLeft, cursorTop, GRID_FACTOR = 100) {
    const maxPaddingHorizontal = 7 * GRID_FACTOR;
    let paddingHorizontal = 2 * GRID_FACTOR;
    while ((frameWidth / 2 - GRID_FACTOR) > paddingHorizontal && paddingHorizontal < maxPaddingHorizontal) {
        paddingHorizontal += GRID_FACTOR;
    }

    const maxPaddingVertical = 7 * GRID_FACTOR;
    let paddingVertical = GRID_FACTOR * 2;
    while ((frameHeight / 2 - GRID_FACTOR) > paddingVertical && paddingVertical < maxPaddingVertical) {
        paddingVertical += GRID_FACTOR;
    }

    const rightOutside = ((frameWidth - (mapLeft + cursorLeft)) < (paddingHorizontal));
    const leftOuside = (cursorLeft <= (paddingHorizontal - mapLeft));
    const topOuside = (cursorTop <= (paddingVertical - mapTop));
    const bottomOuside = ((frameHeight - (mapTop + cursorTop)) < (paddingVertical));

    return (rightOutside || leftOuside || topOuside || bottomOuside);
}

function limitHorizontally(mapLeft, mapWidth, frameWidth) {
    if (mapLeft > 0) {
        mapLeft = 0;
    }
    let limitRight = (mapWidth + mapLeft);
    if (limitRight < frameWidth) {
        mapLeft = -1 * (mapWidth - frameWidth);
    }
    return mapLeft;
}

function limitVertically(mapTop, mapHeight, frameHeight) {
    if (mapTop > 0) {
        mapTop = 0;
    }
    let limitBottom = (mapHeight + mapTop);
    if (limitBottom < frameHeight) {
        mapTop = -1 * (mapHeight - frameHeight);
    }
    return mapTop;
}

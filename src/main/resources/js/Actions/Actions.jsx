import {
    GOTO_PAGE,
    CHOOSE_FACTION,
    REQUEST_WORLD_MAP,
    RECEIVE_WORLD_MAP,
    INVALIDATE_WORLD_MAP,
    VIEWPORT_SET_CANVAS_SIZE,
    CURSOR_GOTO,
    CURSOR_MOVE,
    UNIT_MOVE,
    UNIT_CLICKED
} from '../ActionTypes/ActionTypes.jsx'

import fetch from 'isomorphic-fetch'

export function goToPage(text) {
    return {
        type: GOTO_PAGE,
        text
    }
}

export function cursorMove(direction) {
    return {
        type: CURSOR_MOVE,
        direction
    }
}

export function cursorGoto(xPosition, yPosition) {
    return {
        type: CURSOR_GOTO,
        xPosition,
        yPosition
    }
}

export function unitMove(direction, unitId) {
    return {
        type: UNIT_MOVE,
        direction,
        unitId
    }
}

export function unitClicked(xPosition, yPosition, unitId) {
    return {
        type: UNIT_CLICKED,
        xPosition,
        yPosition,
        unitId
    }
}

export function viewPortChangeCanvasSize(canvasWidth, canvasHeight) {
    return {
        type: VIEWPORT_SET_CANVAS_SIZE,
        canvasWidth,
        canvasHeight
    }
}

export function adjustViewPort(cursorX, cursorY) {
    return {
        type: ADJUST_VIEWPORT,
        cursorX,
        cursorY
    }
}

export function chooseFaction(text) {
    return {
        type: CHOOSE_FACTION,
        text
    }
}

export function requestMap(map) {
    return {
        type: REQUEST_WORLD_MAP,
        map
    }
}

export function receiveMap(worldMap, json) {
    return {
        type: RECEIVE_WORLD_MAP,
        worldMap,
        mapData: json,
        receivedAt: Date.now()
    }
}

export function invalidateMap(worldMap) {
    return {
        type: INVALIDATE_WORLD_MAP,
        worldMap
    }
}

// Asynchronous actions:
export function fetchMap(worldMap) {

    return function (dispatch) {
        dispatch(requestMap(worldMap));

        return fetch(`http://localhost:9090/maps/${worldMap}`)
            .then(response => response.json())
            .then(json =>
                dispatch(receiveMap(worldMap, json))
            )

        // TODO: catch any error in the network call.
    }
}


// lets see about those later:
// export function fetchMapError() {
//     return {
//         type: REQUEST_WORLD_MAP,
//         status: 'error',
//         error: 'Oops'
//     }
// }
//
// export function fetchMapSuccess() {
//     return {
//         type: REQUEST_WORLD_MAP,
//         status: 'success',
//         response: {...}
//     }
// }


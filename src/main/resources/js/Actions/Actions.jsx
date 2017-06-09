import {
    GOTO_PAGE,
    CHOOSE_FACTION,
    REQUEST_WORLD_MAP,
    RECEIVE_WORLD_MAP,
    INVALIDATE_WORLD_MAP
} from '../ActionTypes/ActionTypes.jsx'
import fetch from 'isomorphic-fetch'

export function goToPage(text) {
    return {
        type: GOTO_PAGE,
        text
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
    console.log(json);
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


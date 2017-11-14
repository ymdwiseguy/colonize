import {
    VIEWPORT_SET_CANVAS_SIZE
} from '../ActionTypes/ActionTypes.jsx';

export function viewPort(state = {
    canvasWidth: 0,
    canvasHeight: 0,
    mapOffsetX: 0,
    mapOffsetY: 0
}, action, mapOffsetsGoto = {
    'xOffset': 0,
    'yOffset': 0
}, whoIsActive = 0) {

    if (action.type === VIEWPORT_SET_CANVAS_SIZE) {
        return {
            ...state,
            canvasWidth: action.canvasWidth,
            canvasHeight: action.canvasHeight
        };
    }

    return {
        ...state,
        mapOffsetX: mapOffsetsGoto['xOffset'],
        mapOffsetY: mapOffsetsGoto['yOffset']
    };

}


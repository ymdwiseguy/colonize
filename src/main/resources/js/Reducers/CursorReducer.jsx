import {
    CURSOR_GOTO,
    CURSOR_MOVE,
    RECEIVE_WORLD_MAP,
    UNIT_CLICKED
} from '../ActionTypes/ActionTypes.jsx';

export function cursor(state = {
    cursorActive: false,
    cursorX: 48,
    cursorY: 33
}, action, mapData = null) {

    let newCursorGotoX = state.cursorX;
    let newCursorGotoY = state.cursorY;

    switch (action.type) {

        case CURSOR_GOTO:

            newCursorGotoX = action.xPosition;
            newCursorGotoY = action.yPosition;

            return {
                ...state,
                cursorX: newCursorGotoX,
                cursorY: newCursorGotoY,
                cursorActive: true
            };

        case CURSOR_MOVE:

            if (mapData !== null) {
                newCursorGotoY = moveVertically(action.direction, state.cursorY, mapData.height);
                newCursorGotoX = moveHorizontally(action.direction, state.cursorX, mapData.width);

                return {
                    ...state,
                    cursorX: newCursorGotoX,
                    cursorY: newCursorGotoY
                };
            }
            return state;

        case UNIT_CLICKED:

            return {
                ...state,
                cursorActive: false
            };

        default:
            return state;
    }

}

function moveHorizontally(direction, newCursorGotoX, limit) {
    return moveInDirectionWithLimits(direction, newCursorGotoX, limit, 'LEFT', 'RIGHT');
}

function moveVertically(direction, newCursorGotoY, limit) {
    return moveInDirectionWithLimits(direction, newCursorGotoY, limit, 'UP', 'DOWN');
}

function moveInDirectionWithLimits(direction, position, limit, dir1, dir2) {
    switch (direction) {
        case dir1:
            if (position > 1) {
                position = position - 1;
            }
            break;
        case dir2:
            if (position < limit) {
                position = position + 1;
            }
            break;
    }
    return position;
}

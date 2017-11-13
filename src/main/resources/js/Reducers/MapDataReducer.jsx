import {
    RECEIVE_WORLD_MAP
} from '../ActionTypes/ActionTypes.jsx';

import {
    reduceVisibleTiles
} from '../GameLogic/ViewPortCalc.jsx'

export function mapData(state = {}, action, viewPortUpdate) {
    switch (action.type) {

        case RECEIVE_WORLD_MAP:
            return {
                ...action.mapData,
                tiles: reduceVisibleTiles(action.mapData.tiles, viewPortUpdate, action)
            };

        default:
            let mapUpdate = state;
            if (state.tiles !== undefined && state.tiles.length > 0) {
                mapUpdate = {
                    ...state,
                    tiles: reduceVisibleTiles(state.tiles, viewPortUpdate, action)
                };
            }
            return mapUpdate;
    }
}


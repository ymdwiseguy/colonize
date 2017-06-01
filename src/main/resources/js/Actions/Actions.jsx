import * as actionTypes from '../ActionTypes/ActionTypes.jsx'


export function goToPage(text) {
    return {type: actionTypes.GOTO_PAGE, text}
}

export function chooseFaction(text) {
    return {type: actionTypes.CHOOSE_FACTION, text}
}

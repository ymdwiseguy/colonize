import React from 'react';
import {Provider, connect} from 'react-redux'

import * as actions from '../../../Actions/Actions.jsx'
import LinkListPopup from '../Popups/LinkListPopup.jsx'

const entries = [{
    entryName: "Start a new game",
    action: "CHOOSE_FACTION"
}, {
    entryName: "Load a game",
    action: "LOAD_GAME"
}, {
    entryName: "Map editor",
    action: "MAP_EDITOR"
}];


const mapStateToProps = () => ({
    entries: entries,
    header: "Menu"
});

const mapDispatchToProps = (dispatch) => ({
    onMenuItemClick: (goal) => {
        dispatch(actions.goToPage(goal))
    }
});


const InitialPopup = connect(mapStateToProps, mapDispatchToProps)(LinkListPopup);

export default InitialPopup;

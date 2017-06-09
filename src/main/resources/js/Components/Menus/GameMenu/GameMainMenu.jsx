import React from 'react';
import {Provider, connect} from 'react-redux'

import * as actions from '../../../Actions/Actions.jsx'
import LinkListPopup from './LinkListPopup.jsx'

const entries = [{
    entryName: "Netherlands",
    action: "Netherlands"
}, {
    entryName: "France",
    action: "France"
}, {
    entryName: "Spain",
    action: "Spain"
}, {
    entryName: "England",
    action: "England"
}];


const mapStateToProps = () => ({
    entries: entries,
    header: "Choose a Faction"
});

const mapDispatchToProps = (dispatch) => ({
    onMenuItemClick: (faction) => {
        dispatch(actions.chooseFaction(faction));
        dispatch(actions.fetchMap('america'));
    }
});


const ChooseFactionPopup = connect(mapStateToProps, mapDispatchToProps)(LinkListPopup);

export default ChooseFactionPopup;

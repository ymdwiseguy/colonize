import React from 'react';
import {Provider, connect} from 'react-redux'

import * as actions from '../../../Actions/Actions.jsx'
import SubMenu from '../GameMenu/SubMenu.jsx'

const entries = [{
    entryName: "Load a game",
    action: "LOAD_GAME"
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

const FileSubMenu = connect(mapStateToProps, mapDispatchToProps)(SubMenu);

export default FileSubMenu;

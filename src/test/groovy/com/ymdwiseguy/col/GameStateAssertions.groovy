package com.ymdwiseguy.col

import static com.ymdwiseguy.col.GameScreen.MAPEDITOR

trait GameStateAssertions {

    boolean assertBaseGameState(Game mapEditor){
        assert mapEditor != null
        assert mapEditor.getGameId() != null
        assert mapEditor.getGameScreen() == MAPEDITOR
        return true
    }

    boolean assertMinimalGameState(Game mapEditor){
        assertBaseGameState(mapEditor)
        assert mapEditor.getWorldMap() == null
        assert mapEditor.getGameMenu() == null
        assert mapEditor.getSideMenu() == null
        return true
    }

    boolean assertInitialGameState(Game mapEditor){
        assertBaseGameState(mapEditor)
        assert mapEditor.getWorldMap() == null
        assert mapEditor.getGameMenu() != null
        assert mapEditor.getSideMenu() != null
        return true
    }

    boolean assertGameStateWithLoadedMap(Game mapEditor){
        assertBaseGameState(mapEditor)
        assert mapEditor.getWorldMap() != null
        assert mapEditor.getWorldMap().getWorldMapId() != null
        assert mapEditor.getPopupMenu() == null
        return true
    }

}

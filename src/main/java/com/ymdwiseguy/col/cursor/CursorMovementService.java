package com.ymdwiseguy.col.cursor;

import com.ymdwiseguy.col.Game;
import com.ymdwiseguy.col.worldmap.WorldMap;
import com.ymdwiseguy.col.worldmap.movement.UnitDirection;
import org.springframework.stereotype.Component;

@Component
public class CursorMovementService {
    public Game moveCursor(Game mapEditor, UnitDirection direction) {

        int cursorX = mapEditor.getCursor().getxPosition();
        int cursorY = mapEditor.getCursor().getyPosition();

        switch (direction){
            case DOWN:
                cursorY++;
                break;
            case LEFT:
                cursorX--;
                break;
            case RIGHT:
                cursorX++;
                break;
            case UP:
                cursorY--;
                break;
        }

        Cursor cursor = validatePosition(cursorX, cursorY, mapEditor.getWorldMap());
        mapEditor.setCursor(cursor);

        return mapEditor;
    }

    private Cursor validatePosition(int cursorX, int cursorY, WorldMap worldMap){
        if(cursorX < 1){
            cursorX = 1;
        }
        if(cursorY < 1){
            cursorY = 1;
        }
        if(cursorX > worldMap.getWidth()){
            cursorX = worldMap.getWidth();
        }
        if(cursorY > worldMap.getHeight()){
            cursorY = worldMap.getHeight();
        }
        return new Cursor(cursorX, cursorY);
    }
}

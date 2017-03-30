package com.ymdwiseguy.col.cursor

import com.ymdwiseguy.col.Game
import com.ymdwiseguy.col.MapEditorStates
import com.ymdwiseguy.col.worldmap.movement.UnitDirection
import spock.lang.Specification
import spock.lang.Subject

import static com.ymdwiseguy.col.worldmap.movement.UnitDirection.*

class CursorMovementServiceSpec extends Specification implements MapEditorStates {

    @Subject
    CursorMovementService cursorMovementService

    def setup(){
        cursorMovementService = new CursorMovementService()
    }

    def "moving the cursor right"() {
        given: "a cursor position 1x1"
        mapEditorWithLoadedMap()

        when: "moving is called"
        def result = cursorMovementService.moveCursor(mapEditorWithLoadedMap(), RIGHT)

        then: "the cursor was moved right (2x1)"
        result.cursor.xPosition == 2
        result.cursor.yPosition == 1
    }

    def "moving the cursor down"(){
        when: "moving is called"
        def result = cursorMovementService.moveCursor(mapEditorWithLoadedMap(), DOWN)

        then: "the cursor was moved down (1x2)"
        result.cursor.xPosition == 1
        result.cursor.yPosition == 2
    }

    def "moving the cursor down and up again"(){
        when: "moving is called"
        def resultStepOne = cursorMovementService.moveCursor(mapEditorWithLoadedMap(), DOWN)
        def result = cursorMovementService.moveCursor(resultStepOne, UP)

        then: "the cursor was moved down (1x2)"
        result.cursor.xPosition == 1
        result.cursor.yPosition == 1
    }

    def "moving the cursor out of bounds is not possible"(){
        when: "moving to negative coordinates"
        def result = cursorMovementService.moveCursor(mapEditorWithLoadedMap(), UP)

        then: "the cursor was not moved (1x1)"
        result.cursor.xPosition == 1
        result.cursor.yPosition == 1

        when: "moving out of bounds to the bottom right"
        def result2 = moveXTimes(result, 7, DOWN)
        result2 = moveXTimes(result2, 7, RIGHT)

        then: "the cursor does not surpass the edge of the map (5x5)"
        result.cursor.xPosition == 5
        result.cursor.yPosition == 5
    }

    def "setting the cursor to a position directly"(){
        given: "a cursor position 1x1"
        mapEditorWithLoadedMap()

        when: "putting is called"
        def result = cursorMovementService.putCursor(mapEditorWithLoadedMap(), 2, 2)

        then: "the cursor was set to 2x2"
        result.cursor.xPosition == 2
        result.cursor.yPosition == 2
    }

    def "setting the cursor to an invalid position puts it inside the bounds"(){
        given: "a cursor position 1x1"
        mapEditorWithLoadedMap()

        when: "putting is called"
        def result = cursorMovementService.putCursor(mapEditorWithLoadedMap(), 7, -7)

        then: "the cursor does not surpass the edges of the map (5x5)"
        result.cursor.xPosition == 5
        result.cursor.yPosition == 1
    }

    def Game moveXTimes(Game mapEditor, int times, UnitDirection direction){
        for (int i = 0 ; i < times; i++) {
            mapEditor = cursorMovementService.moveCursor(mapEditor, direction)
        }
        return mapEditor
    }

}

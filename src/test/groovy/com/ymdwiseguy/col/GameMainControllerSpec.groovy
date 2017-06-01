package com.ymdwiseguy.col

import spock.lang.Specification
import spock.lang.Subject

import static org.springframework.http.HttpStatus.OK

class GameMainControllerSpec extends Specification {

    @Subject
    GameMainController gameMainController

    def setup() {
        gameMainController = new GameMainController()
    }

    def "calling the game without id returns load menu"() {
        when: "the controller is called"
        def result = gameMainController.getInitialMenu(null)

        then: "the initial menu is rendered"
        result.getStatusCode() == OK
        result.getBody() == mainmenu()
    }


    String mainmenu() {
        '{\n' +
            '  "header" : "Menu",\n' +
            '  "entries" : [ {\n' +
            '    "entryName" : "Start a new game",\n' +
            '    "endpointUrl" : "/",\n' +
            '    "method" : "GET",\n' +
            '    "active" : false\n' +
            '  }, {\n' +
            '    "entryName" : "Load a game",\n' +
            '    "endpointUrl" : "/",\n' +
            '    "method" : "GET",\n' +
            '    "active" : false\n' +
            '  }, {\n' +
            '    "entryName" : "Map editor",\n' +
            '    "endpointUrl" : "/mapeditor/",\n' +
            '    "method" : "GET",\n' +
            '    "active" : false\n' +
            '  } ],\n' +
            '  "type" : "GAME_MAIN_MENU"\n' +
            '}'
    }


}

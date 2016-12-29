package com.ymdwiseguy.col.servicetests

import com.ymdwiseguy.Colonization
import com.ymdwiseguy.col.Game
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.http.HttpStatus.OK


@SpringApplicationConfiguration(classes = Colonization.class)
@WebIntegrationTest("server.port:0")
class MapEditorServiceSpec extends Specification {


    RestTemplate restTemplate = new TestRestTemplate();

    @Value('${local.server.port}')
    int port = 9090


    String URL_MAPEDITOR
    String URL_SHOW_MAPLIST
    String URL_LOAD_MAP

    Game GAME
    String GAME_ID

    def setup() {
        URL_MAPEDITOR = "http://localhost:$port/api/mapeditor"
        URL_SHOW_MAPLIST = "http://localhost:$port/api/mapeditor"
        URL_LOAD_MAP = "http://localhost:$port/api/mapeditor"
    }


    def "starting a 'game' from type mapeditor"() {
        when: "GET is performed"
        ResponseEntity<String> responseEntity = getResponseEntity(HttpMethod.GET, URL_MAPEDITOR)

        then: "Status is 200 (OK)"
        responseEntity.statusCode == OK

        and: "response body is not empty"
        responseEntity.body != ''

        when: "a game is created from the json"
        GAME = new Game().fromJson(responseEntity.body)

        then: "an id can be retrieved"
        GAME.getGameId() != ''
    }

    def "loading a 'game' from type mapeditor"() {
        given: "an initiated game"
        createInitialGame()

        when: "the mapeditor is called"
        ResponseEntity<String> secondResponse = getResponseEntity(HttpMethod.GET, URL_MAPEDITOR + '/' + GAME_ID)
        Game secondGame = new Game().fromJson(secondResponse.body)

        then: "the initiated data is known"
        secondGame.getGameId() == GAME_ID
    }

    def "getting a menu"(){
        given: "an initiated game"
        createInitialGame()

        when: "the load button is clicked"
        then: "a list of maps is returned"
    }


    def createInitialGame(){
        ResponseEntity<String> initialResponse = getResponseEntity(HttpMethod.GET, URL_MAPEDITOR)
        GAME = new Game().fromJson(initialResponse.body)
        GAME_ID = GAME.getGameId()
    }

    ResponseEntity getResponseEntity(HttpMethod method, String url, String body = "nothing here") {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8")
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity responseEntity = restTemplate.exchange(url, method, entity, String.class)
        return responseEntity
    }

    String getGameJson() {
        return """
    {
      "gameId" : "94199ef2-33f9-4448-ab27-433f5023cc86",
      "gameScreen" : "MAPEDITOR",
      "gameMenu" : {
        "submenus" : [ {
          "entryName" : "Editor",
          "menuEntries" : [ {
            "entryName" : "Load Map",
            "endpointUrl" : "/mapeditor/94199ef2-33f9-4448-ab27-433f5023cc86/maps/"
          } ]
        } ]
      },
      "worldMap" : null,
      "sideMenu" : null
    }
    """
    }
}

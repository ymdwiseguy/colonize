package com.ymdwiseguy.servicetests

import com.ymdwiseguy.Colonization
import com.ymdwiseguy.col.Game
import com.ymdwiseguy.col.MapEditorStates
import com.ymdwiseguy.col.menu.structure.MenuEntry
import com.ymdwiseguy.col.menu.structure.PopupMenu
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
class MapEditorServiceSpec extends Specification implements MapEditorStates {


    RestTemplate restTemplate = new TestRestTemplate();

    @Value('${local.server.port}')
    int PORT = 9090


    String URL_MAP_EDITOR
    String URL_MAP_EDITOR_WITH_UUID
    String URL_LOAD_MAP

    Game GAME
    String MAP_NAME

    def setup() {
        URL_MAP_EDITOR = "http://localhost:$PORT/api/mapeditor"
    }

    def createInitialGame() {
        ResponseEntity<String> initialResponse = getResponseEntity(HttpMethod.GET, URL_MAP_EDITOR)
        assert initialResponse.statusCode == OK

        GAME = new Game().fromJson(initialResponse.body)
        GAME_UUID = GAME.getGameId()
        assert GAME_UUID != null

        URL_MAP_EDITOR_WITH_UUID = "http://localhost:$PORT/api/mapeditor/$GAME_UUID"
    }

    def "User journey for the map editor"() {
        when: "program is started (later initial menu)"
        createInitialGame()

        then: "the retrieved JSON is correct"
        GAME.toJson() == initialMapEditorJson(GAME_UUID)

        when: "the map editor is loaded"
        ResponseEntity<String> responseEntity = getResponseEntity(HttpMethod.GET, URL_MAP_EDITOR_WITH_UUID)
        GAME = new Game().fromJson(responseEntity.body)

        then: "menus are set"
        responseEntity.body == initialMapEditorJsonWithMenus(GAME_UUID)

        //TODO: generate a map
        //TODO: save a map

    }

    def "loading a 'game' from type mapeditor"() {
        given: "an initiated game"
        createInitialGame()

        when: "the mapeditor is called"
        ResponseEntity<String> responseEntity = getResponseEntity(HttpMethod.GET, URL_MAP_EDITOR + '/' + GAME_UUID)
        Game editorInstance = new Game().fromJson(responseEntity.body)

        then: "the initiated data is known"
        editorInstance.getGameId() == GAME_UUID
    }

    def "getting a menu"() {
        given: "an initiated game"
        createInitialGame()

        when: "the load button is clicked"
        ResponseEntity<String> responseEntity = getResponseEntity(HttpMethod.GET, URL_MAP_EDITOR + '/?showPopup=SHOW_MAPLIST')
        Game editorInstance = new Game().fromJson(responseEntity.body)

        then: "a list of maps is returned"
        editorInstance.getPopupMenu().getEntries().size() > 0
        assertMenuContains(editorInstance.getPopupMenu(), 'testSandbox')
    }

    def "getting a map"() {
        given: "an initiated game with a selected map name"
        getAGameAndGetAMapName()

        when: "the map is fetched"
        ResponseEntity<String> responseEntity = getResponseEntity(HttpMethod.GET, URL_LOAD_MAP)
        Game editorInstance = new Game().fromJson(responseEntity.body)

        then: "my game includes the worldmap"
        editorInstance.getWorldMap() != null
        editorInstance.getWorldMap().getWorldMapId() != null
    }

    def assertMenuContains(PopupMenu menu, String entry) {
        List<MenuEntry> entries = menu.getEntries()
        for (MenuEntry menuEntry : entries) {
            if (menuEntry.getEntryName() == entry) {
                return true
            }
        }
        return false
    }

    def getAGameAndGetAMapName() {
        createInitialGame()

        ResponseEntity<String> responseEntity = getResponseEntity(HttpMethod.GET, URL_MAP_EDITOR_WITH_UUID + '/?showPopup=SHOW_MAPLIST')
        Game editorInstance = new Game().fromJson(responseEntity.body)

        MAP_NAME = editorInstance.getPopupMenu().getEntries().get(0).getEntryName()
        URL_LOAD_MAP = URL_MAP_EDITOR_WITH_UUID + '/maps/' + MAP_NAME
    }

    ResponseEntity getResponseEntity(HttpMethod method, String url, String body = "nothing here") {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8")
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity responseEntity = restTemplate.exchange(url, method, entity, String.class)
        return responseEntity
    }


}

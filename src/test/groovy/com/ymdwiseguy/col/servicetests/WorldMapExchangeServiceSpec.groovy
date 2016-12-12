package com.ymdwiseguy.col.servicetests

import com.ymdwiseguy.Colonization
import com.ymdwiseguy.col.worldmap.tile.Tile
import com.ymdwiseguy.col.worldmap.WorldMap
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

import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpMethod.PUT
import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.OK

@SpringApplicationConfiguration(classes = Colonization.class)
@WebIntegrationTest("server.port:0")
class WorldMapExchangeServiceSpec extends Specification {

    RestTemplate restTemplate = new TestRestTemplate();

    @Value('${local.server.port}')
    int port = 9090

    int WIDTH = 2
    int HEIGHT = 2
    WorldMap GENERATED_MAP
    String GENERATED_MAP_UUID


    ResponseEntity getResponseEntity(HttpMethod method, String url, String body = "nothing here") {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8")
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity responseEntity = restTemplate.exchange(url, method, entity, String.class)
        return responseEntity
    }


    def "Maps can be generated, saved, updated and loaded (JSON)"() {
        when: "GET is performed"
        ResponseEntity<String> responseEntity = getResponseEntity(HttpMethod.POST, "http://localhost:$port/api/maps/generate/$WIDTH/$HEIGHT")

        then: "Status is 201 (POST/CREATED)"
        responseEntity.statusCode == CREATED

        and: "body is not empty"
        responseEntity.body != ''

        when: "the response is converted"
        GENERATED_MAP = new WorldMap().fromJson(responseEntity.body)
        GENERATED_MAP_UUID = GENERATED_MAP.getWorldMapId()

        then: "it has the expected data"
        GENERATED_MAP.getTitle() == "new Map"
        GENERATED_MAP.getWidth() == 2
        GENERATED_MAP.getHeight() == 2
        GENERATED_MAP.getTiles().size() == 4

        when: "the map is fetched (GET/READ)"
        ResponseEntity<String> fetchedResponse = getResponseEntity(GET, "http://localhost:$port/api/maps/$GENERATED_MAP_UUID")

        then: "Status is 200 (OK)"
        fetchedResponse.statusCode == OK

        and: "the expected data is returned"
        WorldMap worldMapFetched = new WorldMap().fromJson(fetchedResponse.body)
        assertWorldMap(worldMapFetched)

        when: "the map is saved (POST/UPDATE)"
        worldMapFetched.setTitle("My Worldmap (renamed)")
        ResponseEntity<String> updatedResponse = getResponseEntity(PUT, "http://localhost:$port/api/maps/$GENERATED_MAP_UUID", worldMapFetched.toJson())

        then: "Status is 200 (OK)"
        updatedResponse.statusCode == OK

        and: "the expected data is returned"
        WorldMap updatedWorldMapFetched = new WorldMap().fromJson(updatedResponse.getBody())
        assertWorldMap(updatedWorldMapFetched, worldMapFetched)

        when: "a new map without uuid is beeing saved"
        ResponseEntity<String> newResponse = getResponseEntity(HttpMethod.POST, "http://localhost:$port/api/maps", getMapJson())

        then: "Status is 201 (POST/CREATED)"
        newResponse.statusCode == CREATED

        and: "the map is returned with a generated uuid"
        WorldMap newWorldMap = new WorldMap().fromJson(newResponse.getBody())
        newWorldMap.getWorldMapId() != null
        WorldMap comparator = new WorldMap().fromJson(getMapJson())
        comparator.setWorldMapId(newWorldMap.getWorldMapId())
        assertWorldMap(newWorldMap, comparator)
    }

    def boolean assertWorldMap(WorldMap worldMap, WorldMap comparator = GENERATED_MAP) {
        assert worldMap.getWorldMapId() == comparator.getWorldMapId()
        assert worldMap.getTitle() == comparator.getTitle()
        assert worldMap.getWidth() == comparator.getWidth()
        assert worldMap.getHeight() == comparator.getHeight()
        List<Tile> tiles = worldMap.getTiles()
        List<Tile> comparedTiles = comparator.getTiles()
        assert tiles.size() == comparedTiles.size()
        for (Tile tile : tiles) {
            comparedTiles.contains(tile)
        }
        return true
    }

    String getMapJson() {
        return """
        {
            "title" : "another new Map",
            "tiles" : [ {
                "tileId" : "43e8e83d-4a3d-401d-8eff-76e9974ea72e",
                    "worldMapId" : "49a2a2c1-b474-4234-bc39-b8bfafecac24",
                    "xCoordinate" : 1,
                    "yCoordinate" : 1,
                    "type" : "OCEAN_DEEP"
                }, {
                    "tileId" : "3741a446-f2e7-4648-a7c6-bfce5446ba9b",
                    "worldMapId" : "49a2a2c1-b474-4234-bc39-b8bfafecac24",
                    "xCoordinate" : 2,
                    "yCoordinate" : 1,
                    "type" : "OCEAN_DEEP"
                }, {
                    "tileId" : "db9f5605-6777-44c3-8879-9c20bdedd414",
                    "worldMapId" : "49a2a2c1-b474-4234-bc39-b8bfafecac24",
                    "xCoordinate" : 1,
                    "yCoordinate" : 2,
                    "type" : "OCEAN_DEEP"
                }, {
                    "tileId" : "5854b963-9379-4d82-b8de-8a3d4864a5e5",
                    "worldMapId" : "49a2a2c1-b474-4234-bc39-b8bfafecac24",
                    "xCoordinate" : 2,
                    "yCoordinate" : 2,
                    "type" : "OCEAN_DEEP"
                } ],
            "width" : 2,
            "height" : 2
        }
        """
    }
}

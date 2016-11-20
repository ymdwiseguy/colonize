package com.ymdwiseguy.col.worldmap

import com.ymdwiseguy.Colonization
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification


@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [Colonization])
@WebAppConfiguration
class WorldMapRepoSpec extends Specification {

    @Autowired
    def JdbcTemplate jdbcTemplate

    @Autowired
    def WorldMapService worldMapService

    WorldMap WORLD_MAP
    String WORLD_MAP_ID

    def setup() {
        WORLD_MAP_ID = UUID.randomUUID().toString()
        WORLD_MAP = new WorldMap(WORLD_MAP_ID)
        WORLD_MAP.setTitle("World Map")
    }

    def "WorldMap CRUD"() {
        given: "a worldmap repository"
        WorldMapRepo worldMapRepo = new WorldMapRepo(jdbcTemplate)

        when: "a worldmap is written to the DB"
        String worldMapId = worldMapRepo.createWorldmap(WORLD_MAP)

        then: "no execption is been thrown"
        notThrown(Exception)

        when: "the worldmap is retrieved"
        Optional<WorldMap> fetchedWorldMap = worldMapRepo.getWorldmap(worldMapId)

        then: "the worldmap is the same as the saved one"
        assertWorldMap(fetchedWorldMap, WORLD_MAP)

        when: "the worldmap is updated"
        WORLD_MAP.setTitle("Updated World Map")
        worldMapRepo.updateWorldmap(WORLD_MAP)
        Optional<WorldMap> updatedMap = worldMapRepo.getWorldmap(WORLD_MAP_ID)

        then: "no exception is been thrown"
        notThrown(Exception)

        and: "the result fits the expectation"
        assertWorldMap(updatedMap, WORLD_MAP)

        when: "the worldmap is deleted"
        worldMapRepo.deleteWorldmap(WORLD_MAP_ID)

        and: "the map is fetched"
        Optional<WorldMap> deletedMap = worldMapRepo.getWorldmap(WORLD_MAP_ID)

        then: "nothing is found"
        deletedMap == Optional.empty()
    }


    def assertWorldMap(Optional<WorldMap> result, WorldMap comparator) {
        assert result.isPresent()
        WorldMap resultWorldMap = result.get()
        assert resultWorldMap.getWorldMapID() == comparator.getWorldMapID()
        assert resultWorldMap.getTitle() == comparator.getTitle()
        assert resultWorldMap.getWidth() == comparator.getWidth()
        assert resultWorldMap.getHeight() == comparator.getHeight()
        return true
    }
}

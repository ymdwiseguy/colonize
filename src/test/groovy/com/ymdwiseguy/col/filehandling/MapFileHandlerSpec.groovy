package com.ymdwiseguy.col.filehandling

import com.ymdwiseguy.Colonization
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import spock.lang.Specification
import spock.lang.Subject

import javax.inject.Inject


@SpringApplicationConfiguration(classes = Colonization.class)
@WebIntegrationTest("server.port:0")
class MapFileHandlerSpec extends Specification {

    @Subject
    @Inject
    MapFileHandler mapFileHandler

    String FILENAME = "randomFilename"

    def "a map can be read from file"(){
        when: "the map is read"
        Optional<String> fileContent = mapFileHandler.readDataFromFile("testSandbox")

        then: "filecontent is returned"
        fileContent.isPresent()
        fileContent.get().contains("testSandbox")
    }

    def "a map can be written to a file"(){
        when: "the write method is called"
        boolean success = mapFileHandler.writeDataToFile(FILENAME, "some content")

        then: "a file is written"
        success

        when: "the content is read"
        Optional<String> fileContent = mapFileHandler.readDataFromFile(FILENAME)

        then: "some content is available"
        fileContent.isPresent()
        fileContent.get().contains("some content")

        cleanup: "delete file"
        assert mapFileHandler.deleteFile(FILENAME)
    }
}

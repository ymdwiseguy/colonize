package com.ymdwiseguy.col.views

import com.ymdwiseguy.col.filehandling.FileGetter
import spock.lang.Specification

class MapConfigurationReaderSpec extends Specification {

    FileGetter fileGetter = Mock(FileGetter)

    def "a json config is read correctly "() {
        given: "a config reader"
        fileGetter.readDataFromFile(_,_) >> Optional.of('someFile')
        def reader = new MapConfigurationReader(fileGetter, "maps/", ".json")

        when: "the reader reads"
        def config = reader.read()

        then: "the config is present"
        config.isPresent() == isPresent

        where:
        filename      | isPresent
        "testSandbox" | true
    }
}

package com.ymdwiseguy.col.views

import spock.lang.Specification

class MapConfigurationReaderSpec extends Specification {

    def "a json config is read correctly "() {
        given: "a config reader"
        def reader = new MapConfigurationReader("maps/", ".json")
        reader.setFilename(filename)

        when: "the reader reads"
        def config = reader.read()

        then: "the config is present"
        config.isPresent() == isPresent

        where:
        filename      | isPresent
        "testSandbox" | true
        "serv.ices"   | false
    }
}

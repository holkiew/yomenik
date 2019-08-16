package com.holkiew.yomenik.gateway.authentication.adapter

import com.holkiew.yomenik.gateway.GatewayApplication
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import spock.lang.Specification

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = GatewayApplication.class)
@WebFluxTest
class UserRepositoryAdapterTest extends Specification {

    @Autowired
    ApplicationContext context

    @Autowired
    UserRepositoryAdapter testedObj

    @Test
    def "test jakis "() {
        expect:
            context != null
            testedObj != null
            testedObj.findByUsername("user").log().subscribe()

    }
}

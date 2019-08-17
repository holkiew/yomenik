package com.holkiew.yomenik.gateway.authentication.adapter

import com.holkiew.yomenik.gateway.GatewayApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = GatewayApplication.class)
//@WebFluxTest
@ContextConfiguration(classes = GatewayApplication.class)
@WebAppConfiguration
@WebFluxTest
class UserRepositoryAdapterTest extends Specification {

    @Autowired
    ApplicationContext context

    @Autowired
    UserRepositoryAdapter testedObj

    def jakisTest() {
        given:
            def s1 = ""
        when:
            def s = ""
        then:
            context != null
            testedObj != null
            testedObj.findByUsername("user").log().subscribe()
    }
}

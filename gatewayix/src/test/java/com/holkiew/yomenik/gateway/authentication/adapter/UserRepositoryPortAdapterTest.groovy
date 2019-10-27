package com.holkiew.yomenik.gateway.authentication.adapter

import com.holkiew.yomenik.gateway.GatewayApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = GatewayApplication.class)
@WebAppConfiguration
@WebFluxTest
class UserRepositoryPortAdapterTest extends Specification {

    @Autowired
    ApplicationContext context

    @Autowired
    UserRepositoryAdapter testedObj

    def springContextTest() {
        expect:
            context != null
            testedObj != null
    }
}

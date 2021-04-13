package com.malfoj.rambo.reactor

import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class FluxSizeTest {

    @Test
    fun `check flux size`() {
        //g iven
        var givenFlux = Flux.empty<String>()

        // when
        for (i in 1..100000) {
            givenFlux = givenFlux.mergeWith(Mono.just("test"))
        }

        // then
        StepVerifier
                .create(givenFlux)
                .expectNextCount(100000)
                .verifyComplete()
    }
}
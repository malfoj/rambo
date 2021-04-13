package com.malfoj.rambo.interceptor

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE],
                consumes = [MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE, MediaType.ALL_VALUE])
private class MainWebhook(private val ramboFacade: RamboFacade) {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/raw/{service}")
    private fun getEntries(@PathVariable service: String): Flux<EntryData> {
        return ramboFacade.getAllRequests(service).sort { o1, o2 ->
            o2.timestamp.compareTo(o1.timestamp)
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/response/{service}")
    private fun getCurrentResponse(@PathVariable service: String): Mono<String> {
        return Mono.just(ramboFacade.getCustomResponse(service))
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/response/{service}")
    private fun setCustomResponse(@PathVariable service: String, @RequestBody body: String): Mono<String> {
        return Mono.just(ramboFacade.setupCustomResponse(service, body))
    }
}
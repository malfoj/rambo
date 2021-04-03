package com.malfoj.rambo.interceptor

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/hook",
                produces = [MediaType.APPLICATION_JSON_VALUE],
                consumes = [MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE, MediaType.ALL_VALUE])
private class Webhook(private val ramboFacade: RamboFacade) {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{service}")
    private fun getEntries(@PathVariable service: String): Flux<EntryData> {
        return ramboFacade.getAllRequests(service)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{service}")
    private fun postEndpoint(@PathVariable service: String,
                             @RequestHeader headers: Map<String, String>,
                             @RequestBody(required = false) body: Any): Mono<String> {
        return ramboFacade.saveRequest(service, headers, body)
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{service}")
    private fun setCustomResponse(@PathVariable service: String, @RequestBody body: String): Mono<String> {
        return ramboFacade.setupCustomResponse(service, body)
    }
}

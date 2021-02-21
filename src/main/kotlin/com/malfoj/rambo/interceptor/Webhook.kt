package com.malfoj.rambo.interceptor

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/hook")
private class Webhook(private val ramboFacade: RamboFacade) {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{service}")
    private fun getEntries(@PathVariable service: String): Mono<List<EntryData>> {
        return ramboFacade.getAllRequests(service)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{service}")
    private fun postEndpoint(@PathVariable service: String,
                             @RequestHeader headers: Map<String, String>,
                             @RequestBody(required = false) body: Any): Mono<RamboResponse> {
        return ramboFacade.saveRequest(service, headers, body)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{service}/customresponse")
    private fun setCustomResponse(@PathVariable service: String, @RequestBody body: Any): Mono<RamboResponse> {
        return ramboFacade.setupCustomResponse(service, body)
    }
}

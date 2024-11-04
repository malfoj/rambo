package com.malfoj.rambo.api

import com.malfoj.rambo.interceptor.RamboFacade
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/hook",
                produces = [MediaType.APPLICATION_JSON_VALUE],
                consumes = [MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE, MediaType.ALL_VALUE])
private class HookWebhook(private val ramboFacade: RamboFacade) {

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{service}")
    private fun post(@PathVariable service: String,
                     @RequestHeader headers: Map<String, String>,
                     @RequestBody(required = false) body: Any): Mono<String> {
        return ramboFacade.saveRequest(service, headers, body, RequestMethod.POST)
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{service}")
    private fun put(@PathVariable service: String,
                    @RequestHeader headers: Map<String, String>,
                    @RequestBody(required = false) body: Any): Mono<String> {
        return ramboFacade.saveRequest(service, headers, body, RequestMethod.PUT)
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{service}")
    private fun patch(@PathVariable service: String,
                      @RequestHeader headers: Map<String, String>,
                      @RequestBody(required = false) body: Any): Mono<String> {
        return ramboFacade.saveRequest(service, headers, body, RequestMethod.PATCH)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{service}")
    private fun get(@PathVariable service: String,
                    @RequestHeader headers: Map<String, String>,
                    @RequestBody(required = false) body: Any): Mono<String> {
        return ramboFacade.saveRequest(service, headers, body, RequestMethod.GET)
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{service}")
    private fun delete(@PathVariable service: String,
                       @RequestHeader headers: Map<String, String>,
                       @RequestBody(required = false) body: Any): Mono<String> {
        return ramboFacade.saveRequest(service, headers, body, RequestMethod.DELETE)
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{service}", method = [RequestMethod.HEAD])
    private fun head(@PathVariable service: String,
                     @RequestHeader headers: Map<String, String>,
                     @RequestBody(required = false) body: Any): Mono<String> {
        return ramboFacade.saveRequest(service, headers, body, RequestMethod.HEAD)
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/{service}", method = [RequestMethod.OPTIONS])
    private fun options(@PathVariable service: String,
                     @RequestHeader headers: Map<String, String>,
                     @RequestBody(required = false) body: Any): Mono<String> {
        return ramboFacade.saveRequest(service, headers, body, RequestMethod.OPTIONS)
    }
}

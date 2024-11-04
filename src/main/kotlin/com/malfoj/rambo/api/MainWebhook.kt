package com.malfoj.rambo.api

import com.malfoj.rambo.interceptor.RamboFacade
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping(
    produces = [MediaType.APPLICATION_JSON_VALUE],
    consumes = [MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_XML_VALUE, MediaType.ALL_VALUE]
)
private class MainWebhook(private val ramboFacade: RamboFacade) {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/raw/{service}")
    private fun getEntries(@PathVariable service: String): List<EntryDataResponse> {
        return getAllEntriesMapThemAndSort(service)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/response/{service}")
    private fun getCurrentResponse(@PathVariable service: String): String {
        return ramboFacade.getCustomResponse(service)
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/response/{service}")
    private fun setCustomResponse(@PathVariable service: String, @RequestBody body: String): String {
        return ramboFacade.setupCustomResponse(service, body)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/fetchall")
    private fun getAllServices(@RequestParam(required = false) auth: String?): List<String> {
        if (auth == null || !"lukas".equals(auth, ignoreCase = true)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found")
        }
        return ramboFacade.getAllResponses()
    }

    private fun getAllEntriesMapThemAndSort(service: String) = ramboFacade.getAllRequests(service)
        .map {
            EntryDataResponse(
                timestamp = it.timestamp,
                headers = it.headers,
                body = it.body,
                response = it.response,
                requestMethod = it.requestMethod
            )
        }
        .sortedWith { o1, o2 ->
            o2.timestamp.compareTo(o1.timestamp)
        }
}
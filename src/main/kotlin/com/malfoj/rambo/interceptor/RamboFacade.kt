package com.malfoj.rambo.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMethod
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@Component
internal class RamboFacade(private val requestsRepository: RequestRepository,
                           private val responsesRepository: ResponsesRepository) {

    internal fun getAllRequests(service: String): List<EntryData> {
        return requestsRepository.getAll(service)
    }

    internal fun saveRequest(service: String,
                             headers: Map<String, String>,
                             body: Any?,
                             requestMethod: RequestMethod): Mono<String> {

        val entryData =
                EntryData(timestamp = Instant.now(),
                          headers = headers,
                          body = getBodyOrEmptyString(body),
                          response = getCustomResponse(service),
                          requestMethod = requestMethod)

        requestsRepository.add(service, entryData)
        return Mono.just(entryData.response)
    }

    internal fun getAllResponses(): List<String> {
        return responsesRepository.getAllServices()
    }

    internal fun setupCustomResponse(service: String, body: String): String {
        responsesRepository.add(service, body)
        return responsesRepository.get(service)!!
    }

    internal fun getCustomResponse(service: String): String {
        var response = """
            { 
                "name": "John",
                "surname": "Rambo"
            }
            """

        if (responsesRepository.get(service) is String) {
            response = responsesRepository.get(service)!!
        }
        return response
    }

    private fun getBodyOrEmptyString(body: Any?): Any {
        if (body == null) {
            return ""
        }
        return body
    }
}
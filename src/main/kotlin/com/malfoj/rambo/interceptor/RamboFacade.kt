package com.malfoj.rambo.interceptor

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@Component
internal class RamboFacade(private val requestsRepository: RequestRepository,
                           private val responsesRepository: ResponsesRepository) {

    internal fun getAllRequests(service: String): Flux<EntryData> {
        return requestsRepository.getAll(service).log()
    }

    internal fun saveRequest(service: String, headers: Map<String, String>, body: Any?): Mono<String> {
        var response = """
            { 
                "name": "John",
                "surname": "Rambo"
            }
            """

        if (responsesRepository.get(service) is String) {
            response = responsesRepository.get(service)!!
        }

        val entryData =
                EntryData(timestamp = Instant.now(),
                          headers = headers,
                          body = getBodyOrEmptyString(body),
                          response = response)


        requestsRepository.add(service, entryData)
        return Mono.just(entryData.response)
    }

    internal fun setupCustomResponse(service: String, body: String): Mono<String> {
        responsesRepository.add(service, body)
        return Mono.just(responsesRepository.get(service)!!)
    }

    private fun getBodyOrEmptyString(body: Any?): Any {
        if (body == null) {
            return ""
        }
        return body
    }
}
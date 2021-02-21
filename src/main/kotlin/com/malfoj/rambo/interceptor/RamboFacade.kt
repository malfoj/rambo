package com.malfoj.rambo.interceptor

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.Instant

@Component
internal class RamboFacade(private val requestsRepository: RequestRepository,
                           private val responsesRepository: ResponsesRepository) {

    internal fun getAllRequests(service: String): Mono<List<EntryData>> {
        return Mono.just(requestsRepository.getAll(service).reversed())
    }

    internal fun saveRequest(service: String, headers: Map<String, String>, body: Any?): Mono<RamboResponse> {
        var response = RamboDefaultResponse() as RamboResponse

        if (responsesRepository.get(service) is RamboResponse) {
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

    internal fun setupCustomResponse(service: String, body: Any): Mono<RamboResponse> {
        val ramboCustomResponse = RamboCustomResponse(body)
        responsesRepository.add(service, ramboCustomResponse)
        return Mono.just(responsesRepository.get(service)!!)
    }

    private fun getBodyOrEmptyString(body: Any?): Any {
        if (body == null) {
            return ""
        }
        return body
    }
}
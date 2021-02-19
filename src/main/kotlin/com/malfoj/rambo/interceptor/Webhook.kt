package com.malfoj.rambo.interceptor

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.time.Instant

@RestController
@RequestMapping("/hook")
private class Webhook(val datasource: Datasource) {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{service}")
    private fun getEntries(@PathVariable service: String): Mono<List<EntryData>> {
        return Mono.just(datasource.collection[service]?.reversed() ?: listOf())
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{service}")
    private fun postEndpoint(@PathVariable service: String, @RequestHeader headers: Map<String, String>, @RequestBody body: Any): Mono<RamboResponse> {

        if (datasource.collection[service] == null) {
            datasource.collection[service] = mutableListOf()
        }
        datasource.collection[service]!!.add(
                EntryData(timestamp = Instant.now(), headers = headers, body = body)
        )
        return Mono.just(RamboDefaultResponse())
    }
}

@Component
private class Datasource {

    val collection: MutableMap<String, MutableList<EntryData>> = mutableMapOf()

    @Scheduled(cron = "0 0 6 * * *")
    fun clearCollection() {
        collection.clear()
    }
}

private interface RamboResponse

private data class RamboDefaultResponse(val name: String = "Rambo", val greeting: String = "Hello traveler!") :
        RamboResponse

private data class EntryData(val timestamp: Instant, val headers: Map<String, String>, val body: Any)

@Configuration
@EnableScheduling
private class SchedulerConfig
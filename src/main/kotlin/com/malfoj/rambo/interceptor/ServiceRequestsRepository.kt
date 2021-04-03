package com.malfoj.rambo.interceptor

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@Configuration
internal class RepositoryConfiguration {

    @Bean
    internal fun getRequestRepository(): RequestRepository {
        return ServiceRequestsRepository()
    }

    @Bean
    internal fun getResponsesRepository(): ResponsesRepository {
        return ServiceResponsesRepository()
    }
}

internal interface RequestRepository {

    fun add(service: String, entryData: EntryData)

    fun getAll(service: String): Flux<EntryData>
}

internal interface ResponsesRepository {

    fun add(service: String, response: String)

    fun get(service: String): String?
}

internal class ServiceRequestsRepository : RequestRepository {

    private val collection: MutableMap<String, Flux<EntryData>> = mutableMapOf()

    override fun add(service: String, entryData: EntryData) {
        ensureCollectionIsInitated(service)
        this.collection[service] = Flux.merge(Mono.just(entryData), this.collection[service]!!)
    }

    override fun getAll(service: String): Flux<EntryData> {
        ensureCollectionIsInitated(service)
        return this.collection[service]!!
    }

    private fun ensureCollectionIsInitated(service: String) {
        if (this.collection[service] == null) {
            this.collection[service] = Flux.empty()
        }
    }

    @Scheduled(cron = "0 0 6 * * *", zone = "GMT+1")
    private fun clearCollection() {
        collection.clear()
    }
}

internal class ServiceResponsesRepository : ResponsesRepository {

    private val collection: MutableMap<String, String> = mutableMapOf()

    override fun add(service: String, response: String) {
        this.collection[service] = response
    }

    override fun get(service: String): String? {
        return this.collection[service]
    }

    @Scheduled(cron = "0 0 6 * * *", zone = "GMT+1")
    private fun clearCollection() {
        collection.clear()
    }
}

internal data class EntryData(val timestamp: Instant,
                              val headers: Map<String, String>,
                              val body: Any?,
                              val response: String)

@Configuration
@EnableScheduling
private class SchedulerConfig
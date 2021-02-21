package com.malfoj.rambo.interceptor

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.time.Instant

@Configuration
private class RepositoryConfiguration {

    @Bean
    fun getRequestRepository(): RequestRepository {
        return ServiceRequestsRepository()
    }

    @Bean
    fun getResponsesRepository(): ResponsesRepository {
        return ServiceResponsesRepository()
    }
}

internal interface RequestRepository {

    fun add(service: String, entryData: EntryData)

    fun getAll(service: String): MutableList<EntryData>
}

internal interface ResponsesRepository {

    fun add(service: String, response: RamboResponse)

    fun get(service: String): RamboResponse?
}

private class ServiceRequestsRepository : RequestRepository {

    private val collection: MutableMap<String, MutableList<EntryData>> = mutableMapOf()

    override fun add(service: String, entryData: EntryData) {
        ensureCollectionIsInitated(service)
        this.collection[service]!!.add(entryData)
    }

    override fun getAll(service: String): MutableList<EntryData> {
        ensureCollectionIsInitated(service)
        return this.collection[service]!!
    }

    private fun ensureCollectionIsInitated(service: String) {
        if (this.collection[service] == null) {
            this.collection[service] = mutableListOf()
        }
    }

    @Scheduled(cron = "0 0 6 * * *", zone = "GMT+1")
    private fun clearCollection() {
        collection.clear()
    }
}

private class ServiceResponsesRepository : ResponsesRepository {

    private val collection: MutableMap<String, RamboResponse> = mutableMapOf()

    override fun add(service: String, response: RamboResponse) {
        this.collection[service] = response
    }

    override fun get(service: String): RamboResponse? {
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
                              val response: RamboResponse)

@Configuration
@EnableScheduling
private class SchedulerConfig
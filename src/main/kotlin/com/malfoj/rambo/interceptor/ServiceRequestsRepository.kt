package com.malfoj.rambo.interceptor

import com.malfoj.rambo.api.EntryDataResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.RequestMethod
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

    fun getAll(service: String): List<EntryData>
}

internal interface ResponsesRepository {

    fun add(service: String, response: String)

    fun get(service: String): String?

    fun getAllServices(): List<String>
}

internal class ServiceRequestsRepository : RequestRepository {

    private val collection: MutableMap<String, List<EntryData>> = mutableMapOf()

    override fun add(service: String, entryData: EntryData) {
        ensureCollectionIsInitated(service)
        this.collection[service] = this.collection[service]!!.plus(entryData)
    }

    override fun getAll(service: String): List<EntryData> {
        ensureCollectionIsInitated(service)
        return this.collection[service]!!
    }

    private fun ensureCollectionIsInitated(service: String) {
        if (this.collection[service] == null) {
            this.collection[service] = ArrayList()
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

    override fun getAllServices(): List<String> {
        return this.collection.keys.toList()
    }

    //@Scheduled(cron = "0 0 6 * * *", zone = "GMT+1")
    private fun clearCollection() {
        collection.clear()
    }
}

internal data class EntryData(val timestamp: Instant,
                             val headers: Map<String, String>,
                             val body: Any?,
                             val response: String,
                             val requestMethod: RequestMethod
)

@Configuration
@EnableScheduling
private class SchedulerConfig
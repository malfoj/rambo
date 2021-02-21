package com.malfoj.rambo.interceptor

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

class RepositoryTests {

    private lateinit var givenRepository: RequestRepository

    @BeforeEach
    fun reset(){
        givenRepository = RepositoryConfiguration().getRequestRepository()
    }

    @Test
    fun `should add to data source a new request`() {
        // given
        val givenService = "service"
        val givenTimestamp = Instant.now()
        val givenEntryData = EntryData(timestamp = givenTimestamp,
                                       headers = mapOf(),
                                       body = null,
                                       response = RamboDefaultResponse())

        // when
        givenRepository.add(givenService, givenEntryData)

        // then
        var responses = givenRepository.getAll(givenService)
        responses.let {
            it.size shouldBe 1
            it.get(0).apply {
                timestamp shouldBe givenTimestamp
                headers shouldBe mapOf()
                body shouldBe null
                response shouldBe RamboDefaultResponse()
            }
        }
    }

    @Test
    fun `should add 403 to data source a new request`() {
        // given
        val givenService = "service"
        val givenTimestamp = Instant.now()
        val givenEntryData = EntryData(timestamp = givenTimestamp,
                                       headers = mapOf(),
                                       body = null,
                                       response = RamboDefaultResponse())

        // when
        for(i in 1..403) {
            givenRepository.add(givenService, givenEntryData)
        }

        // then
        givenRepository.getAll(givenService).size shouldBe 403
    }
}
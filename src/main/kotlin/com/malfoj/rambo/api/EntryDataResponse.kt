package com.malfoj.rambo.api

import org.springframework.web.bind.annotation.RequestMethod
import java.time.Instant

data class EntryDataResponse(val timestamp: Instant,
                             val headers: Map<String, String>,
                             val body: Any?,
                             val response: String,
                             val requestMethod: RequestMethod
)

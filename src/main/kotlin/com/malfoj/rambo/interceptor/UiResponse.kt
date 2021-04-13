package com.malfoj.rambo.interceptor

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

data class UiResponse(val response: String, val entries: List<EntryData>)
package com.malfoj.rambo.interceptor

internal interface RamboResponse

internal data class RamboDefaultResponse(val name: String = "Rambo", val greeting: String = "Hello traveler!") :
        RamboResponse

internal data class RamboCustomResponse(val content: Any) : RamboResponse

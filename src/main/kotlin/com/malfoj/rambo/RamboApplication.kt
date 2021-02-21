package com.malfoj.rambo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc

@SpringBootApplication
class RamboApplication

fun main(args: Array<String>) {
    runApplication<RamboApplication>(*args)
}

package com.malfoj.rambo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RamboApplication

fun main(args: Array<String>) {
    runApplication<RamboApplication>(*args)
}

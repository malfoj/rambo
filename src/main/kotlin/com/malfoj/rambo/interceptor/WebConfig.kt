package com.malfoj.rambo.interceptor

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
private class WebConfig : WebMvcConfigurer {

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/")
                .setViewName("forward:/default.html")
        registry.addViewController("/ui/**/{y:[\\w\\-]+}")
                .setViewName("forward:/index.html")
    }
}
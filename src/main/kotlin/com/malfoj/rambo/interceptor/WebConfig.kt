package com.malfoj.rambo.interceptor

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addViewControllers(registry: ViewControllerRegistry) {
        registry.addViewController("/ui/**/{y:[\\w\\-]+}")
                .setViewName("forward:/index.html")
    }
}
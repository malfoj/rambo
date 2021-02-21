package com.malfoj.rambo.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * Ensure client-side paths redirect to index.html because client handles routing. NOTE: Do NOT use @EnableWebMvc or it will break this.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/ui/**/{y:[\\w\\-]+}")
                .setViewName("forward:/index.html");
    }

}
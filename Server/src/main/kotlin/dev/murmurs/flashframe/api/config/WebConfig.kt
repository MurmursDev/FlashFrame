package dev.murmurs.flashframe.api.config

import dev.murmurs.flashframe.api.controller.UserController
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@Import(
    UserController::class
)
class WebConfig(private val corsConfig: CorsConfig) {

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/users/**")
                    .allowedOrigins(*corsConfig.allowOrigins.toTypedArray())
                    .allowedMethods("*")
                    .allowCredentials(true)
            }
        }
    }

}

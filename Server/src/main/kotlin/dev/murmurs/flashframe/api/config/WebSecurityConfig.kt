package dev.murmurs.flashframe.api.config

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@Profile("localTest")
class WebSecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        return http.csrf { it.disable() }
            .cors { }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .oauth2ResourceServer {
                it.jwt { }
            }
            .exceptionHandling {
                it.authenticationEntryPoint { _, response, _ ->
                    response.setHeader("Access-Control-Allow-Origin", "*")
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied")
                }
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .build()
    }
}

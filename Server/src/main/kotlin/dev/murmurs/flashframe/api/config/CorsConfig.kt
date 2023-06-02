package dev.murmurs.flashframe.api.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "flashframe.cors")
data class CorsConfig(val allowOrigins: List<String>)

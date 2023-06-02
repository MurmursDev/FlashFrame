package dev.murmurs.flashframe.api

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import dev.murmurs.flashframe.api.config.AppConfig
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = [AppConfig::class])
@ConfigurationPropertiesScan(basePackageClasses = [AppConfig::class])
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}

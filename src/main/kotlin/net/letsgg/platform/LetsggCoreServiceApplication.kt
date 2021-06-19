package net.letsgg.platform

import net.letsgg.platform.config.AuthProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableConfigurationProperties(value = [AuthProperties::class])
class LetsggCoreServiceApplication

fun main(args: Array<String>) {
    runApplication<LetsggCoreServiceApplication>(*args)
}
package net.letsgg.platform

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("net.letsgg.platform.*")
class LetsggCoreServiceApplication

fun main(args: Array<String>) {
	runApplication<LetsggCoreServiceApplication>(*args)
}

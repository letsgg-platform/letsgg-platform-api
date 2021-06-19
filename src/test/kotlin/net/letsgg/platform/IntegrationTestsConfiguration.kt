package net.letsgg.platform

import net.letsgg.platform.config.ValidationConfig
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import org.springframework.test.context.TestPropertySource

@TestConfiguration
@Import(value = [ValidationConfig::class])
@TestPropertySource("classpath:application-test.properties")
@Profile("test")
class IntegrationTestsConfiguration
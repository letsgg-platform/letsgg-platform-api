package net.letsgg.platform.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.client.RestTemplate

@Configuration
class WebApiConfig {

    @Bean
    fun getRestTemplate() = RestTemplate()

    @Bean
    fun getPasswordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

}
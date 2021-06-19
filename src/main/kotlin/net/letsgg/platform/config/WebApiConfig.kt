package net.letsgg.platform.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.client.RestTemplate
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebApiConfig : WebMvcConfigurer {
    
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(CurrentUserMethodArgumentResolver())
    }
    
    @Bean
    fun getRestTemplate(): RestTemplate = RestTemplate()
    
    @Bean
    fun getPasswordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
    
    @Bean
    fun propertyConfigInDev(): PropertySourcesPlaceholderConfigurer? {
        return PropertySourcesPlaceholderConfigurer()
    }
}
package net.letsgg.platform.config.security

import net.letsgg.platform.security.LetsggUserPermission.READ
import net.letsgg.platform.security.jwt.JwtTokenVerifier
import net.letsgg.platform.security.jwt.JwtUserAuthenticationFilter
import net.letsgg.platform.service.LetsggUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder


@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: LetsggUserDetailsService,
) : WebSecurityConfigurerAdapter() {

    companion object SecurityUtils {
        private val AUTH_WHITELIST = arrayOf(
            "/v2/api-docs**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui**",
            "/swagger-ui/**",
            "/webjars/**",
            "/actuator/**"
        )
    }

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(JwtUserAuthenticationFilter(authenticationManager()))
            .addFilterAfter(JwtTokenVerifier(), JwtUserAuthenticationFilter::class.java)
            .authorizeRequests()
            .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
            .antMatchers(*AUTH_WHITELIST).permitAll()
            .antMatchers(HttpMethod.GET, "/api/**")
            .hasAuthority(READ.getPermission())
            .antMatchers(HttpMethod.POST, "/api/auth/*").permitAll()
            .antMatchers("/api/population-resource").permitAll()
            .antMatchers("/api/email-service/**").permitAll()
            .anyRequest()
            .authenticated()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(daoAuthenticationProvider())
    }

    @Bean
    fun daoAuthenticationProvider(): DaoAuthenticationProvider {
        val provider = DaoAuthenticationProvider()
        provider.setPasswordEncoder(passwordEncoder)
        provider.setUserDetailsService(userDetailsService)
        return provider
    }
}
package net.letsgg.platform.config.security

import net.letsgg.platform.config.AuthProperties
import net.letsgg.platform.exception.handler.oauth2.OAuth2AuthenticationFailureHandler
import net.letsgg.platform.exception.handler.oauth2.OAuth2AuthenticationSuccessHandler
import net.letsgg.platform.security.AppUserDetailsService
import net.letsgg.platform.security.RestAuthenticationEntryPoint
import net.letsgg.platform.security.jwt.AuthorizationTokenService
import net.letsgg.platform.security.jwt.JwtTokenVerifier
import net.letsgg.platform.security.oauth2.AppOauth2UserService
import net.letsgg.platform.security.oauth2.HttpCookieOauth2AuthorizationRequestRepository
import net.letsgg.platform.service.auth.token.JwtTokenUtilService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.*
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true, securedEnabled = true)
class SecurityConfig(
    private val passwordEncoder: PasswordEncoder,
    private val userDetailsService: AppUserDetailsService,
    private val oauth2AuthenticationSuccessHandler: OAuth2AuthenticationSuccessHandler,
    private val oauth2AuthenticationFailureHandler: OAuth2AuthenticationFailureHandler,
    private val oauth2UserService: AppOauth2UserService,
    private val httpCookieOauth2AuthorizationRequestRepository: HttpCookieOauth2AuthorizationRequestRepository,
    private val authProperties: AuthProperties,
    private val authorizationTokenService: AuthorizationTokenService,
    private val jwtTokenUtilService: JwtTokenUtilService
) : WebSecurityConfigurerAdapter() {

    companion object SecurityUtils {
        private val OPEN_API_WHITELIST = arrayOf(
            "/v3/api-docs",
            "/v3/api-docs/swagger-config",
            "/swagger-ui/**",
            "/swagger-ui.html"
        )
    }

    override fun configure(http: HttpSecurity) {
        http
            .cors().configurationSource(corsConfigurationSource())
            .and()
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling().authenticationEntryPoint(RestAuthenticationEntryPoint())
            .and()
            .authorizeRequests()
            .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
            .antMatchers(*OPEN_API_WHITELIST).permitAll()
//            .antMatchers(HttpMethod.GET, "/api/user-info/me")
//            .hasAuthority(USER_INFO.getPermission())
            .antMatchers(HttpMethod.GET, "/api/auth/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
            .antMatchers("/api/population-resource").permitAll()
            .antMatchers("/api/email-service/**").permitAll()
            .antMatchers("/api/oauth2/**").permitAll()
            .antMatchers("/auth/**", "/oauth2/**").permitAll()
            .antMatchers("/test/**").permitAll()
            .anyRequest()
            .authenticated()
            .and()
            .oauth2Login()
            .authorizationEndpoint()
            .baseUri("/oauth2/authorize")
            .authorizationRequestRepository(httpCookieOauth2AuthorizationRequestRepository)
            .and()
//            .redirectionEndpoint()
//            .baseUri("/oauth2/callback/*")
//            .and()
            .userInfoEndpoint()
            .userService(oauth2UserService)
            .and()
            .successHandler(oauth2AuthenticationSuccessHandler)
            .failureHandler(oauth2AuthenticationFailureHandler)

        http.addFilterBefore(
            JwtTokenVerifier(authProperties, authorizationTokenService, jwtTokenUtilService),
            UsernamePasswordAuthenticationFilter::class.java
        )
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

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = authProperties.corsAllowedOrigins
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf(
            ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_HEADERS,
            AUTHORIZATION, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS,
            ORIGIN, CACHE_CONTROL, CONTENT_TYPE, "Device-Type"
        )
        configuration.exposedHeaders = listOf(
            ACCESS_CONTROL_ALLOW_ORIGIN, ACCESS_CONTROL_ALLOW_HEADERS,
            AUTHORIZATION, ACCESS_CONTROL_REQUEST_METHOD, ACCESS_CONTROL_REQUEST_HEADERS,
            ORIGIN, CACHE_CONTROL, CONTENT_TYPE, "Device-Type"
        )
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun getAuthenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    @Bean
    fun getAuthenticationPrincipalMethodResolverBean(): AuthenticationPrincipalArgumentResolver =
        AuthenticationPrincipalArgumentResolver()
}

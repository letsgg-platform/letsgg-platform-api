package net.letsgg.platform.config

import net.letsgg.platform.security.CurrentUser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.Contact
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@EnableSwagger2
@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): Docket = Docket(DocumentationType.SWAGGER_2).select()
      .apis(RequestHandlerSelectors.basePackage("net.letsgg.platform.api.resource"))
        .paths(PathSelectors.any())
        .build()
        .ignoredParameterTypes(CurrentUser::class.java)
        .securitySchemes(listOf(apiKey()))
        .securityContexts(listOf(securityContext()))
        .apiInfo(openApiInfo())

    private fun openApiInfo() = ApiInfoBuilder()
      .title("Let'sGG Platform API")
        .description("")
        .contact(Contact("Roman Tupis", "https://t.me/romm1", "romantupss@gmail.com"))
        .license("")
        .licenseUrl("")
        .version("0.0.1")
        .build()

    @Bean
    fun securityContext(): SecurityContext =
        SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.any())
            .build()

    fun defaultAuth(): List<SecurityReference> {
      val authorizationScope = AuthorizationScope("global", "accessEverything")
      val authorizationScopes: Array<AuthorizationScope?> = arrayOfNulls(1)
      authorizationScopes[0] = authorizationScope
      return listOf(
        SecurityReference("JWT", authorizationScopes)
      )
    }

    private fun apiKey() = ApiKey("JWT", "Authorization", "header")

}
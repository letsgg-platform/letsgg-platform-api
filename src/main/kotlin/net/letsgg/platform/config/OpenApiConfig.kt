package net.letsgg.platform.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableSwagger2
@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): Docket = Docket(DocumentationType.SWAGGER_2).select()
        .apis(RequestHandlerSelectors.basePackage("net.letsgg.platform.webapi.controller"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(openApiInfo())

    private fun openApiInfo() = ApiInfoBuilder()
        .title("let'sGG Platform API")
        .description("")
        .contact(Contact("Roman Tupis", "https://t.me/romm1", "romantupss@gmail.com"))
        .license("")
        .licenseUrl("")
        .version("0.0.1")
        .build()
}
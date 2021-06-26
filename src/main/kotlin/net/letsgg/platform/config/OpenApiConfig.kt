package net.letsgg.platform.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import net.letsgg.platform.security.CurrentUser
import org.springdoc.core.SpringDocUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenApiConfig {

  companion object {
    init {
      SpringDocUtils.getConfig().addAnnotationsToIgnore(CurrentUser::class.java)
    }
  }

  @Bean
  fun openApi(
    @Value("\${springdoc.api.title}") title: String,
    @Value("\${springdoc.api.description}") description: String,
    @Value("\${springdoc.api.version}") version: String,
    @Value("\${springdoc.api.contact.name}") contactName: String,
    @Value("\${springdoc.api.contact.uri}") contactUri: String,
    @Value("\${springdoc.api.contact.email}") contactEmail: String,
  ): OpenAPI {
    val info = Info().apply {
      this.title = title
      this.version = version
      this.description = description
      termsOfService = ""
      license = License()
      contact = Contact()
        .name(contactName)
        .email(contactEmail)
        .url(contactUri)
    }
    return OpenAPI().apply {
      this.info = info
    }
  }
}
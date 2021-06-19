package net.letsgg.platform.api.resource

import net.letsgg.platform.security.Preauthorized
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("test")
class TestResource {


  @GetMapping
  @PreAuthorize(Preauthorized.WITH_AUTHORITY_READ)
  fun testEndpoint() = "test"
}
package net.letsgg.platform.api.resource

import net.letsgg.platform.service.newsletter.NewsletterSubscribeServiceFacade
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

/**
 * TODO. replace for email preferences settings.
 * */

@RestController
@RequestMapping("api/email-service")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
@Deprecated(level = DeprecationLevel.WARNING, message = "Will be replaced by user email preferences")
class UserEmailResource(
  private val newsletterSubscribeServiceFacade: NewsletterSubscribeServiceFacade
) {
  @PostMapping("/newsletter-signup/{userEmail}")
  fun subscribeForNewsletter(@PathVariable userEmail: String): ResponseEntity<Unit> {
    newsletterSubscribeServiceFacade.signUpForNewsletterAndSendWelcomeMail(userEmail)
    return ResponseEntity(HttpStatus.OK)
  }

  @GetMapping("/newsletter-unsubscribe-{id}")
  fun unsubscribeFromNewsletter(@PathVariable("id") emailEntryId: UUID): String {
        val messageOnSuccessfulUnsubscribe = "Unsubscribed."
        newsletterSubscribeServiceFacade.unsubscribeFromNewsletterAndSendByeMail(emailEntryId)
        return messageOnSuccessfulUnsubscribe
    }
}
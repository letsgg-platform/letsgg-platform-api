package net.letsgg.platform.webapi.controller

import net.letsgg.platform.service.NewsletterSignUpServiceFacade
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("api/email-service")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class UserEmailController(
    private val newsletterSignUpServiceFacade: NewsletterSignUpServiceFacade
) {
    @PostMapping("/newsletter-signup/{userEmail}")
    fun subscribeForNewsletter(@PathVariable userEmail: String): ResponseEntity<Unit> {
        newsletterSignUpServiceFacade.signUpForNewsletterAndSendWelcomeMail(userEmail)
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/newsletter-unsubscribe-{id}")
    fun unsubscribeFromNewsletter(@PathVariable("id") emailEntryId: UUID): String { //TODO. reconsider
        val messageOnSuccessfulUnsubscribe = "Unsubscribed."
        newsletterSignUpServiceFacade.unsubscribeFromNewsletterAndSendByeMail(emailEntryId)
        return messageOnSuccessfulUnsubscribe
    }
}
package net.letsgg.platform.webapi.controller

import net.letsgg.platform.service.NewsletterSignUpServiceFacade
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/mail-service")
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class UserMailController(
    private val newsletterSignUpServiceFacade: NewsletterSignUpServiceFacade
) {
    @PostMapping("/news-letter-signup/{userEmail}")
    fun subscribeForNewsFeed(@PathVariable userEmail: String): ResponseEntity<Unit> {
        newsletterSignUpServiceFacade.signUpForNewsLetterAndSendWelcomeMail(userEmail)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/newsletter-unsubscribe-{id}")
    fun unsubscribeFromNewsFeed(@PathVariable("id") emailEntryId: String): ResponseEntity<Unit> {
        newsletterSignUpServiceFacade.unsubscribeFromNewsFeedAndSendByeMail(emailEntryId)
        return ResponseEntity.ok().build()
    }
}
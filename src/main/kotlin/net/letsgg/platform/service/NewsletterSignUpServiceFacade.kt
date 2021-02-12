package net.letsgg.platform.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class NewsletterSignUpServiceFacade(
    private val emailSenderService: EmailSenderService,
    private val newsletterSubscribeService: NewsletterSubscribeService
) {

    fun signUpForNewsletterAndSendWelcomeMail(targetEmail: String) {
        with(targetEmail) {
            val emailEntry = newsletterSubscribeService.signUpForNewsFeed(this)
            emailSenderService.sendPostSubscribeForNewsletterEmails("let'sGG is coming. Are you ready?", emailEntry)
        }
    }

    fun unsubscribeFromNewsletterAndSendByeMail(emailEntryId: UUID) {
        newsletterSubscribeService.unsubscribeFromNewsFeed(emailEntryId)
    }
}
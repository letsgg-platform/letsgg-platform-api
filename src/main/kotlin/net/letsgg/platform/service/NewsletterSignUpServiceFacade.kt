package net.letsgg.platform.service

import org.springframework.stereotype.Service

@Service
class NewsletterSignUpServiceFacade(
    private val emailSenderService: EmailSenderService,
    private val newsletterSignUpService: NewsletterSignUpService
) {

    fun signUpForNewsLetterAndSendWelcomeMail(targetEmail: String) {
        with(targetEmail) {
            val emailEntry = newsletterSignUpService.signUpForNewsFeed(this)
            emailSenderService.sendPostSubscribeForNewsletterEmails("let'sGG is coming. Are you ready?", emailEntry)
        }
    }

    fun unsubscribeFromNewsFeedAndSendByeMail(emailEntryId: String) {
        newsletterSignUpService.unsubscribeFromNewsFeed(emailEntryId)
    }
}
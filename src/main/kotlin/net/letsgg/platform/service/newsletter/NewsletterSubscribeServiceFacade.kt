package net.letsgg.platform.service.newsletter

import net.letsgg.platform.entity.EmailEntry
import net.letsgg.platform.service.email.EmailSenderService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.util.*

@Service
@Transactional
class NewsletterSubscribeServiceFacade(
    private val emailSenderService: EmailSenderService,
    private val templateEngine: TemplateEngine,
    private val springTemplateEngine: SpringTemplateEngine,
    private val newsletterSubscribeService: NewsletterSubscribeService,
) {
    

    @Value("\${thymeleaf.email.newsletter-unsubscribe.url}")
    lateinit var newsletterUnsubscribeUrl: String

    fun signUpForNewsletterAndSendWelcomeMail(targetEmail: String) {
        with(targetEmail.toLowerCase()) {
            val emailEntry = newsletterSubscribeService.signUpForNewsFeed(this)
            sendPostSubscribeForNewsletterEmails(emailEntry)
        }
    }

    fun unsubscribeFromNewsletterAndSendByeMail(emailEntryId: UUID) {
        newsletterSubscribeService.unsubscribeFromNewsFeed(emailEntryId)
    }

    private fun sendPostSubscribeForNewsletterEmails(emailEntry: EmailEntry) {
        val inlineFiles = listOf(
            Pair("let'sGG-logo", ClassPathResource("static/images/logo_tertiary.png")),
            Pair("facebook-white", ClassPathResource("static/images/facebook-white.png")),
            Pair("twitter-white", ClassPathResource("static/images/twitter-white.png")),
        )
        val thymeLeafContext = Context().apply {
            setVariable("userEmailId", emailEntry.id)
            setVariable("newsletterUnsubscribeUrl", newsletterUnsubscribeUrl)
        }
        val content = springTemplateEngine.process("newsletter-prelaunch.html", thymeLeafContext)
        emailSenderService.sendEmailWithThymeleafTemplate(
            content, thymeLeafContext, "let'sGG is coming. Are you ready?",
            emailEntry.userEmail, true, inlineFiles
        )
    }
}
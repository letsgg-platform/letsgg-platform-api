package net.letsgg.platform.service

import net.letsgg.platform.entity.EmailEntry
import net.letsgg.platform.utility.LoggerDelegate
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class EmailSenderService(
    private val javaMailSender: JavaMailSender,
    private val templateEngine: TemplateEngine
) {

    private val log by LoggerDelegate()

    fun sendEmail(subject: String, text: String, targetEmail: String) {
        val message = SimpleMailMessage()
        with(message) {
            setSubject(subject)
            setText(text)
            setTo(targetEmail)
        }
        javaMailSender.send(message)
    }

    fun sendEmailWithThymeleafTemplate(
        content: String, thymeleafContext: Context, subject: String, targetEmail: String,
        isHtml: Boolean, inlines: List<Pair<String, ClassPathResource>>
    ) {
        val mimeMessage = javaMailSender.createMimeMessage()
        val mimeMessageHelper = MimeMessageHelper(mimeMessage, true, "UTF-8")
        mimeMessageHelper.apply {
            setText(content, isHtml)
            setTo(targetEmail)
            setSubject(subject)
            inlines.forEach {
                addInline(it.first, it.second)
            }
        }
        javaMailSender.send(mimeMessage)
        log.info("Sending mail to $targetEmail")
    }

    fun sendPostSubscribeForNewsletterEmails(subject: String, emailEntry: EmailEntry) {
        val inlineFiles = listOf(
            Pair("let'sGG-logo", ClassPathResource("static/images/logo_tertiary.png")),
            Pair("facebook-white", ClassPathResource("static/images/facebook-white.png")),
            Pair("twitter-white", ClassPathResource("static/images/twitter-white.png")),
        )
        val thymeLeafContext = Context().apply {
            setVariable("userEmailId", emailEntry.id)
        }
        val content = templateEngine.process("newsletter-prelaunch.html", thymeLeafContext)
        sendEmailWithThymeleafTemplate(content, thymeLeafContext, subject, emailEntry.userEmail, true, inlineFiles)
    }
}
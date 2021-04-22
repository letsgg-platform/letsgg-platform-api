package net.letsgg.platform.service.email

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

    fun sendEmail(simpleMailMessage: SimpleMailMessage) = javaMailSender.send(simpleMailMessage)

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
        log.info("Sending email to $targetEmail")
    }
}
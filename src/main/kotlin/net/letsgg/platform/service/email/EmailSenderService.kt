package net.letsgg.platform.service.email

import org.springframework.core.io.ClassPathResource
import org.springframework.mail.SimpleMailMessage
import org.thymeleaf.context.Context

interface EmailSenderService {
  fun sendEmail(simpleMailMessage: SimpleMailMessage)
  fun sendEmailWithThymeleafTemplate(
    content: String, thymeleafContext: Context, subject: String, targetEmail: String,
    isHtml: Boolean, inlines: List<Pair<String, ClassPathResource>>
  )
}
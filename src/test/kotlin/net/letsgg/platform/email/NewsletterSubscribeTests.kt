package net.letsgg.platform.email

import net.letsgg.platform.GlobalTestsConfig
import net.letsgg.platform.entity.EmailEntry
import net.letsgg.platform.repository.EmailEntryRepo
import net.letsgg.platform.service.NewsletterSubscribeService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.any
import org.mockito.Mockito.doReturn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.validation.ConstraintViolationException


@Import(value = [GlobalTestsConfig::class])
@ExtendWith(value = [SpringExtension::class])
@ContextConfiguration(classes = [NewsletterSubscribeService::class])
@ActiveProfiles("test")
class NewsletterSubscribeTests {

    @MockBean
    private lateinit var emailEntryRepo: EmailEntryRepo

    @Autowired
    private lateinit var newsletterSubscribeService: NewsletterSubscribeService

    @Test
    fun throwsExceptionWhenNotValidEmail() {
        assertThrows<ConstraintViolationException> {
            newsletterSubscribeService.signUpForNewsFeed("test")
        }
    }

    @Test
    fun doesNotThrowWhenEmailIsValid() {
        doReturn(EmailEntry("")).`when`(emailEntryRepo).save(any())

        assertDoesNotThrow {
            newsletterSubscribeService.signUpForNewsFeed("test@gmail.com")
        }
    }
}
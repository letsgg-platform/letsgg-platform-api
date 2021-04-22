package net.letsgg.platform

import net.letsgg.platform.entity.EmailEntry
import net.letsgg.platform.repository.EmailEntryRepo
import net.letsgg.platform.service.newsletter.NewsletterSubscribeServiceFacade
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.TransactionSystemException


@ActiveProfiles("test")
@ExtendWith(value = [SpringExtension::class])
@Import(value = [GlobalTestsConfig::class])
@SpringBootTest
class JpaBeanValidationTest(
    @Autowired private val emailEntryRepo: EmailEntryRepo
) {
    @MockBean
    lateinit var newsletterSubscribeServiceFacade: NewsletterSubscribeServiceFacade

    @Test
    fun throwsExceptionWithInvalidBean() {
        val objectToValidate = EmailEntry("testtest")

        assertThrows<TransactionSystemException> {
            emailEntryRepo.save(objectToValidate)
        }
    }
}
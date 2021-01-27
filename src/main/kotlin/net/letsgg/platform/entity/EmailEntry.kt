package net.letsgg.platform.entity

import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.validation.annotation.Validated

@Document(collection = "signedUpForNewsletter")
class EmailEntry(
    @Indexed(unique = true)
    val userEmail: String
) : BaseEntity()
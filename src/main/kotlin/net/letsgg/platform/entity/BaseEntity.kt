package net.letsgg.platform.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Transient
import java.time.Instant
import java.util.*

abstract class BaseEntity(

    @Transient
    val randomId: UUID = UUID.randomUUID()
) {

    companion object {
        private val serialVersionUID = 1L
    }

    @Id
    lateinit var id: String

    @CreatedDate
    lateinit var creationTimestamp: Instant

    @LastModifiedDate
    lateinit var modificationTimestamp: Instant
}
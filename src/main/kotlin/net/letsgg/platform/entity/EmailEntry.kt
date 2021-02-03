package net.letsgg.platform.entity

import java.util.*
import javax.persistence.Entity

@Entity
class EmailEntry(
    val userEmail: String
) : AbstractJpaPersistable<UUID>()
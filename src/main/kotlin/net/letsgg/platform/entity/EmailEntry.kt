package net.letsgg.platform.entity

import java.util.*
import javax.persistence.Entity
import javax.validation.constraints.Email

@Entity
class EmailEntry(
    @field:Email
    val userEmail: String
) : AbstractJpaPersistable<UUID>() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as EmailEntry

        if (userEmail != other.userEmail) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + userEmail.hashCode()
        return result
    }

    override fun toString(): String {
        return "${super.toString()}, userEmail='$userEmail')"
    }
}
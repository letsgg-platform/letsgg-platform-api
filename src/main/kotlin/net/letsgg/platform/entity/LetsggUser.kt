package net.letsgg.platform.entity

import net.letsgg.platform.security.LetsggUserRole
import java.time.LocalDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table
import javax.validation.constraints.Email
import javax.validation.constraints.Past
import javax.validation.constraints.Size

@Entity
@Table(name = "letsgg_user")
class LetsggUser(
    var firstName: String,
    var lastName: String,
    @field:Size(min = 3) val username: String,
    @field:Email var email: String,
    var passwordHash: String,
    @field:Past var birthdayDate: LocalDate,
    var country: String,
    @Enumerated(EnumType.STRING)
    var userRole: LetsggUserRole = LetsggUserRole.USER
) : AbstractJpaPersistable<UUID>() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as LetsggUser

        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (username != other.username) return false
        if (email != other.email) return false
        if (passwordHash != other.passwordHash) return false
        if (birthdayDate != other.birthdayDate) return false
        if (country != other.country) return false
        if (userRole != other.userRole) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + passwordHash.hashCode()
        result = 31 * result + birthdayDate.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + userRole.hashCode()
        return result
    }

    override fun toString(): String {
        return "${super.toString()}, firstName='$firstName', lastName='$lastName', username='$username', email='$email', passwordHash='$passwordHash', birthdayDate=$birthdayDate, country='$country', userRole=$userRole)"
    }
}
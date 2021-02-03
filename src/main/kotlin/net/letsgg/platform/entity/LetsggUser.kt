package net.letsgg.platform.entity

import net.letsgg.platform.security.LetsggUserRole
import java.time.LocalDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "letsgg_user")
class LetsggUser(
    var firstName: String,
    var lastName: String,
    val username: String,
    var email: String,
    var passwordHash: String,
    var birthdayDate: LocalDate,
    var country: String,
    @Enumerated(EnumType.STRING)
    var userRole: LetsggUserRole = LetsggUserRole.USER
) : AbstractJpaPersistable<UUID>() {

    override fun toString(): String {
        return "LetsggUser(firstName='$firstName', lastName='$lastName', username='$username', email='$email', passwordHash='$passwordHash', birthdayDate=$birthdayDate, country='$country', userRole=$userRole)"
    }

}
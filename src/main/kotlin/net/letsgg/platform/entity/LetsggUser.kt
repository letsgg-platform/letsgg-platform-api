package net.letsgg.platform.entity

import net.letsgg.platform.entity.type.Gender
import net.letsgg.platform.security.AppUserRole
import net.letsgg.platform.entity.type.AuthProvider
import java.time.LocalDate
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.Past
import javax.validation.constraints.Size

@Entity
@Table(
    name = "letsgg_user", uniqueConstraints = [
        UniqueConstraint(columnNames = ["username", "email"])
    ]
)
class LetsggUser(
        var name: String? = null,
        @field:Size(min = 3, max = 12) var username: String,
        @field:Email var email: String,
        @Enumerated(EnumType.STRING)
    var gender: Gender = Gender.UNDEFINED,
        var passwordHash: String,
        @Enumerated(EnumType.STRING)
    var userRole: AppUserRole = AppUserRole.UNFINISHED_SETUP_USER,
        @Enumerated(EnumType.STRING)
    val authProvider: AuthProvider,
        val authProviderId: String,
        val hasFinishedSetup: Boolean = false,
        val isEmailVerified: Boolean = false,
) : AbstractJpaPersistable<UUID>() {

    @field:Past
    var birthdayDate: LocalDate? = null
    var imageUrl: String? = null

    @field:OneToMany(fetch = FetchType.LAZY)
    @field:JoinTable(
        name = "user_spoken_language",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "locale_id")]
    )
    var spokenLanguages: List<LocaleEntity>? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as LetsggUser

        if (name != other.name) return false
        if (username != other.username) return false
        if (email != other.email) return false
        if (passwordHash != other.passwordHash) return false
        if (birthdayDate != other.birthdayDate) return false
        if (userRole != other.userRole) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + passwordHash.hashCode()
        result = 31 * result + birthdayDate.hashCode()
        result = 31 * result + userRole.hashCode()
        return result
    }

    override fun toString(): String {
        return "${super.toString()}, name='$name', username='$username', email='$email', passwordHash='$passwordHash', birthdayDate=$birthdayDate, userRole=$userRole)"
    }
}
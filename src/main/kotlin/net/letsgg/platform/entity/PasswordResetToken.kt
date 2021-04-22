package net.letsgg.platform.entity

import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.persistence.*


@Entity
class PasswordResetToken(
    @Column(unique = true, nullable = false)
    val token: String,
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REFRESH, CascadeType.MERGE])
    @JoinColumn(name = "user_id")
    val user: LetsggUser
) : AbstractJpaPersistable<UUID>() {
    

    /*
      Setting the id explicitly, so that hibernate could merge entities
      instead of doing persist with duplicate pkeys.
    */
    init {
        this.id = user.id
    }

    val expireDate: Instant = Instant.now().plus(TOKEN_EXPIRATION_IN_HOURS, ChronoUnit.HOURS)

    companion object {
        private const val TOKEN_EXPIRATION_IN_HOURS = 24L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as PasswordResetToken

        if (user != other.user) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + token.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + expireDate.hashCode()
        return result
    }
}
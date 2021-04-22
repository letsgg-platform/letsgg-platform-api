package net.letsgg.platform.security.oauth2

import net.letsgg.platform.entity.AbstractJpaPersistable
import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.security.TokenType
import org.hibernate.annotations.NaturalId
import org.springframework.security.oauth2.core.OAuth2AccessToken
import java.util.*
import javax.persistence.*

@Entity
//@Table(name = "oauth_token_info", schema = "public")
class OauthTokenInfo(
    @field:Column(unique = true, nullable = false)
    val authorizationCode: String,
    @field:Column(length = 512)
    val accessToken: String,
    @field:Column(length = 512)
    val refreshToken: String,
    val tokenType: TokenType,
    @MapsId
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REFRESH, CascadeType.MERGE])
    @JoinColumn(name = "user_id")
    val user: LetsggUser
) : AbstractJpaPersistable<UUID>() {
    
    init {
        this.id = user.id
    }
    
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        
        other as OauthTokenInfo
        
        if (authorizationCode != other.authorizationCode) return false
        if (accessToken != other.accessToken) return false
        if (refreshToken != other.refreshToken) return false
        if (user != other.user) return false
        
        return true
    }
    
    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + authorizationCode.hashCode()
        result = 31 * result + accessToken.hashCode()
        result = 31 * result + refreshToken.hashCode()
        result = 31 * result + user.hashCode()
        return result
    }
}
package net.letsgg.platform.api.mapper

import net.bytebuddy.utility.RandomString
import net.letsgg.platform.api.dto.UserDto
import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.security.oauth2.OAuth2UserInfo
import net.letsgg.platform.security.oauth2.UserAuthProvider
import net.letsgg.platform.security.oauth2.UserAuthProvider.LOCAL
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class LetsggUserMapper(
  private val passwordEncoder: PasswordEncoder
) {

  fun convert(entity: LetsggUser): UserDto {
    return UserDto(
      email = entity.email,
      fullName = entity.name,
      userName = entity.username,
    ).apply {
      hasFinishedSetup = entity.hasFinishedSetup
      isEmailVerified = entity.isEmailVerified
    }
  }

  fun convert(dto: UserDto): LetsggUser {
    return LetsggUser(
      name = dto.fullName,
      username = dto.userName,
      email = dto.email,
      passwordHash = passwordEncoder.encode(dto.password),
      authProvider = LOCAL,
      authProviderId = LOCAL.providerId
    )
  }

  fun convert(oAuth2UserInfo: OAuth2UserInfo, oauth2Provider: UserAuthProvider): LetsggUser {
    return LetsggUser(
      name = oAuth2UserInfo.getName(),
      username = oAuth2UserInfo.getLogin(),
      email = oAuth2UserInfo.getEmail(),
      passwordHash = generateRandomPasswordHash(),
      authProvider = oauth2Provider,
      authProviderId = oAuth2UserInfo.getId()
    ).apply {
      imageUrl = oAuth2UserInfo.getImageUrl()
    }
  }

  private fun generateRandomPasswordHash(): String {
    return passwordEncoder.encode(RandomString.make(32))
  }
}
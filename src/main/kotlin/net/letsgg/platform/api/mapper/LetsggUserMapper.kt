package net.letsgg.platform.api.mapper

import net.bytebuddy.utility.RandomString
import net.letsgg.platform.api.dto.AppUserResponseDto
import net.letsgg.platform.api.dto.SignUpRequest
import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.security.oauth2.OAuth2UserInfo
import net.letsgg.platform.security.oauth2.UserAuthProvider
import net.letsgg.platform.security.oauth2.UserAuthProvider.LOCAL
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class LetsggUserMapper(
  private val passwordEncoder: PasswordEncoder
) : Mapper<LetsggUser, SignUpRequest, AppUserResponseDto> {

  override fun toDto(entity: LetsggUser): AppUserResponseDto {
    return AppUserResponseDto(
      email = entity.email,
      hasFinishedSetup = entity.hasFinishedSetup,
      isEmailVerified = entity.isEmailVerified
    )
  }

  override fun toEntity(dto: SignUpRequest): LetsggUser {
    return LetsggUser(
      name = dto.name,
      username = dto.username,
      email = dto.email,
      passwordHash = passwordEncoder.encode(dto.password),
      authProvider = LOCAL,
      authProviderId = LOCAL.providerId
    )
  }

  fun toEntity(oAuth2UserInfo: OAuth2UserInfo, oauth2Provider: UserAuthProvider): LetsggUser {
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
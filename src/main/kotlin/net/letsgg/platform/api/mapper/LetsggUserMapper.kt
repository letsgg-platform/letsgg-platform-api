package net.letsgg.platform.api.mapper

import net.letsgg.platform.api.dto.UserDto
import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.entity.type.AuthProvider
import net.letsgg.platform.entity.type.AuthProvider.LOCAL
import net.letsgg.platform.security.oauth2.OAuth2UserInfo
import org.apache.commons.lang3.RandomStringUtils
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

  //TODO. consider what fields to include
  fun convert(oAuth2UserInfo: OAuth2UserInfo, oauth2Provider: AuthProvider): LetsggUser {
    return LetsggUser(
      name = oAuth2UserInfo.getName(),
      username = oAuth2UserInfo.getLogin(),
      email = oAuth2UserInfo.getEmail(),
      passwordHash = passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(32)),
      authProvider = oauth2Provider,
      authProviderId = oAuth2UserInfo.getId()
    ).apply {
      imageUrl = oAuth2UserInfo.getImageUrl()
    }
  }

  fun update(source: LetsggUser, target: LetsggUser) = target.apply {
    name = source.name
    email = source.email
    birthdayDate = source.birthdayDate
    gender = source.gender
    imageUrl = source.imageUrl
    spokenLanguages = source.spokenLanguages
  }
}
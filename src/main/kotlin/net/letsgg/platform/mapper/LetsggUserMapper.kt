package net.letsgg.platform.mapper

import net.letsgg.platform.entity.LetsggUser
import net.letsgg.platform.webapi.dto.LetsggUserResponseDto
import net.letsgg.platform.webapi.dto.LetsggUserSignupRequestDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class LetsggUserMapper(
    private val passwordEncoder: PasswordEncoder
) : Mapper<LetsggUser, LetsggUserSignupRequestDto, LetsggUserResponseDto> {

    override fun toDto(entity: LetsggUser): LetsggUserResponseDto {
        return LetsggUserResponseDto(
            entity.username,
            entity.birthdayDate,
            entity.country,
        )
    }

    override fun toEntity(dto: LetsggUserSignupRequestDto): LetsggUser {
        return LetsggUser(
            dto.firstName,
            dto.lastName,
            dto.userName,
            dto.email,
            passwordEncoder.encode(dto.password),
            dto.birthdate,
            dto.country,
        )
    }
}
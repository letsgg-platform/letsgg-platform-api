package net.letsgg.platform.service

import net.letsgg.platform.api.dto.UserDto

interface UserFinishSetupService {

  fun finishSetup(userEmail: String, updatedUserDto: UserDto): UserDto
}
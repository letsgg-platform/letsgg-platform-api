package net.letsgg.platform.service.user

import net.letsgg.platform.entity.LetsggUser

interface AppUserSettingsService {
  fun resetPassword(email: String)
  fun createPasswordResetTokenForUser(resetToken: String, user: LetsggUser)
  fun changeUserPassword(newPassword: String, resetToken: String)
}
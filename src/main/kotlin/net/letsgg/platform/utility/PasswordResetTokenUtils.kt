package net.letsgg.platform.utility

import java.util.*

object PasswordResetTokenUtils {
    
    fun generateResetPasswordToken(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}
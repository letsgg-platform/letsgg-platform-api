package net.letsgg.platform.entity

import java.util.*

object PasswordResetTokenUtils {
    
    fun generateResetPasswordToken(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }
}
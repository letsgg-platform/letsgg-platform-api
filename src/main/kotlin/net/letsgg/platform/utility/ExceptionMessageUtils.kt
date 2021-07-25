package net.letsgg.platform.utility

import java.util.*

val EMAIL_ALREADY_USED = getLocalizedMessage("email.exists.exception")
val INVALID_LOGIN_CREDENTIALS = getLocalizedMessage("user.auth.exception")
val INTERNAL_EXCEPTION = getLocalizedMessage("internal.exception")
val VALIDATION_EXCEPTION = getLocalizedMessage("validation.exception")
val OAUTH2_REDIRECT_URI_EXCEPTION = getLocalizedMessage("oauth2.redirect.uri.exception")
val OAUTH2_REDIRECT_ALREADY_COMMITTED = getLocalizedMessage("oauth2.redirect.uri.commit")

fun getLocalizedMessage(messageKey: String): String {
    return ResourceBundle.getBundle("i18n/messages", Locale.getDefault()).getString(messageKey)
}

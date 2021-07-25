package net.letsgg.platform.security

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.TYPE
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

/**
 * Used for getting currently logged-in user's email.
 * */
@Retention(RUNTIME)
@Target(allowedTargets = [VALUE_PARAMETER, TYPE])
annotation class CurrentUser

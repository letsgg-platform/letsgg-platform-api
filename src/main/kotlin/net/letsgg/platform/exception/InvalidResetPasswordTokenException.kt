package net.letsgg.platform.exception

import java.lang.RuntimeException

class InvalidResetPasswordTokenException : RuntimeException {

    companion object {
        const val DEFAULT_EXCEPTION_MESSAGE = "invalid token"
    }
    constructor() : super(DEFAULT_EXCEPTION_MESSAGE)
    constructor(msg: String) : super(msg)
    constructor(msg: String, throwable: Throwable) : super(msg, throwable)
}

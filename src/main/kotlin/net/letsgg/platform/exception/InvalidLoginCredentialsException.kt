package net.letsgg.platform.exception

import java.lang.RuntimeException

class InvalidLoginCredentialsException : RuntimeException {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)
}

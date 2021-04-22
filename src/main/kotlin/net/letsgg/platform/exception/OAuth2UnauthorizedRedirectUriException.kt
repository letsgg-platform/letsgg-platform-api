package net.letsgg.platform.exception

import kotlin.RuntimeException

class OAuth2UnauthorizedRedirectUriException : RuntimeException {


    constructor(message: String, throwable: Throwable) : super(message, throwable)

    constructor(message: String) : super(message)
}
package net.letsgg.platform.exception

import org.springframework.security.core.AuthenticationException

class OAuth2AuthenticationProcessingException : AuthenticationException {

    constructor(message: String, throwable: Throwable) : super(message, throwable)

    constructor(message: String) : super(message)
}

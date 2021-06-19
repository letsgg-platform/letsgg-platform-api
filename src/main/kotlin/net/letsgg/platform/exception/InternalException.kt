package net.letsgg.platform.exception

class InternalException: RuntimeException {

    constructor(message: String, cause: Throwable): super(message, cause)

    constructor(message: String): super(message)
}
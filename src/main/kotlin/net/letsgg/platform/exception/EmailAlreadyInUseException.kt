package net.letsgg.platform.exception

class EmailAlreadyInUseException : RuntimeException {

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)
}

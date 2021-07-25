package net.letsgg.platform.exception

import net.letsgg.platform.utility.getLocalizedMessage

class ResourceNotFoundException(message: String) : RuntimeException(message) {

    companion object {
        fun notExistsByFieldSupplier(field: String, value: String): Throwable =
            throw ResourceNotFoundException(
                String.format(
                    getLocalizedMessage("entity.not-exists.exception"),
                    field,
                    value
                )
            )
    }
}

package net.letsgg.platform.utility.constants

class LetsggConstants {

    object SecurityConstants{
        const val SECRET_KEY = "amf6luqk6veem9rn22d9298ztthxz22g"
        const val TOKEN_EXPIRE_IN_DAYS = 14L
        const val TOKEN_PREFIX = "Bearer"
        const val HEADER_STRING = "Authorization"
        const val AUTH_FILTER_PROCESS_URL = "/api/auth"
    }
}
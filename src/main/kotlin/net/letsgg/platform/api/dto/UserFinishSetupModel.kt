package net.letsgg.platform.api.dto

import net.letsgg.platform.entity.type.Gender
import java.time.Instant

data class UserFinishSetupModel(
        val residenceCountry: String, //switch to enum or consider using third-party libs
        val city: String,
        val birthDate: Instant,
        val gender: Gender,
        val spokenLanguages: List<Any>, //FIXME
        val interests: List<Any>
)
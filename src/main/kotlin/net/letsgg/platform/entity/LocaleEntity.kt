package net.letsgg.platform.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity(name = "locale")
class LocaleEntity(
  @field:Id val id: Int,
  @field:Column(name = "alpha2")
  val alpha2CountryCode: String,
  val languageName: String,
)
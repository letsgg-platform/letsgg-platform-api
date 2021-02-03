package net.letsgg.platform.mapper

interface Mapper<ENTITY, REQUEST, RESPONSE> {

    fun toDto(entity: ENTITY): RESPONSE

    fun toEntity(dto: REQUEST): ENTITY
}
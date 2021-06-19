package net.letsgg.platform.api.mapper

/**
 * Third-way mapper REQUEST -> ENTITY -> RESPONSE
 * You might have only one {@link DTO} designed for both request and response,
 * in that case use it for both REQUEST and RESPONSE.
 * */
interface Mapper<ENTITY, REQUEST, RESPONSE> {

  fun toDto(entity: ENTITY): RESPONSE

  fun toEntity(dto: REQUEST): ENTITY
}
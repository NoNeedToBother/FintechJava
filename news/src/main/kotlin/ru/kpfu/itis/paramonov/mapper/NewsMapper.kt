package ru.kpfu.itis.paramonov.mapper

import ru.kpfu.itis.paramonov.dto.News
import ru.kpfu.itis.paramonov.dto.NewsDto

class NewsMapper(
    val placeMapper: PlaceMapper
) {

    fun mapResponseToDto(responseDto: NewsDto): News {
        return News(
            id = responseDto.id,
            title = responseDto.title,
            place = responseDto.place?.let { placeMapper.mapResponseToDto(it) },
            description = responseDto.description,
            siteUrl = responseDto.siteUrl,
            favoritesCount = responseDto.favoritesCount,
            commentsCount = responseDto.commentsCount
        )
    }
}
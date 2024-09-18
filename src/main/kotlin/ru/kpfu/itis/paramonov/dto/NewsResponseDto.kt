package ru.kpfu.itis.paramonov.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponseDto(
    val results: List<NewsDto>
)

@Serializable
data class NewsDto(
    val id: Int,
    val title: String,
    val place: PlaceResponseDto?,
    val description: String,
    @SerialName("site_url")
    val siteUrl: String,
    @SerialName("favorites_count")
    val favoritesCount: Int,
    @SerialName("comments_count")
    val commentsCount: Int,
    @SerialName("publication_date")
    val publicationDate: Long,
)

@Serializable
data class PlaceResponseDto(val id: Int)
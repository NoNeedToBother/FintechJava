package ru.kpfu.itis.paramonov.dto

import kotlin.math.exp

data class News(
    val id: Int,
    val title: String,
    val place: Place?,
    val description: String,
    val siteUrl: String,
    val favoritesCount: Int,
    val commentsCount: Int,
) {
    val rating: Double by lazy {
        1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
    }
}

data class Place(
    val id: Int,
    val title: String?,
    val address: String?,
    val phoneNumber: String?
)
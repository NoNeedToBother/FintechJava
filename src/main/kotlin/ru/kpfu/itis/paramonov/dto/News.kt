package ru.kpfu.itis.paramonov.dto

import kotlin.math.exp

data class News(
    val id: Int,
    val title: String,
    val place: Int?,
    val description: String,
    val siteUrl: String,
    val favoritesCount: Int,
    val commentsCount: Int,
) {
    val rating: Double by lazy {
        1 / (1 + exp(-(favoritesCount.toDouble() / (commentsCount + 1))))
    }
}
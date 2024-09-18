package ru.kpfu.itis.paramonov.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.kpfu.itis.paramonov.mapper.NewsMapper
import ru.kpfu.itis.paramonov.dto.News
import ru.kpfu.itis.paramonov.dto.NewsDto
import ru.kpfu.itis.paramonov.dto.NewsResponseDto
import ru.kpfu.itis.paramonov.exceptions.InvalidParameterException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class NewsService(
    private val dispatcher: CoroutineDispatcher,
    private val client: HttpClient,
    private val newsMapper: NewsMapper
) {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun getNews(count: Int = 100): List<News> {
        if (count < 0) {
            throw InvalidParameterException("News count cannot be negative")
        }

        return withContext(dispatcher) {
            getNewsResponses(count).map { dto -> newsMapper.mapResponseToDto(dto) }
        }
    }

    suspend fun getMostRatedNews(count: Int, period: ClosedRange<LocalDate>, useSequences: Boolean = false): List<News> {
        if (count < 0) {
            throw InvalidParameterException("News count cannot be negative")
        }
        if (period.endInclusive.isBefore(period.start)) {
            throw InvalidParameterException("Period end before start")
        }
        return withContext(dispatcher) {
            val responses = getNewsResponses(count)
            if (useSequences) {
                responses.getMostRatedNewsWithSequences(period)
            } else responses.getMostRatedNews(period)
        }
    }

    private suspend fun getNewsResponses(count: Int): List<NewsDto> {
        val newsResponses = mutableListOf<NewsDto>()
        val totalPageCount = count.div(MAX_NEWS_PAGE_COUNT) + 1
        val pageSize = if (count < MAX_NEWS_PAGE_COUNT) {
            count.mod(MAX_NEWS_PAGE_COUNT)
        } else MAX_NEWS_PAGE_COUNT

        for (page in 1..totalPageCount) {
            val requestCount = if (page == totalPageCount && count > MAX_NEWS_PAGE_COUNT) {
                count.mod(MAX_NEWS_PAGE_COUNT)
            } else MAX_NEWS_PAGE_COUNT
            if (count != 0) {
                newsResponses.addAll(retrieveNews(pageSize = pageSize, count = requestCount, page = page))
            }
        }
        return newsResponses
    }

    private suspend fun retrieveNews(pageSize: Int, count: Int, page: Int): List<NewsDto> {
        val fields = listOf("id", "title", "place", "description", "site_url", "favorites_count", "comments_count", "publication_date")
        val textFormat = "text"
        val location = "kzn"
        val response = client.get("https://kudago.com/public-api/v1.4/news/") {
            url {
                with(parameters) {
                    append("page_size", pageSize.toString())
                    append("page", page.toString())
                    append("text_format", textFormat)
                    append("location", location)
                    append("fields", fields.joinToString(separator = ","))
                }
            }
        }
        val body = json.decodeFromString<NewsResponseDto>(response.body())
        return body.results
            .subList(0, count)
    }

    private fun List<NewsDto>.getMostRatedNews(period: ClosedRange<LocalDate>): List<News> {
        val result = mutableListOf<News>()
        for (news in this) {
            val localDateTimeValue = news.publicationDate.fromEpochSecondsToLocalDate(
                ZoneOffset.ofHours(3)
            )
            if (period.start.atStartOfDay() <= localDateTimeValue &&
                localDateTimeValue < period.endInclusive.atTime(23, 59, 59)) {
                result.add(
                    newsMapper.mapResponseToDto(news)
                )
            }
        }
        return result
            .sortedWith { news1, news2 ->
            news1.rating.compareTo(news2.rating)
            }
    }

    private fun List<NewsDto>.getMostRatedNewsWithSequences(period: ClosedRange<LocalDate>): List<News> =
        asSequence()
            .filter { dto ->
                val localDateTimeValue = dto.publicationDate.fromEpochSecondsToLocalDate(
                    ZoneOffset.ofHours(3)
                )
                period.start.atStartOfDay() <= localDateTimeValue &&
                        localDateTimeValue < period.endInclusive.atTime(24, 0)
            }
            .map { dto -> newsMapper.mapResponseToDto(dto) }
            .sortedWith { news1, news2 ->
                news1.rating.compareTo(news2.rating)
            }
            .toList()

    private fun Long.fromEpochSecondsToLocalDate(offset: ZoneOffset): LocalDateTime =
        LocalDateTime.ofEpochSecond(this, 0, offset)

    companion object {
        private const val MAX_NEWS_PAGE_COUNT = 100
    }
}

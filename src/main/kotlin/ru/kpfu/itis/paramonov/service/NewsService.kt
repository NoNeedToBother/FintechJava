package ru.kpfu.itis.paramonov.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import ru.kpfu.itis.paramonov.NewsMapper
import ru.kpfu.itis.paramonov.dto.News
import ru.kpfu.itis.paramonov.dto.NewsResponseDto
import ru.kpfu.itis.paramonov.exceptions.InvalidParameterException

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

        val news = mutableListOf<News>()
        val totalPageCount = count.div(MAX_NEWS_PAGE_COUNT) + 1
        val pageSize = if (count < MAX_NEWS_PAGE_COUNT) {
            count.mod(MAX_NEWS_PAGE_COUNT)
        } else MAX_NEWS_PAGE_COUNT

        withContext(dispatcher) {
            for (page in 1..totalPageCount) {
                val requestCount = if (page == totalPageCount && count > MAX_NEWS_PAGE_COUNT) {
                    count.mod(MAX_NEWS_PAGE_COUNT)
                } else MAX_NEWS_PAGE_COUNT
                if (count != 0) {
                    news.addAll(retrieveNews(pageSize = pageSize, count = requestCount, page = page))
                }
            }
        }
        return news
    }

    private suspend fun retrieveNews(pageSize: Int, count: Int, page: Int): List<News> {
        val fields = listOf("id", "title", "place", "description", "site_url", "favorites_count", "comments_count")
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
            .map { resp -> newsMapper.mapResponseToDto(resp) }
    }

    companion object {
        private const val MAX_NEWS_PAGE_COUNT = 100
    }
}
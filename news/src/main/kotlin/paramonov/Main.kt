package ru.kpfu.itis.paramonov

import io.ktor.client.*
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.kpfu.itis.paramonov.dsl.allNews
import ru.kpfu.itis.paramonov.mapper.NewsMapper
import ru.kpfu.itis.paramonov.mapper.PlaceMapper
import ru.kpfu.itis.paramonov.service.NewsService
import ru.kpfu.itis.paramonov.util.saveNews
import java.time.LocalDate
import kotlin.coroutines.CoroutineContext

val logger: Logger? = LoggerFactory.getLogger("Main.kt")

suspend fun main() {
    val coroutineExceptionHandler = { _: CoroutineContext, ex: Throwable ->
        logger?.error("Error!", ex) ?: Unit
    }
    val client = HttpClient()
    val placeMapper = PlaceMapper()
    val newsMapper = NewsMapper(placeMapper)

    val service = NewsService(
        dispatcher = Dispatchers.IO,
        client = client,
        newsMapper = newsMapper
    )

    val coroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler(coroutineExceptionHandler))

    coroutineScope.launch {
        val news = service.getNews(250)
        saveNews("news.csv", news)
        val mostRated = service.getMostRatedNews(150, LocalDate.of(2024, 9, 1) ..LocalDate.now())
        saveNews("rated.csv", mostRated)

        val element = mostRated.firstOrNull() { it.place != null } ?: mostRated.first()
        println(
            allNews {
                news {
                    id(element.id)
                    title(element.title)
                    description(element.description)
                    element.place?.let {
                        place {
                            id(it.id)
                            title(it.title)
                            address(it.address)
                            phoneNumber(it.phoneNumber)
                        }
                    } ?: nullElement("Place")

                    siteUrl(element.siteUrl)
                    favoritesCount(element.favoritesCount)
                    commentsCount(element.commentsCount)
                    rating(element.rating)
                }
            }
        )
    }.join()
}
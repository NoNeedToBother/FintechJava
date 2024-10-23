package ru.kpfu.itis.paramonov

import io.ktor.client.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.toList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.kpfu.itis.paramonov.mapper.NewsMapper
import ru.kpfu.itis.paramonov.mapper.PlaceMapper
import ru.kpfu.itis.paramonov.service.NewsService
import ru.kpfu.itis.paramonov.util.saveNews
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
        val channel = service.getNewsWithWorkers(500, 1)
        val news = channel.toList()
        saveNews("worker_news.csv", news)
    }.join()
}
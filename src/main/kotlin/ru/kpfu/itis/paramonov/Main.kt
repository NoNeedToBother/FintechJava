package ru.kpfu.itis.paramonov

import io.ktor.client.*
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.kpfu.itis.paramonov.service.NewsService
import kotlin.coroutines.CoroutineContext

val logger: Logger? = LoggerFactory.getLogger("Main.kt")

suspend fun main() {
    val coroutineExceptionHandler = { _: CoroutineContext, ex: Throwable ->
        logger?.error("Error!", ex) ?: Unit
    }
    val client = HttpClient()
    val newsMapper = NewsMapper()

    val service = NewsService(
        dispatcher = Dispatchers.IO,
        client = client,
        newsMapper = newsMapper
    )

    val coroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler(coroutineExceptionHandler))

    coroutineScope.launch {
        service.getNews(101)
    }.join()
}
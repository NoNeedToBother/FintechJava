package ru.kpfu.itis.paramonov.util

import ru.kpfu.itis.paramonov.dto.News
import ru.kpfu.itis.paramonov.exceptions.InvalidParameterException
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createFile
import kotlin.io.path.exists

fun saveNews(path: String, news: Collection<News>) {
    if (!path.endsWith(".csv")) {
        throw InvalidParameterException("Expected .csv format")
    }
    val filePath = Path(path)
    if (filePath.exists()) {
        throw InvalidParameterException("File already exists")
    }

    val file = filePath.createFile()
    file.bufferedWriter().use { writer ->
        writer.write("Id,Title,Place,Description,Site url,Favorites count,Comments count,Rating")
        news.forEach { news ->
            writer.newLine()
            val sb = StringBuilder()
            with(news) {
                sb.append(id).append(",").append(title.formatToCsv()).append(",")
                    .append(place).append(",").append(siteUrl.formatToCsv()).append(",")
                    .append(favoritesCount).append(",").append(commentsCount).append(",")
                    .append(rating.toString().replace(",", "."))
            }
            writer.write(sb.toString())
        }
    }
}

private fun String.formatToCsv(): String =
    if (!(contains(",") || contains("\""))) {
        this
    }
    else {
        StringBuilder("\"")
        .append(this.replace("\"", "\"\""))
        .append("\"").toString()
    }

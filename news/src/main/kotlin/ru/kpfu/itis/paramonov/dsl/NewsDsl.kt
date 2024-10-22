package ru.kpfu.itis.paramonov.dsl

@DslMarker
annotation class NewsMarker

interface Element {
    fun render(builder: StringBuilder, indent: String)
}

@NewsMarker
abstract class AbstractElement: Element {
    protected val children = arrayListOf<Element>()

    protected fun <T: Element> initElement(element: T, init: T.() -> Unit = {}): T {
        element.init()
        children.add(element)
        return element
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }
}

@NewsMarker
abstract class ObjectElement(private val name: String): AbstractElement() {

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("${indent}<${name}>\n")
        for (c in children) {
            c.render(builder, "$indent  ")
        }
        if (!builder.endsWith("\n")) {
            builder.append("\n")
        }
        builder.append("$indent</$name>\n")
    }
}

@NewsMarker
abstract class FieldElement(private val name: String, private val value: String): AbstractElement() {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("${indent}<${name}>$value</$name>\n")
    }
}

fun allNews(init: News.() -> Unit): News {
    val news = News()
    news.init()
    return news
}

class News: ObjectElement("All news") {
    fun news(init: NewsElement.() -> Unit) = initElement(NewsElement(), init)
}

class NewsElement: ObjectElement("News") {

    fun id(value: Int) = initElement(Id(value))

    fun title(value: String) = initElement(Title(value))

    fun place(init: Place.() -> Unit) = initElement(Place(), init)

    fun description(value: String) = initElement(Description(value))

    fun siteUrl(value: String) = initElement(SiteUrl(value))

    fun favoritesCount(value: Int) = initElement(FavoritesCount(value))

    fun commentsCount(value: Int) = initElement(CommentsCount(value))

    fun rating(value: Double) = initElement(Rating(value))

    fun nullElement(name: String) = initElement(NullElement(name))

}

class NullElement(name: String): FieldElement(name, "null")

class Id(value: Int): FieldElement("Id", value.toString())
class Title(value: String): FieldElement("Title", value)
class Description(value: String): FieldElement("Description", value)
class SiteUrl(value: String): FieldElement("Site URL", value)
class FavoritesCount(value: Int): FieldElement("Favorites count", value.toString())
class CommentsCount(value: Int): FieldElement("Comments count", value.toString())
class Rating(value: Double): FieldElement("Rating", value.toString())
class Address(value: String): FieldElement("Address", value)
class PhoneNumber(value: String): FieldElement("Phone number", value)

class Place: ObjectElement("Place") {
    fun id(value: Int) = initElement(Id(value))
    fun title(value: String?) = initElement(Title(value ?: "null"))
    fun address(value: String?) = initElement(Address(value ?: "null"))
    fun phoneNumber(value: String?) = initElement(PhoneNumber(value ?: "null"))
}
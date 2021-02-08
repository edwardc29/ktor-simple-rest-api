package com.carrion.edward

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        post("/") {
            val post = call.receive<String>()
            call.respondText("Received $post from the post body", ContentType.Text.Plain)
        }

        get("/library/book/bookId/checkout") {
            val bookId = call.parameters.get("bookId")
            call.respondText("You checked out the book $bookId")
        }

        get("/library/book/bookId/reserve") {
            val bookId = call.parameters.get("bookId")
            call.respondText("You reserved out the book $bookId")
        }

        get("/library/book/{bookId}") {
            val bookId = call.parameters.get("bookId")
            val book = Book(bookId!!, "Title", "Author")
            val x = arrayListOf<String>()
            val hypermediaLink = listOf<HypermediaLink> {
                HypermediaLink("http://localhost:8080/library/book/$bookId/checkout", "checkout", "GET")
                HypermediaLink("http://localhost:8080/library/book/$bookId/checkout", "reserve", "GET")
            }
            val bookResponse = BookResponse(book, hypermediaLink)
            call.respond(bookResponse)
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

data class Book(val id: String, val title: String, val author: String)
data class BookResponse(val book: Book, val links: List<HypermediaLink>)
data class HypermediaLink(val href: String, val rel: String, val type: String)
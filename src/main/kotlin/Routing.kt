package com.example

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

fun Application.configureRouting() {
    routing {
        post("/shorten") {
            val request = call.receive<ShortenRequest>()
            val shortUrl = UrlShortenerService.shortenUrl(request.longUrl)
            call.respond(ShortenResponse(shortUrl))
        }

        get("/{shortCode}") {
            val code = call.parameters["shortCode"]
            if (code != null) {
                val longUrl = UrlRepository.getUrl(code)
                if (longUrl != null) {
                    call.respondRedirect(longUrl, permanent = true)
                } else {
                    call.respondText("Short URL not found", status = HttpStatusCode.NotFound)
                }
            } else {
                call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
            }
        }
        get("/stats/{shortCode}") {
            val code = call.parameters["shortCode"]
            if (code != null) {
                val clicks = UrlRepository.getClickCount(code)
                call.respond(mapOf("shortCode" to code, "clicks" to clicks))
            } else {
                call.respondText("Bad Request", status = HttpStatusCode.BadRequest)
            }
        }
    }
}

@Serializable
data class ShortenRequest(val longUrl: String)

@Serializable
data class ShortenResponse(val shortUrl: String)




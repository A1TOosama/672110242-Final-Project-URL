package com.example

object UrlShortenerService {
    private const val BASE_URL = "http://localhost:8080/"
    private const val CODE_LENGTH = 6
    private val charset = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"

    private fun generateCode(): String =
        (1..CODE_LENGTH).map { charset.random() }.joinToString("")

    suspend fun shortenUrl(longUrl: String): String {
        var code: String
        do {
            code = generateCode()
        } while (UrlRepository.getUrl(code) != null)

        UrlRepository.saveUrl(code, longUrl)
        return BASE_URL + code
    }
}


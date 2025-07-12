package com.example

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UrlServiceTest {

    @Test
    fun `shortenUrl should return valid short url`() = runBlocking {
        val longUrl = "https://openai.com"
        val shortUrl = UrlShortenerService.shortenUrl(longUrl)

        assertNotNull(shortUrl)
        assert(shortUrl.startsWith("http://localhost:8080/"))
    }

    @Test
    fun `getUrl should return original long url`() = runBlocking {
        val longUrl = "https://github.com"
        val shortUrl = UrlShortenerService.shortenUrl(longUrl)
        val code = shortUrl.removePrefix("http://localhost:8080/")

        val retrievedUrl = UrlRepository.getUrl(code)
        assertEquals(longUrl, retrievedUrl)
    }

    @Test
    fun `click count should increment correctly`() = runBlocking {
        val longUrl = "https://example.com"
        val shortUrl = UrlShortenerService.shortenUrl(longUrl)
        val code = shortUrl.removePrefix("http://localhost:8080/")

        repeat(3) { UrlRepository.getUrl(code) }

        val clicks = UrlRepository.getClickCount(code)
        assertEquals(3, clicks)
    }

    @Test
    fun `getUrl should return null for nonexistent code`() = runBlocking {
        val url = UrlRepository.getUrl("doesNotExist")
        assertNull(url)
    }
}
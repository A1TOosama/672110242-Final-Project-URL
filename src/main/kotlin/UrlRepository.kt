package com.example

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap

object UrlRepository {
    private val storage = ConcurrentHashMap<String, String>()
    private val clickCount = ConcurrentHashMap<String, Int>()
    private val mutex = Mutex()

    suspend fun saveUrl(shortCode: String, longUrl: String) {
        mutex.withLock {
            storage[shortCode] = longUrl
            clickCount[shortCode] = 0
        }
    }

    suspend fun getUrl(shortCode: String): String? {
        mutex.withLock {
            val url = storage[shortCode]
            if (url != null) {
                clickCount[shortCode] = clickCount.getOrDefault(shortCode, 0) + 1
            }
            return url
        }
    }

    fun getClickCount(shortCode: String): Int = clickCount[shortCode] ?: 0
}
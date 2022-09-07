package dev.rohail.debuggy.interceptor

import okhttp3.Response

class ResponseExceptionWrapper(
    private val response: Response? = null,
    private val exception: Exception? = null,
    private val responseTime: String
) {

    fun getResponse(): Response? = response

    fun getException(): Exception? = exception

    fun getResponseTime(): String = responseTime

    fun isResponse(): Boolean {
        return response != null
    }

    fun isException(): Boolean {
        return exception != null
    }
}
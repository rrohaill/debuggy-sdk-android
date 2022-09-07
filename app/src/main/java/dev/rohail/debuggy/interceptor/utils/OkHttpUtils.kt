package dev.rohail.debuggy.interceptor.utils

import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException


object OkHttpUtils {

    fun stringifyHeaders(headers: Map<String, List<String>?>): String {
        val sb = StringBuilder()
        for (entry in headers.entries) {
            val value = entry.value?.mapNotNull { it.ifEmpty { null } }
            if (!value.isNullOrEmpty()) {
                appendKey(sb, entry.key)
                appendValue(sb, value)
            }
        }
        return sb.toString()
    }

    private fun appendKey(sb: StringBuilder, key: String?) {
        if (key != null) {
            sb.append('[').append(key).append("]\n")
        }
    }

    private fun appendValue(sb: StringBuilder, values: List<String?>?) {
        if (!values.isNullOrEmpty()) {
            sb.append(values.joinToString(", ")).append("\n\n")
        }
    }

    fun cloneResponse(response: Response): Response? {
        return try {
            response.newBuilder().body(response.peekBody(Long.MAX_VALUE)).build()
        } catch (ignore: IOException) {
            null
        }
    }

    fun requestToString(request: Request): String? {
        return if (request.body == null) null else try {
            val copy: Request = request.newBuilder().build()
            val buffer = Buffer()
            copy.body?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "No request data"
        }
    }

}
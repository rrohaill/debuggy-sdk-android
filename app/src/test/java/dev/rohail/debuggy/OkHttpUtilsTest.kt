package dev.rohail.debuggy

import dev.rohail.debuggy.interceptor.utils.OkHttpUtils.stringifyHeaders
import org.junit.Assert.assertEquals
import org.junit.Test

class OkHttpUtilsTest {
    @Test
    fun testStringifyWithContentHeaders() {
        val map: MutableMap<String, List<String>?> = mutableMapOf()
        map["foo"] = listOf("bar", "qux")
        assertEquals("[foo]\nbar, qux\n\n", stringifyHeaders(map))
    }

    @Test
    fun testStringifyWithoutContentHeaders() {
        val map: MutableMap<String, List<String>?> = mutableMapOf()
        map["foo"] = null
        assertEquals("", stringifyHeaders(map))
    }
}
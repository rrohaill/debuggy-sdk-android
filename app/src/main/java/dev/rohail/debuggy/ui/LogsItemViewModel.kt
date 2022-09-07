package dev.rohail.debuggy.ui

import android.content.Context
import androidx.core.content.ContextCompat
import dev.rohail.debuggy.R
import dev.rohail.debuggy.interceptor.ResponseExceptionWrapper
import okhttp3.Request
import okhttp3.Response

class LogsItemViewModel(private val context: Context,private val request: Request, private val response: ResponseExceptionWrapper) {

    private var httpVerb: String? = null

    private var httpStatusBgColor = 0
    private var httpsBadgeTintColor = 0
    private var url: String? = null
    private var contentType: String? = null

    init {
        setup()
    }

    private fun setup() {
        setupRequestValues()
        setupResponseValues()
        setupUrl()
    }


    private fun setupRequestValues() {
        this.httpVerb = request.method
        this.httpsBadgeTintColor = ContextCompat.getColor(
            context, if (request.isHttps) R.color.gray_30 else R.color.gray_70
        )
    }


    private fun setupResponseValues() {
        if (response.isResponse()) {
            val response: Response? = response.getResponse()
            this.httpStatusBgColor = ContextCompat.getColor(
                context,
                if (response?.isSuccessful == true) R.color.success_response else R.color.failed_response
            )
            this.contentType =
                response?.header("Content-Type", "Unknown")!!.split(";").toTypedArray()[0]
        } else {
            this.httpStatusBgColor = ContextCompat.getColor(
                context,
                R.color.failed_response
            )
        }
    }


    private fun setupUrl() {
        this.url = request.url.toString()
    }

    fun getHttpVerb(): String? {
        return httpVerb
    }

    fun getHttpStatusBgColor(): Int {
        return httpStatusBgColor
    }

    fun getUrl(): String? {
        return url
    }

    fun getHttpsBadgeTintColor(): Int {
        return httpsBadgeTintColor
    }

    fun getContentType(): String? {
        return contentType
    }
}
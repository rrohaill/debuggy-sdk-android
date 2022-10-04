package dev.rohail.debuggy.interceptor

import android.content.Context
import android.util.Log
import com.squareup.seismic.ShakeDetector
import dev.rohail.debuggy.interceptor.utils.OkHttpUtils.cloneResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

object DebugInterceptor : Interceptor {

    private lateinit var instance: DebugInterceptor

    private lateinit var shakeDetector: MyShakeDetector
    private val logs = mutableListOf<Pair<Request, ResponseExceptionWrapper>>()
    private var searchLog: Pair<Request, ResponseExceptionWrapper>? = null

    fun create(): DebugInterceptor {
        if (!::instance.isInitialized)
            instance = this

        return instance
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        create()
        val request = chain.request()

        val t1 = System.nanoTime()

        val response: Response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            instance.logs.add(
                Pair(
                    request,
                    ResponseExceptionWrapper(exception = e, responseTime = "")
                )
            )
            throw e
        }
        val t2 = System.nanoTime()

        val responseTime = String.format(
            Locale.getDefault(),
            "Received response for %s in %.1fms%n",
            response.request.url, (t2 - t1) / 1e6
        )

        try {
            instance.logs.add(
                Pair(
                    request,
                    ResponseExceptionWrapper(
                        response = cloneResponse(response),
                        responseTime = responseTime
                    )
                )
            )
        } catch (e: Exception) {
            Log.d("DebugInterceptor", e.localizedMessage ?: "index out of bound exception")
        }
        return response
    }

    fun getLogs(): List<Pair<Request, ResponseExceptionWrapper>> {
        return instance.logs
    }

    fun clearLogs() {
        if (::instance.isInitialized) {
            instance.logs.clear()
            instance.searchLog = null
        }
    }

    fun removeLogItem(item: Pair<Request, ResponseExceptionWrapper>) {
        instance.logs.remove(item)
    }

    fun addItem(position: Int, item: Pair<Request, ResponseExceptionWrapper>) {
        instance.logs.add(position, item)
    }

    fun setSearchLog(item: Pair<Request, ResponseExceptionWrapper>) {
        if (::instance.isInitialized)
            instance.searchLog = item
    }

    fun getSearchLog(): Pair<Request, ResponseExceptionWrapper>? =
        if (::instance.isInitialized)
            instance.searchLog
        else
            null


    fun start(context: WeakReference<Context>) {
        start(context, ShakeDetector.SENSITIVITY_HARD)
    }

    fun start(context: WeakReference<Context>, sensitivity: Int) {
        if (!instance::shakeDetector.isInitialized) {
            instance.shakeDetector = MyShakeDetector()
            instance.shakeDetector.start(context = context, sensitivity = sensitivity)
        }
    }

    fun stop() {
        if (!instance::shakeDetector.isInitialized) {
            return
        }
        instance.shakeDetector.stop()
    }

}
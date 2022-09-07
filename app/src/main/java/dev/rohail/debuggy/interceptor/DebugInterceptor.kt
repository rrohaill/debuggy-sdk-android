package dev.rohail.debuggy.interceptor

import android.content.Context
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

    fun create(): DebugInterceptor {
        if (!DebugInterceptor::instance.isInitialized)
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
            logs.add(
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

        logs.add(
            Pair(
                request,
                ResponseExceptionWrapper(
                    response = cloneResponse(response),
                    responseTime = responseTime
                )
            )
        )
        return response
    }

    fun getLogs(): List<Pair<Request, ResponseExceptionWrapper>> {
        return logs
    }

    fun start(context: WeakReference<Context>) {
        start(context, ShakeDetector.SENSITIVITY_MEDIUM)
    }

    fun start(context: WeakReference<Context>, sensitivity: Int) {
        if (!instance::shakeDetector.isInitialized) {
            shakeDetector = MyShakeDetector()
            shakeDetector.start(context = context, sensitivity = sensitivity)
        }
    }

    fun stop() {
        if (!instance::shakeDetector.isInitialized) {
            return
        }
        shakeDetector.stop()
    }
}
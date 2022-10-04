package dev.rohail.debuggy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import dev.rohail.debuggy.R
import dev.rohail.debuggy.databinding.ActivityLogDetailBinding
import dev.rohail.debuggy.interceptor.DebugInterceptor
import dev.rohail.debuggy.interceptor.ResponseExceptionWrapper
import dev.rohail.debuggy.interceptor.utils.OkHttpUtils
import okhttp3.Request

class LogDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogDetailBinding
    private lateinit var tabsAdapter: TabsAdapter

    private val interceptor: DebugInterceptor by lazy { DebugInterceptor.create() }

    private lateinit var request: Request
    private lateinit var responseExceptionWrapper: ResponseExceptionWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        interceptor.getSearchLog()?.let {
            this.request = it.first
            this.responseExceptionWrapper = it.second

            setUpTabs()
        } ?: finish()
    }

    private fun setUpTabs() {
        tabsAdapter = TabsAdapter(
            supportFragmentManager
        )
        addTab(getString(R.string.log_info), generateBasicInfo(), null)
        addTab(
            getString(R.string.log_request),
            request.headers.toMultimap().toMutableMap().apply {
                put("URL", listOf(request.url.toString()))
                put("Body", listOf(request.body?.toString() ?: ""))
            },
            OkHttpUtils.requestToString(request)
        )
        if (responseExceptionWrapper.isResponse()) {
            addTab(
                tabName = getString(R.string.log_response),
                headers = responseExceptionWrapper.getResponse()?.headers?.toMultimap()
                    ?.toMutableMap()
                    ?: mutableMapOf(),
                body = responseExceptionWrapper.getResponse()?.body?.string()
            )
        } else if (responseExceptionWrapper.isException()) {
            val errorMap: MutableMap<String, List<String>> = LinkedHashMap()
            errorMap["Error"] = listOf(responseExceptionWrapper.getException()?.message ?: "")
            addTab(getString(R.string.log_exception), errorMap, null)
        }
        binding.viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.viewPager.adapter = tabsAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun addTab(tabName: String, headers: MutableMap<String, List<String>>, body: String?) {
        if (body != null) {
            headers["Body"] = listOf(body)
        }
        if (headers.isNotEmpty()) {
            val headersFragment: HeadersFragment = HeadersFragment.newInstance(headers)
            tabsAdapter.addFragment(headersFragment, tabName)
        }
    }

    private fun generateBasicInfo(): MutableMap<String, List<String>> {
        val info: MutableMap<String, List<String>> = LinkedHashMap()
        info["Info"] = listOf(responseExceptionWrapper.getResponseTime())
        info["URL"] = listOf(request.url.toString())
        info["VERB"] = listOf(request.method)
        if (responseExceptionWrapper.isResponse()) info["Status"] = listOf(
            java.lang.String.valueOf(
                responseExceptionWrapper.getResponse()!!.code
            )
        )
        info["Cache-Request"] =
            listOf(request.cacheControl.toString())
        if (responseExceptionWrapper.isResponse()
            && responseExceptionWrapper.getResponse()?.cacheControl != null
        ) info["Cache-Response"] =
            listOf(responseExceptionWrapper.getResponse()?.cacheControl.toString())
        return info
    }

}
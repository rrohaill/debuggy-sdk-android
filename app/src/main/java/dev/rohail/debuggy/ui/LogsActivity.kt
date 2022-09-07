package dev.rohail.debuggy.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.rohail.debuggy.databinding.ActivityLogsBinding
import dev.rohail.debuggy.interceptor.DebugInterceptor
import dev.rohail.debuggy.interceptor.ResponseExceptionWrapper
import dev.rohail.debuggy.listener.OnItemClickListener
import okhttp3.Request

class LogsActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    OnItemClickListener {

    private lateinit var binding: ActivityLogsBinding
    private lateinit var adapter: LogsAdapter
    private val interceptor: DebugInterceptor by lazy { DebugInterceptor.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setUpRecyclerView()
        setUpAdapter()
        setUpSwipeToRefresh()

    }

    private fun setUpAdapter() {
        adapter = LogsAdapter(interceptor.getLogs(), this)
        binding.recyclerView.adapter = adapter
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun setUpSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        setUpAdapter()
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onItemClicked(
        request: Request?,
        responseExceptionWrapper: ResponseExceptionWrapper?,
        position: Int
    ) {
        val intent = Intent(this, LogDetailActivity::class.java)
        intent.putExtra(LogDetailActivity.LOG_POSITION, position)
        startActivity(intent)
    }
}
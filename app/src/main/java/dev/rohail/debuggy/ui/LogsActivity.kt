package dev.rohail.debuggy.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import dev.rohail.debuggy.R
import dev.rohail.debuggy.databinding.ActivityLogsBinding
import dev.rohail.debuggy.interceptor.DebugInterceptor
import dev.rohail.debuggy.interceptor.ResponseExceptionWrapper
import dev.rohail.debuggy.listener.OnItemClickListener
import okhttp3.Request
import java.util.*

class LogsActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener,
    OnItemClickListener {

    private lateinit var binding: ActivityLogsBinding
    private lateinit var adapter: LogsAdapter
    private val interceptor: DebugInterceptor by lazy { DebugInterceptor.create() }
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setUpRecyclerView()
        setUpAdapter()
        setUpSwipeToRefresh()
        setupFab()

    }

    private fun setupFab() {
        binding.fabDelete.setOnClickListener {
            interceptor.clearLogs()
            adapter.filterList(emptyList())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // below line is to get our inflater
        val inflater = menuInflater

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu)

        // below line is to get our menu item.
        val searchItem = menu.findItem(R.id.actionSearch)

        // getting search view of our item.
        searchView = searchItem.actionView as SearchView

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText)
                return false
            }
        })
        return true
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredList = mutableListOf<Pair<Request, ResponseExceptionWrapper>>()

        // running a for loop to compare elements.
        for (item in interceptor.getLogs()) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.first.url.toUrl().toString().lowercase(Locale.getDefault())
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)
    }

    private fun setUpAdapter() {
        adapter = LogsAdapter(interceptor.getLogs(), this)
        binding.recyclerView.adapter = adapter
    }

    private var itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                interceptor.getLogs().getOrNull(viewHolder.adapterPosition)?.let { item ->
                    val position = viewHolder.adapterPosition
                    interceptor.removeLogItem(item)
                    adapter.removeItem(item)
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                    // below line is to display our snackbar with action.
                    Snackbar.make(binding.recyclerView, "Deleted", Snackbar.LENGTH_LONG)
                        .setAction(
                            "Undo"
                        ) {
                            // adding on click listener to our action of snack bar.
                            // below line is to add our item to array list with a position.
                            interceptor.addItem(position, item)

                            // below line is to notify item is
                            // added to our adapter class.
                            adapter.addItem(position, item)
                            adapter.notifyItemInserted(position)
                        }.show()
                }
            }
        }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@LogsActivity)
            ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
        }
    }

    private fun setUpSwipeToRefresh() {
        binding.swipeRefresh.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        searchView.setQuery("", false)
        setUpAdapter()
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onItemClicked(
        request: Request,
        responseExceptionWrapper: ResponseExceptionWrapper,
        position: Int
    ) {
        interceptor.setSearchLog(Pair(request, responseExceptionWrapper))
        val intent = Intent(this, LogDetailActivity::class.java)
        startActivity(intent)
    }
}
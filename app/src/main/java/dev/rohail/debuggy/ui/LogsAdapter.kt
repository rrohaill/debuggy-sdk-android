package dev.rohail.debuggy.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.rohail.debuggy.databinding.LogsItemBinding
import dev.rohail.debuggy.interceptor.ResponseExceptionWrapper
import dev.rohail.debuggy.listener.OnItemClickListener
import okhttp3.Request

class LogsAdapter(
    private var logList: List<Pair<Request, ResponseExceptionWrapper>>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<LogsItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return LogsItemViewHolder(LogsItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: LogsItemViewHolder, position: Int) {
        val item = logList[position]
        val viewModel = LogsItemViewModel(
            context = holder.itemView.context,
            request = item.first,
            response = item.second
        )
        holder.drawView(viewModel = viewModel)

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClicked(item.first, item.second, position)
        }
    }

    override fun getItemCount(): Int = logList.size

    // method for filtering our recyclerview items.
    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<Pair<Request, ResponseExceptionWrapper>>) {
        // below line is to add our filtered
        // list in our logs list.
        logList = filteredList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    fun removeItem(item: Pair<Request, ResponseExceptionWrapper>) {
        val itemPosition = logList.indexOf(item)
        if (itemPosition > -1) {
            logList.toMutableList().removeAt(itemPosition)
        }
    }

    fun addItem(position: Int, item: Pair<Request, ResponseExceptionWrapper>) {
        if (logList.getOrNull(position) != item)
            logList.toMutableList().add(position, item)
    }

}
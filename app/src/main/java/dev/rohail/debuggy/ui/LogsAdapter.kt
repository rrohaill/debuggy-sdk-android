package dev.rohail.debuggy.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.rohail.debuggy.databinding.LogsItemBinding
import dev.rohail.debuggy.interceptor.ResponseExceptionWrapper
import dev.rohail.debuggy.listener.OnItemClickListener
import okhttp3.Request

class LogsAdapter(
    private val logList: List<Pair<Request, ResponseExceptionWrapper>>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<LogsItemViewHolder>() {
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

}
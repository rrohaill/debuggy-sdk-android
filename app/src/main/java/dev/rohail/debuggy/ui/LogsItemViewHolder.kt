package dev.rohail.debuggy.ui

import androidx.recyclerview.widget.RecyclerView
import dev.rohail.debuggy.databinding.LogsItemBinding

class LogsItemViewHolder(private val binding: LogsItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun drawView(viewModel: LogsItemViewModel) {
        binding.apply {
            itemHttpVerb.text = viewModel.getHttpVerb()
            itemHttpVerb.setBackgroundColor(viewModel.getHttpStatusBgColor())
            itemUrl.text = viewModel.getUrl()
            itemHttpsBadge.setColorFilter(viewModel.getHttpsBadgeTintColor())
            itemContentType.text = viewModel.getContentType()
        }
    }
}
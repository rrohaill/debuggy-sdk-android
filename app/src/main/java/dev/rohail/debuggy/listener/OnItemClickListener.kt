package dev.rohail.debuggy.listener

import dev.rohail.debuggy.interceptor.ResponseExceptionWrapper
import okhttp3.Request

interface OnItemClickListener {
    fun onItemClicked(
        request: Request,
        responseExceptionWrapper: ResponseExceptionWrapper,
        position: Int
    )
}
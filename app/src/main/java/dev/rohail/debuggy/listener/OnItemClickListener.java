package dev.rohail.debuggy.listener;

import dev.rohail.debuggy.interceptor.ResponseExceptionWrapper;
import okhttp3.Request;

public interface OnItemClickListener {
    void onItemClicked(Request request, ResponseExceptionWrapper responseExceptionWrapper, int position);
}

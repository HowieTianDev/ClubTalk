package com.howietian.clubtalk.home.event.detail.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.home.event.detail.EventTopDataHelper;
import com.howietian.clubtalk.listener.IDataBinder;

public class EventTopWebAdapter extends DelegateAdapter.Adapter<EventTopWebAdapter.EventTopWebViewHolder> implements IDataBinder<EventTopDataHelper> {

    private EventTopDataHelper mDataHelper;

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new SingleLayoutHelper();
    }

    @NonNull
    @Override
    public EventTopWebViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_web_top, parent, false);
        EventTopWebViewHolder holder = new EventTopWebViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventTopWebViewHolder holder, int position) {
        holder.fill(mDataHelper);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void bind(EventTopDataHelper data) {
        mDataHelper = data;
    }

    static class EventTopWebViewHolder extends RecyclerView.ViewHolder {
        private WebView mWebView;

        public EventTopWebViewHolder(@NonNull View itemView) {
            super(itemView);

            mWebView = itemView.findViewById(R.id.webview_top);
        }

        public void fill(EventTopDataHelper dataHelper) {
            if (dataHelper != null && dataHelper.getEvent() != null) {
                Event event = dataHelper.getEvent();
                if (!TextUtils.isEmpty(event.getUrl())) {
                    mWebView.loadUrl(event.getUrl());
                }
            }
        }
    }
}

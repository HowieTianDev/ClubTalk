package com.howietian.clubtalk.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.listener.IDataBinder;

public class SearchResultAdapter extends DelegateAdapter.Adapter<SearchResultAdapter.SearchResultViewHolder> implements IDataBinder<SearchResultDataHelper> {

    private SearchResultDataHelper mDataHelper;
    private OnSearchItemClickListener mOnSearchItemClickListener;

    public SearchResultAdapter(OnSearchItemClickListener onSearchItemClickListener) {
        mOnSearchItemClickListener = onSearchItemClickListener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        SearchResultViewHolder holder = new SearchResultViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        holder.fill(mDataHelper, position, mOnSearchItemClickListener);
    }

    @Override
    public int getItemCount() {
        if (mDataHelper == null) {
            return 0;
        }
        return mDataHelper.getCount();
    }

    @Override
    public void bind(SearchResultDataHelper data) {
        mDataHelper = data;
    }

    static class SearchResultViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.text_search_title);
        }

        public void fill(SearchResultDataHelper dataHelper, int position, final OnSearchItemClickListener listener) {

            if (dataHelper == null || dataHelper.getItem(position) == null) {
                return;
            }
            final Event event = dataHelper.getItem(position);
            mTitle.setText(event.getTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(event);
                    }
                }
            });
        }
    }

    public interface OnSearchItemClickListener {
        void onClick(Event event);
    }
}

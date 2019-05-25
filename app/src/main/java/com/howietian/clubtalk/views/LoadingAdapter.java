package com.howietian.clubtalk.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;

public class LoadingAdapter extends DelegateAdapter.Adapter<LoadingAdapter.LoadingViewHolder> {

    private Context mContext;
    private Boolean mIsLoading = true;
    private LoadingViewHolder mViewHolder;

    public LoadingAdapter(Context context) {
        mContext = context;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new SingleLayoutHelper();
    }

    @NonNull
    @Override
    public LoadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mViewHolder = new LoadingViewHolder(new LoadingView(mContext));
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LoadingViewHolder holder, int position) {
        mViewHolder.showLoading(mIsLoading);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void showLoading(boolean isLoading) {
        mIsLoading = isLoading;
        mViewHolder.showLoading(mIsLoading);
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        LoadingView mLoadingView;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            mLoadingView = (LoadingView) itemView;
        }

        public void showLoading(boolean isLoading) {
            mLoadingView.showLoading(isLoading);
        }
    }
}

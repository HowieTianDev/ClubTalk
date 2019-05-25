package com.howietian.clubtalk.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.howietian.clubtalk.R;

public class CircleTopAdapter extends DelegateAdapter.Adapter<CircleTopAdapter.CircleTopViewHolder> {

    private CircleTopListAdapter.OnTopItemClickListener mOnTopItemClickListener;

    public CircleTopAdapter(CircleTopListAdapter.OnTopItemClickListener onTopItemClickListener) {
        mOnTopItemClickListener = onTopItemClickListener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new SingleLayoutHelper();
    }

    @NonNull
    @Override
    public CircleTopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_top, parent, false);
        CircleTopViewHolder holder = new CircleTopViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CircleTopViewHolder holder, int position) {
        holder.fill(mOnTopItemClickListener);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class CircleTopViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLayoutManager;
        private CircleTopListAdapter mCircleTopListAdapter;
        private Context mContext;

        public CircleTopViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mRecyclerView = itemView.findViewById(R.id.recycler_circle_top);
        }

        private void fill(CircleTopListAdapter.OnTopItemClickListener listener) {
            if (mRecyclerView.getAdapter() == null) {
                mLayoutManager = new LinearLayoutManager(mContext);
                mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mCircleTopListAdapter = new CircleTopListAdapter(listener);

                mRecyclerView.setAdapter(mCircleTopListAdapter);
            } else {
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }

        }
    }
}

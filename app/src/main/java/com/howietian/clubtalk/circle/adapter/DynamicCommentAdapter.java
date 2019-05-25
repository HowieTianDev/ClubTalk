package com.howietian.clubtalk.circle.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.circle.CommentListDataHelper;
import com.howietian.clubtalk.listener.IDataBinder;
import com.howietian.clubtalk.views.circle.CommentWidget;

public class DynamicCommentAdapter extends RecyclerView.Adapter<DynamicCommentAdapter.DynamicCommentViewHolder> implements IDataBinder<CommentListDataHelper> {

    private CommentListDataHelper mDataHelper;
    private DynamicListAdapter.OnDynamicClickListener mOnCommentClickListener;
    private int mDynamicPosition;

    public DynamicCommentAdapter(int position, DynamicListAdapter.OnDynamicClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
        mDynamicPosition = position;
    }

    @NonNull
    @Override
    public DynamicCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_widget, parent, false);
        DynamicCommentViewHolder holder = new DynamicCommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicCommentViewHolder holder, int position) {
        holder.fill(mDataHelper, position, mDynamicPosition, mOnCommentClickListener);
    }

    @Override
    public int getItemCount() {
        if (mDataHelper == null) {
            return 0;
        }
        return mDataHelper.getCount();
    }

    @Override
    public void bind(CommentListDataHelper data) {
        mDataHelper = data;
    }

    static class DynamicCommentViewHolder extends RecyclerView.ViewHolder {
        private CommentWidget mCommentWidget;

        public DynamicCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            mCommentWidget = itemView.findViewById(R.id.comment_widget);

        }

        public void fill(final CommentListDataHelper dataHelper, final int position, final int dynamicPosition, final DynamicListAdapter.OnDynamicClickListener listener) {

            final Comment comment = dataHelper.getItem(position);

            mCommentWidget.setCommentText(comment);

            mCommentWidget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCommentItemClick(dynamicPosition, comment);
                    }
                }
            });
        }
    }


}

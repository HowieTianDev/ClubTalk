package com.howietian.clubtalk.home.event.detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.entrance.login.LoginActivity;
import com.howietian.clubtalk.home.event.detail.EventCommentDataHelper;
import com.howietian.clubtalk.home.listener.OnCommentClickListener;
import com.howietian.clubtalk.listener.IDataBinder;
import com.howietian.clubtalk.utils.TimeUtil;

import java.util.Date;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class EventCommentAdapter extends DelegateAdapter.Adapter<EventCommentAdapter.CommentBaseViewHolder> implements IDataBinder<EventCommentDataHelper> {

    private EventCommentDataHelper mDataHelper;
    private OnCommentClickListener mOnCommentClickListener;

    public EventCommentAdapter(OnCommentClickListener onCommentClickListener) {
        mOnCommentClickListener = onCommentClickListener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public CommentBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EventCommentDataHelper.TYPE_COMMENT_TITLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_title, parent, false);
            EventCommentTitleViewHolder holder = new EventCommentTitleViewHolder(view);
            return holder;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment_single, parent, false);
        EventCommentViewHolder viewHolder = new EventCommentViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentBaseViewHolder holder, int position) {
        holder.fill(mDataHelper, position, mOnCommentClickListener);
    }

    @Override
    public int getItemCount() {
        if (mDataHelper == null) {
            return 1;
        }
        return mDataHelper.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return EventCommentDataHelper.TYPE_COMMENT_TITLE;
        }
        return EventCommentDataHelper.TYPE_COMMENT_ITEN;
    }

    @Override
    public void bind(EventCommentDataHelper data) {
        mDataHelper = data;
    }


    static class CommentBaseViewHolder extends RecyclerView.ViewHolder {
        public CommentBaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void fill(EventCommentDataHelper dataHelper, int position, OnCommentClickListener listener) {

        }
    }


    static class EventCommentTitleViewHolder extends CommentBaseViewHolder {
        private TextView mCommentCountText;
        private TextView mLikeCountText;

        public EventCommentTitleViewHolder(@NonNull View itemView) {
            super(itemView);
            mCommentCountText = itemView.findViewById(R.id.text_comment_count);
            mLikeCountText = itemView.findViewById(R.id.text_like_count);
        }

        @Override
        public void fill(EventCommentDataHelper dataHelper, int position, OnCommentClickListener listener) {
            super.fill(dataHelper, position, listener);
            mCommentCountText.setText("评论（" + dataHelper.getCommentCount() + "）");
            mLikeCountText.setText("赞（" + dataHelper.getEventLikeCount() + "）");
        }
    }

    static class EventCommentViewHolder extends CommentBaseViewHolder {

        private CircleImageView mAvatarImage;
        private TextView mNicknameText;
        private TextView mCreateTimeText;
        private LinearLayout mLikeLayout;
        private TextView mLikeCountText;
        private ImageView mLikeImage;
        private TextView mContentText;

        private Context mContext;

        public EventCommentViewHolder(@NonNull View itemView) {
            super(itemView);
            findView();
            mContext = itemView.getContext();
        }

        private void findView() {
            mAvatarImage = itemView.findViewById(R.id.image_avatar);
            mNicknameText = itemView.findViewById(R.id.text_nickname);
            mCreateTimeText = itemView.findViewById(R.id.text_create_time);
            mLikeLayout = itemView.findViewById(R.id.layout_like);
            mLikeCountText = itemView.findViewById(R.id.text_like_count);
            mLikeImage = itemView.findViewById(R.id.image_like);
            mContentText = itemView.findViewById(R.id.text_content);
        }

        @Override
        public void fill(EventCommentDataHelper dataHelper, final int position, final OnCommentClickListener listener) {
            final Comment comment = dataHelper.getCommentItem(position);

            if (comment.getUser() != null) {
                if (comment.getUser().getAvatar() != null) {
                    Glide.with(mContext).load(comment.getUser().getAvatar().getUrl()).into(mAvatarImage);
                }
                mNicknameText.setText(comment.getUser().getNickName());

                if (BmobUser.isLogin()) {
                    if (comment.getLikedIdList() != null && comment.getLikedIdList().contains(BmobUser.getCurrentUser(User.class).getObjectId())) {
                        mLikeImage.setSelected(true);
                        comment.setLiked(true);
                    } else {
                        comment.setLiked(false);
                        mLikeImage.setSelected(false);
                    }
                } else {
                    mLikeImage.setSelected(false);
                    comment.setLiked(false);
                }

                Date date = TimeUtil.getSimpleDateFormat(comment.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
                String dateStr = TimeUtil.getTimeFormatText(date);
                mCreateTimeText.setText(dateStr);
                mContentText.setText(comment.getContent());

                mLikeCountText.setText("" + comment.getLikeCount());

                mLikeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BmobUser.isLogin()) {
                            if (comment.getLiked()) {
                                if (listener != null) {
                                    listener.commentCancelLike(position, comment);
                                }
                            } else {
                                if (listener != null) {
                                    listener.commentLike(position, comment);
                                }
                            }
                        } else {
                            LoginActivity.launch(mContext);
                        }
                    }
                });
            }
        }

    }
}

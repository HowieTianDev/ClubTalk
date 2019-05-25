package com.howietian.clubtalk.circle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.bean.Dynamic;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.circle.DynamicListDataHelper;
import com.howietian.clubtalk.listener.IDataBinder;
import com.howietian.clubtalk.utils.TimeUtil;
import com.howietian.clubtalk.views.circle.ClickShowMoreLayout;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class DynamicListAdapter extends DelegateAdapter.Adapter<DynamicListAdapter.DynamicViewHolder> implements IDataBinder<DynamicListDataHelper> {

    private DynamicListDataHelper mDataHelper;
    private OnDynamicClickListener mListener;

    public DynamicListAdapter(OnDynamicClickListener listener) {
        mListener = listener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public DynamicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dynamic, parent, false);
        DynamicViewHolder holder = new DynamicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DynamicViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull DynamicViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads != null && payloads.size() > 0) {
            holder.fill(mDataHelper, position, (String) payloads.get(0), mListener);
        } else {
            holder.fill(mDataHelper, position, mListener);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataHelper == null) {
            return 0;
        }

        return mDataHelper.getCount();
    }

    @Override
    public void bind(DynamicListDataHelper data) {
        mDataHelper = data;
    }

    static class DynamicViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView mAvatarImage;
        private ImageView mVipImage;
        private TextView mNicknameText;
        private TextView mCreateTimeText;
        private ClickShowMoreLayout mShowMoreLayout;

        private NineGridView mNineGridView;

        private TextView mLikeCountText;
        private ImageView mLikeImage;
        private ImageView mCommentImage;

        private RecyclerView mCommentRecycler;
        private TextView mLabelText;

        private Context mContext;

        public DynamicViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            findView();
        }

        private void findView() {
            mAvatarImage = itemView.findViewById(R.id.image_avatar);
            mVipImage = itemView.findViewById(R.id.image_vip);
            mNicknameText = itemView.findViewById(R.id.text_nickname);
            mCreateTimeText = itemView.findViewById(R.id.text_create_time);
            mShowMoreLayout = itemView.findViewById(R.id.layout_click_show_more);

            mNineGridView = itemView.findViewById(R.id.nine_grid_view);
            mLikeCountText = itemView.findViewById(R.id.text_like_count);
            mLikeImage = itemView.findViewById(R.id.image_like);
            mCommentImage = itemView.findViewById(R.id.image_comment);

            mCommentRecycler = itemView.findViewById(R.id.recycler_dynamic_comment);

            mLabelText = itemView.findViewById(R.id.text_label);

        }

        public void fill(DynamicListDataHelper dataHelper, final int position, final OnDynamicClickListener listener) {
            final Dynamic dynamic = dataHelper.getItem(position);
            if (dynamic == null) {
                return;
            }
            if (dynamic.getUser() != null) {
                User user = dynamic.getUser();
                if (user.getAvatar() != null) {
                    Glide.with(mContext).load(dynamic.getUser().getAvatar().getUrl()).into(mAvatarImage);
                } else {
                    mAvatarImage.setImageResource(R.drawable.ic_account_circle_blue_grey_100_36dp);
                }

                mNicknameText.setText(user.getNickName());

                if (user.getClub()) {
                    mVipImage.setVisibility(View.VISIBLE);
                } else {
                    mVipImage.setVisibility(View.GONE);
                }
            }

            Date date = TimeUtil.getSimpleDateFormat(dynamic.getCreatedAt(), TimeUtil.PATTERN_NORMAL);
            String dateStr = TimeUtil.getTimeFormatText(date);
            mCreateTimeText.setText(dateStr);

            mShowMoreLayout.setText(dynamic.getContent());

            if (dynamic.getImageUrlList() != null) {
                ArrayList<ImageInfo> imageInfoList = new ArrayList<>();
                for (String url : dynamic.getImageUrlList()) {
                    ImageInfo info = new ImageInfo();
                    info.setThumbnailUrl(url);
                    info.setBigImageUrl(url);
                    imageInfoList.add(info);
                }
                mNineGridView.setAdapter(new NineGridViewClickAdapter(mContext, imageInfoList));
                mNineGridView.setVisibility(View.VISIBLE);
            } else {
                mNineGridView.setVisibility(View.GONE);
            }

            mLikeCountText.setText("赞（" + dynamic.getLikeCount() + "）");

            if (BmobUser.isLogin()) {
                if (dynamic.getLikedIdList() != null && dynamic.getLikedIdList().contains(BmobUser.getCurrentUser(User.class).getObjectId())) {
                    dynamic.setLiked(true);
                } else {
                    dynamic.setLiked(false);
                }
            } else {
                dynamic.setLiked(false);
            }


            mLikeImage.setSelected(dynamic.getLiked());

            if (dynamic.getCommentDataHelper() != null && dynamic.getCommentDataHelper().getCount() > 0) {
                mCommentRecycler.setVisibility(View.VISIBLE);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                mCommentRecycler.setLayoutManager(linearLayoutManager);

                DynamicCommentAdapter adapter = new DynamicCommentAdapter(position, listener);
                adapter.bind(dynamic.getCommentDataHelper());
                mCommentRecycler.setAdapter(adapter);

            } else {
                mCommentRecycler.setVisibility(View.GONE);
            }

            mLabelText.setText(dynamic.getType());

            mLikeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onLike(dynamic.getLiked(), position, dynamic);
                    }
                }
            });

            mCommentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onComment(position, dynamic);
                    }
                }
            });

            mNicknameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onUserClick(dynamic.getUser());
                    }
                }
            });

            mAvatarImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onUserClick(dynamic.getUser());
                    }
                }
            });
        }

        public void fill(DynamicListDataHelper dataHelper, int position, String tag, OnDynamicClickListener listener) {
            Dynamic dynamic = dataHelper.getItem(position);
            if (tag.equals("like")) {
                mLikeCountText.setText("赞（" + dynamic.getLikeCount() + "）");
                mLikeImage.setSelected(dynamic.getLiked());
            } else if (tag.equals("comment")) {
                if (dynamic.getCommentDataHelper() != null && dynamic.getCommentDataHelper().getCount() > 0) {
                    mCommentRecycler.setVisibility(View.VISIBLE);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                    mCommentRecycler.setLayoutManager(linearLayoutManager);

                    DynamicCommentAdapter adapter = new DynamicCommentAdapter(position, listener);
                    adapter.bind(dynamic.getCommentDataHelper());
                    mCommentRecycler.setAdapter(adapter);

                } else {
                    mCommentRecycler.setVisibility(View.GONE);
                }
            }
        }
    }

    public interface OnDynamicClickListener {

        void onLike(boolean isLike, int position, Dynamic dynamic);

        void onComment(int position, Dynamic dynamic);

        void onCommentItemClick(int position, Comment comment);

        void onUserClick(User user);
    }
}

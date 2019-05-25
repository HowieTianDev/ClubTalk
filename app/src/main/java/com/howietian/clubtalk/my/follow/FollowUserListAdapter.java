package com.howietian.clubtalk.my.follow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.bumptech.glide.Glide;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.listener.IDataBinder;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowUserListAdapter extends DelegateAdapter.Adapter<FollowUserListAdapter.BaseViewHolder> implements IDataBinder<FollowUserListDataHelper> {

    private static final int TYPE_TOP = 0;
    private static final int TYPE_NORMAL = 1;

    private FollowUserListDataHelper mDataHelper;

    private OnFollowUserClickListener mListener;

    public FollowUserListAdapter(OnFollowUserClickListener listener) {
        mListener = listener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_single, parent, false);
        FollowUserViewHolder holder = new FollowUserViewHolder(view);
        return holder;


    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.fill(mDataHelper, position, mListener);
    }

    @Override
    public int getItemCount() {
        if (mDataHelper == null) {
            return 0;
        }
        return mDataHelper.getCount();
    }


    @Override
    public void bind(FollowUserListDataHelper data) {
        mDataHelper = data;
    }


    static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void fill(FollowUserListDataHelper dataHelper, int position, OnFollowUserClickListener listener) {

        }
    }

    static class FollowUserViewHolder extends BaseViewHolder {
        private CircleImageView mAvatarImage;
        private ImageView mVipImage;
        private TextView mNicknameText;
        private TextView mSchoolText;
        private TextView mProfileText;

        private Context mContext;

        public FollowUserViewHolder(@NonNull View itemView) {
            super(itemView);
            findView();
            mContext = itemView.getContext();
        }

        private void findView() {
            mAvatarImage = itemView.findViewById(R.id.image_avatar);
            mVipImage = itemView.findViewById(R.id.image_vip);
            mNicknameText = itemView.findViewById(R.id.text_real_name);
            mSchoolText = itemView.findViewById(R.id.text_school);
            mProfileText = itemView.findViewById(R.id.text_phone);
        }

        @Override
        public void fill(FollowUserListDataHelper dataHelper, int position, final OnFollowUserClickListener listener) {
            final User user = dataHelper.getItem(position);
            if (user != null) {
                if (user.getAvatar() != null) {
                    Glide.with(mContext).load(user.getAvatar().getUrl()).into(mAvatarImage);
                }
                if (user.getClub()) {
                    mVipImage.setVisibility(View.VISIBLE);
                } else {
                    mVipImage.setVisibility(View.GONE);
                }
                mNicknameText.setText(user.getNickName());
                mSchoolText.setText(user.getSchool());
                mProfileText.setText(user.getProfile());

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onClickUser(user);
                        }
                    }
                });
            }
        }
    }

    public interface OnFollowUserClickListener {
        void onClickUser(User user);
    }

}

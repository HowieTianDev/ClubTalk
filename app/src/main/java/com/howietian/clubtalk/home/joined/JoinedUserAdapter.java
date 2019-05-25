package com.howietian.clubtalk.home.joined;

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

public class JoinedUserAdapter extends DelegateAdapter.Adapter<JoinedUserAdapter.BaseViewHolder> implements IDataBinder<JoinedUserDataHelper> {

    private static final int TYPE_TOP = 0;
    private static final int TYPE_NORMAL = 1;

    private JoinedUserDataHelper mDataHelper;

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TOP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_top_count, parent, false);
            JoinedUserTopViewHolder holder = new JoinedUserTopViewHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_single, parent, false);
            JoinedViewHolder holder = new JoinedViewHolder(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.fill(mDataHelper, position);
    }

    @Override
    public int getItemCount() {
        if (mDataHelper == null) {
            return 0;
        }
        return mDataHelper.getCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOP;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public void bind(JoinedUserDataHelper data) {
        mDataHelper = data;
    }


    static class BaseViewHolder extends RecyclerView.ViewHolder {

        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void fill(JoinedUserDataHelper dataHelper, int position) {

        }
    }

    static class JoinedUserTopViewHolder extends BaseViewHolder {

        private TextView mJoinedCountText;

        public JoinedUserTopViewHolder(@NonNull View itemView) {
            super(itemView);
            mJoinedCountText = itemView.findViewById(R.id.text_joined_count);
        }


        @Override
        public void fill(JoinedUserDataHelper dataHelper, int position) {
            super.fill(dataHelper, position);
            mJoinedCountText.setText("已报名人数：" + dataHelper.getCount());
        }
    }


    static class JoinedViewHolder extends BaseViewHolder {
        private CircleImageView mAvatarImage;
        private ImageView mVipImage;
        private TextView mRealNameText;
        private TextView mSchoolText;
        private TextView mPhoneText;

        private Context mContext;

        public JoinedViewHolder(@NonNull View itemView) {
            super(itemView);
            findView();
            mContext = itemView.getContext();
        }

        private void findView() {
            mAvatarImage = itemView.findViewById(R.id.image_avatar);
            mVipImage = itemView.findViewById(R.id.image_vip);
            mRealNameText = itemView.findViewById(R.id.text_real_name);
            mSchoolText = itemView.findViewById(R.id.text_school);
            mPhoneText = itemView.findViewById(R.id.text_phone);
        }

        public void fill(JoinedUserDataHelper dataHelper, int position) {
            User user = dataHelper.getItem(position);
            if (user != null) {
                if (user.getAvatar() != null) {
                    Glide.with(mContext).load(user.getAvatar().getUrl()).into(mAvatarImage);
                }
                if (user.getClub()) {
                    mVipImage.setVisibility(View.VISIBLE);
                } else {
                    mVipImage.setVisibility(View.GONE);
                }
                mRealNameText.setText(user.getRealName());
                mSchoolText.setText(user.getSchool());
                mPhoneText.setText(user.getMobilePhoneNumber());
            }
        }
    }

}

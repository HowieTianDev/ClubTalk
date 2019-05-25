package com.howietian.clubtalk.home.event.detail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.bumptech.glide.Glide;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.home.event.detail.EventTopDataHelper;
import com.howietian.clubtalk.listener.IDataBinder;
import com.howietian.clubtalk.utils.TimeUtil;

import java.util.Date;

import cn.bmob.v3.BmobUser;

public class EventTopTextAdapter extends DelegateAdapter.Adapter<EventTopTextAdapter.EventTopViewHolder> implements IDataBinder<EventTopDataHelper> {

    private EventTopDataHelper mEventTopDataHelper;
    private OnTopButtonClickListener mOnTopButtonClickListener;

    public EventTopTextAdapter(OnTopButtonClickListener onTopButtonClickListener) {
        mOnTopButtonClickListener = onTopButtonClickListener;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new SingleLayoutHelper();
    }

    @NonNull
    @Override
    public EventTopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_top, parent, false);
        EventTopViewHolder viewHolder = new EventTopViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventTopViewHolder holder, int position) {
        holder.fill(mEventTopDataHelper, mOnTopButtonClickListener);
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void bind(EventTopDataHelper data) {
        mEventTopDataHelper = data;
    }

    static class EventTopViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        private TextView mTitleText;
        private TextView mCreateTimeText;
        private TextView mUserNameText;
        private TextView mStatusText;
        private TextView mDeadLineText;
        private ImageView mCoverImage;
        private TextView mOrganizerText;
        private TextView mPlaceText;
        private TextView mStartTimeText;
        private TextView mPhoneText;
        private TextView mContentText;
        private Button mJoinBtn;


        public EventTopViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            findView();
        }

        private void findView() {
            mTitleText = itemView.findViewById(R.id.text_title);
            mCreateTimeText = itemView.findViewById(R.id.text_create_time);
            mUserNameText = itemView.findViewById(R.id.text_username);
            mStatusText = itemView.findViewById(R.id.text_status);
            mDeadLineText = itemView.findViewById(R.id.text_deadline);
            mCoverImage = itemView.findViewById(R.id.image_cover);
            mOrganizerText = itemView.findViewById(R.id.text_organizer);
            mPlaceText = itemView.findViewById(R.id.text_place);
            mStartTimeText = itemView.findViewById(R.id.text_start_time);
            mPhoneText = itemView.findViewById(R.id.text_phone);
            mContentText = itemView.findViewById(R.id.text_content);
            mJoinBtn = itemView.findViewById(R.id.btn_join);

        }

        private void fill(EventTopDataHelper dataHelper, final OnTopButtonClickListener listener) {
            final Event event = dataHelper.getEvent();
            if (event != null) {
                User user = BmobUser.getCurrentUser(User.class);

                mTitleText.setText(event.getTitle());

                Date date = TimeUtil.getSimpleDateFormat(event.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
                String dateStr = TimeUtil.formatDate(date, "yyyy-MM-dd");
                mCreateTimeText.setText(dateStr);

                mUserNameText.setText(event.getNickname());

                String status = TimeUtil.getJoinStatus(event);
                mStatusText.setText(status);
                mDeadLineText.setText(event.getDeadLine());
                if (event.getCover() != null) {
                    Glide.with(mContext).load(event.getCover().getUrl()).into(mCoverImage);
                }

                mOrganizerText.setText(event.getOrganizer());
                mPlaceText.setText(event.getPlace());
                mStartTimeText.setText(event.getStartTime());
                mPhoneText.setText(event.getPhone());
                mContentText.setText(event.getContent());

                if (BmobUser.isLogin() && event.getUser() != null && event.getUser().getObjectId().equals(user.getObjectId())) {
                    mJoinBtn.setText("查看报名情况");
                    mJoinBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.viewJoinInfo(event);
                            }
                        }
                    });
                } else {
                    if (status.equals(TimeUtil.JOINED)) {
                        mJoinBtn.setText(TimeUtil.JOINED);
                        mJoinBtn.setEnabled(false);
                    } else if (status.equals(TimeUtil.END_JOIN)) {
                        mJoinBtn.setText(TimeUtil.END_JOIN);
                        mJoinBtn.setEnabled(false);
                    } else if (status.equals(TimeUtil.ON_JOIN)) {
                        mJoinBtn.setText("我要报名");
                        mJoinBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listener != null) {
                                    listener.joinEvent(event);
                                }
                            }
                        });
                    }
                }

            }
        }

    }

    public interface OnTopButtonClickListener {
        void joinEvent(Event event);

        void viewJoinInfo(Event event);
    }
}

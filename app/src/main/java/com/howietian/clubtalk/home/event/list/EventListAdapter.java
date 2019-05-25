package com.howietian.clubtalk.home.event.list;

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
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.listener.IDataBinder;
import com.howietian.clubtalk.utils.TimeUtil;

import java.util.Date;

public class EventListAdapter extends DelegateAdapter.Adapter<EventListAdapter.EventViewHolder> implements IDataBinder<EventDataHelper> {

    private Context mContext;
    private OnEventItemClickListener mOnEventItemClickListener;
    private EventDataHelper mEventDataHelper;
    private LinearLayoutHelper mLinearLayoutHelper;

    public EventListAdapter(Context context, OnEventItemClickListener listItemClickListener) {
        mContext = context;
        mOnEventItemClickListener = listItemClickListener;
        mLinearLayoutHelper = new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home_list, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.fill(mEventDataHelper, position, mOnEventItemClickListener);
    }

    @Override
    public int getItemCount() {
        if (mEventDataHelper == null) {
            return 0;
        }
        return mEventDataHelper.getCount();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLinearLayoutHelper;
    }

    @Override
    public void bind(EventDataHelper data) {
        mEventDataHelper = data;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        private ImageView mCoverImage;
        private TextView mTitleText;
        private LinearLayout mStatusLayout;
        private TextView mStatusText;
        private TextView mCommentCountText;
        private TextView mCreateTimeText;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mCoverImage = itemView.findViewById(R.id.image_cover);
            mTitleText = itemView.findViewById(R.id.text_title);
            mStatusText = itemView.findViewById(R.id.text_status);
            mCommentCountText = itemView.findViewById(R.id.text_comment_count);
            mCreateTimeText = itemView.findViewById(R.id.text_create_time);
            mStatusLayout = itemView.findViewById(R.id.layout_status);
        }

        public void fill(EventDataHelper dataHelper, int position, final OnEventItemClickListener listener) {
            final Event event = dataHelper.getItem(position);

            Date date = TimeUtil.getSimpleDateFormat(event.getCreatedAt(), "yyyy-MM-dd HH:mm:ss");
            String dateText = TimeUtil.getTimeFormatText(date);

            if (event.getCover() != null) {
                Glide.with(mContext).load(event.getCover().getUrl()).into(mCoverImage);
            }
            mCreateTimeText.setText(dateText);
            mCommentCountText.setText(event.getCommentCount() + "条评论");
            mTitleText.setText(event.getTitle());

            if (event.getType() == Event.TYPE_ELEGANT) {
                mStatusLayout.setVisibility(View.GONE);
            } else {
                mStatusLayout.setVisibility(View.VISIBLE);
                mStatusText.setText(TimeUtil.getJoinStatus(event));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClick(event);
                    }
                }
            });
        }
    }

    public interface OnEventItemClickListener {
        void onClick(Event event);
    }
}

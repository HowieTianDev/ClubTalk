package com.howietian.clubtalk.home.event.detail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.entrance.login.LoginActivity;
import com.howietian.clubtalk.home.event.detail.adapter.EventCommentAdapter;
import com.howietian.clubtalk.home.event.detail.adapter.EventTopTextAdapter;
import com.howietian.clubtalk.home.event.detail.adapter.EventTopWebAdapter;
import com.howietian.clubtalk.home.joined.JoinedUserActivity;
import com.howietian.clubtalk.home.listener.OnBottomClickListener;
import com.howietian.clubtalk.home.listener.OnCommentClickListener;
import com.howietian.clubtalk.views.CommentDialog;

import cn.bmob.v3.BmobUser;

public class EventDetailActivity extends BaseActivity<EventDetailViewModel> implements View.OnClickListener,
        EventTopTextAdapter.OnTopButtonClickListener, OnBottomClickListener, OnCommentClickListener {


    public static void launch(Context context) {
        Intent intent = new Intent(context, EventDetailActivity.class);
        context.startActivity(intent);
    }

    public static void launch(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static final String TAG_EVENT_DETAIL = "event_detail";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private TextView mCommentText;
    private ImageView mLikeImage;
    private ImageView mCollectImage;

    private VirtualLayoutManager mLayoutManager;
    private DelegateAdapter mAdapter;
    private EventTopTextAdapter mEventTopAdapter;
    private EventTopWebAdapter mEventTopWebAdapter;
    private EventCommentAdapter mEventCommentAdapter;

    private Event mEvent;
    private int mType;

    private boolean mIsLike;
    private boolean mIsCollect;

    private User mUser = BmobUser.getCurrentUser(User.class);


    @Override
    protected int getLayoutId() {
        return R.layout.activity_event_detail;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readArguments();

        findView();
        initRecyclerView();
        initBottomBar();

        observe();
        getViewModel().start(mEvent);
    }

    private void readArguments() {
        if (getIntent() != null) {
            String eventJson = getIntent().getStringExtra(TAG_EVENT_DETAIL);
            mEvent = new Gson().fromJson(eventJson, Event.class);
            mType = mEvent.getType();
            if (mEvent == null) {
                showToast("系统错误");
                finish();
            }
        }
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_event_detail);
        setSupportActionBar(mToolbar);
        if (mType == Event.TYPE_ELEGANT || mType == Event.TYPE_BANNER) {
            mToolbar.setTitle("精选");
        } else if (mType == Event.TYPE_EVENT) {
            mToolbar.setTitle("活动");
        }
        mRecyclerView = findViewById(R.id.recycler_event_detail);
        mCommentText = findViewById(R.id.text_comment);
        mLikeImage = findViewById(R.id.image_like);
        mCollectImage = findViewById(R.id.image_collect);

        mCommentText.setOnClickListener(this);
        mLikeImage.setOnClickListener(this);
        mCollectImage.setOnClickListener(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        mLayoutManager = new VirtualLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DelegateAdapter(mLayoutManager);

        if (mType == Event.TYPE_EVENT || mType == Event.TYPE_PERSON_PAGE) {
            mEventTopAdapter = new EventTopTextAdapter(this);
            getViewModel().bindTopData(mEventTopAdapter);
            mAdapter.addAdapter(mEventTopAdapter);
        } else if (mType == Event.TYPE_ELEGANT || mType == Event.TYPE_BANNER) {
            mEventTopWebAdapter = new EventTopWebAdapter();
            getViewModel().bindTopData(mEventTopWebAdapter);
            mAdapter.addAdapter(mEventTopWebAdapter);
        }

        mEventCommentAdapter = new EventCommentAdapter(this);
        getViewModel().bindCommentData(mEventCommentAdapter);
        mAdapter.addAdapter(mEventCommentAdapter);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void initBottomBar() {
        if (BmobUser.isLogin()) {
            String userId = mUser.getObjectId();
            if (mEvent.getLikedIdList() != null) {
                if (mEvent.getLikedIdList().contains(userId)) {
                    mIsLike = true;
                    mLikeImage.setSelected(true);
                }
            }

            if (mEvent.getCollectedIdList() != null) {
                if (mEvent.getCollectedIdList().contains(userId)) {
                    mIsCollect = true;
                    mCollectImage.setSelected(true);
                }
            }
        }

    }

    private void observe() {
        getViewModel().getNotifyTop().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                if (mType == Event.TYPE_EVENT || mType == Event.TYPE_PERSON_PAGE) {
                    mEventTopAdapter.notifyDataSetChanged();
                }

                if (mType == Event.TYPE_ELEGANT || mType == Event.TYPE_BANNER) {
                    mEventTopWebAdapter.notifyDataSetChanged();
                }
            }
        });

        getViewModel().getNotifyComment().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                mEventCommentAdapter.notifyDataSetChanged();
            }
        });

        getViewModel().getNotifyCommentItem().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mEventCommentAdapter.notifyItemChanged(integer, "tag");
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == mCommentText) {
            showCommentDialog();
        } else if (v == mCollectImage) {
            collectClick();
        } else if (v == mLikeImage) {
            likeClick();
        }
    }

    private void likeClick() {
        if (BmobUser.isLogin()) {
            getViewModel().like(!mIsLike, mEvent, mUser, this);
        } else {
            showToast("请先登录哦~");
            LoginActivity.launch(this);
        }
    }

    private void collectClick() {
        if (BmobUser.isLogin()) {
            getViewModel().collect(!mIsCollect, mEvent, mUser, this);
        } else {
            showToast("请先登录哦~");
            LoginActivity.launch(this);
        }
    }

    private void showCommentDialog() {
        if (BmobUser.isLogin()) {
            CommentDialog commentDialog = new CommentDialog(this, true);
            commentDialog.setOnSubmitCommentListener(new CommentDialog.OnSubmitCommentListener() {
                @Override
                public void onSubmitComment(String text) {
                    getViewModel().comment(text, mEvent, mUser, EventDetailActivity.this);
                }
            });
            commentDialog.show();
        } else {
            showToast("请先登录哦~");
            LoginActivity.launch(this);
        }
    }


    @Override
    public void joinEvent(Event event) {
        if (BmobUser.isLogin()) {
            String school = mUser.getSchool();
            String phone = mUser.getMobilePhoneNumber();
            String realName = mUser.getRealName();
            // 活动参与者
            if (!TextUtils.isEmpty(school) && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(realName)) {
                showJoinDialog();
            } else {
                showToast("请先在个人中心完善真实信息");
            }
        } else {
            showToast("请先登录哦~");
            LoginActivity.launch(this);
        }
    }

    private void showJoinDialog() {
        if (BmobUser.isLogin()) {
            String msg = "姓名：" + mUser.getRealName() + "\n学校：" + mUser.getSchool() + "\n联系方式：" + mUser.getMobilePhoneNumber();

            AlertDialog joinDialog = new AlertDialog.Builder(this)
                    .setTitle("请确认报名信息准确无误")
                    .setMessage(msg)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getViewModel().joinEvent(mEvent, mUser);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create();
            joinDialog.show();
        }
    }


    @Override
    public void viewJoinInfo(Event event) {
        // 去报名详情界面
        Intent intent = new Intent(this, JoinedUserActivity.class);
        intent.putExtra(JoinedUserActivity.TAG_EVENT, new Gson().toJson(mEvent));
        JoinedUserActivity.launch(this, intent);
    }

    @Override
    public void onLikeSucceed(int likeCount) {
        mLikeImage.setSelected(true);
        mIsLike = true;
        getViewModel().notifyCommentTitle("like", likeCount);
    }

    @Override
    public void onLikeFail() {
        mLikeImage.setSelected(false);
        mIsLike = false;
    }

    @Override
    public void cancelLikeSucceed(int likeCount) {
        mLikeImage.setSelected(false);
        mIsLike = false;
        getViewModel().notifyCommentTitle("like", likeCount);
    }

    @Override
    public void cancelLikeFail() {
        mLikeImage.setSelected(true);
        mIsLike = true;
    }

    @Override
    public void onCollectSucceed() {
        mCollectImage.setSelected(true);
        mIsCollect = true;
    }

    @Override
    public void onCollectFail() {
        mCollectImage.setSelected(false);
        mIsCollect = false;
    }

    @Override
    public void cancelCollectSucceed() {
        mCollectImage.setSelected(false);
        mIsCollect = false;
    }

    @Override
    public void cancelCollectFail() {
        mCollectImage.setSelected(true);
        mIsCollect = true;
    }

    @Override
    public void onCommentSucceed(Comment comment, int commentCount) {
        // 插入评论
        getViewModel().insertComment(comment);
        showToast("提交成功");
    }

    @Override
    public void commentLike(int position, Comment comment) {
        getViewModel().commentLike(position, comment, mUser);
    }

    @Override
    public void commentCancelLike(int position, Comment comment) {
        getViewModel().commentCancelLike(position, comment, mUser);
    }
}

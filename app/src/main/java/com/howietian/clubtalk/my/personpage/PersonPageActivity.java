package com.howietian.clubtalk.my.personpage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.adapter.PageAdapter;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.circle.CircleFragment;
import com.howietian.clubtalk.entrance.login.LoginActivity;
import com.howietian.clubtalk.home.event.list.EventListFragment;
import com.howietian.clubtalk.my.follow.FollowUserListActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonPageActivity extends BaseActivity<PersonPageViewModel> implements View.OnClickListener {

    public static final String TAG_PERSON_PAGE = "person_page";

    public static void launch(Context context, User user) {
        if (user == null) {
            return;
        }
        Intent intent = new Intent(context, PersonPageActivity.class);
        intent.putExtra(TAG_PERSON_PAGE, new Gson().toJson(user));
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CircleImageView mAvatarImage;
    private ImageView mVipImage;
    private TextView mNicknameText;
    private TextView mProfileText;
    private TextView mFollowText;
    private TextView mFollowCountText;
    private TextView mFanCountText;
    private LinearLayout mFollowCountLayout;
    private LinearLayout mFanCountLayout;

    private User mTargetUser;

    private CircleFragment mCircleFragment;
    private EventListFragment mEventListFragment;
    private PageAdapter mPageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readArguments();
        findView();

        initView();
        observe();

        User currentUser = BmobUser.getCurrentUser(User.class);
        getViewModel().start(mTargetUser, currentUser);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person_page;
    }

    private void readArguments() {
        if (getIntent() != null) {
            String userJson = getIntent().getStringExtra(TAG_PERSON_PAGE);
            mTargetUser = new Gson().fromJson(userJson, User.class);
            if (mTargetUser == null) {
                showToast("系统错误");
                return;
            }
        }
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_person_page);
        setSupportActionBar(mToolbar);

        mViewPager = findViewById(R.id.viewpager_person);
        mTabLayout = findViewById(R.id.tabLayout_person_page);
        mAvatarImage = findViewById(R.id.image_avatar);
        mVipImage = findViewById(R.id.image_vip);
        mNicknameText = findViewById(R.id.text_nickname);
        mProfileText = findViewById(R.id.text_profile);
        mFollowCountText = findViewById(R.id.text_follow_count);
        mFanCountText = findViewById(R.id.text_fan_count);
        mFollowText = findViewById(R.id.text_follow);

        mFollowCountLayout = findViewById(R.id.layout_follow_count);
        mFanCountLayout = findViewById(R.id.layout_fan_count);


        mFollowText.setOnClickListener(this);
        mFollowCountLayout.setOnClickListener(this);
        mFanCountLayout.setOnClickListener(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        List<String> titles = new ArrayList<>();
        titles.add("活动");
        titles.add("动态");

        List<Fragment> fragmentList = new ArrayList<>();
        if (mCircleFragment == null) {
            mCircleFragment = new CircleFragment();
            Bundle bundle = new Bundle();
            bundle.putString(EventListFragment.TAG_TARGET_USER, new Gson().toJson(mTargetUser));
            mCircleFragment.setArguments(bundle);
        }
        if (mEventListFragment == null) {
            mEventListFragment = new EventListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(EventListFragment.TAG_TYPE, Event.TYPE_PERSON_PAGE);
            bundle.putString(EventListFragment.TAG_TARGET_USER, new Gson().toJson(mTargetUser));
            mEventListFragment.setArguments(bundle);
        }
        fragmentList.add(mEventListFragment);
        fragmentList.add(mCircleFragment);

        mPageAdapter = new PageAdapter(getSupportFragmentManager(), fragmentList, titles);
        mViewPager.setAdapter(mPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void observe() {
        getViewModel().getNotifyAvatar().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    Glide.with(PersonPageActivity.this).load(s).into(mAvatarImage);
                }
            }
        });

        getViewModel().getNotifyIsClub().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mVipImage.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });

        getViewModel().getNotifyFanCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mFanCountText.setText(String.valueOf(integer));
            }
        });

        getViewModel().getNotifyFollowCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mFollowCountText.setText(String.valueOf(integer));
            }
        });

        getViewModel().getNotifyIsFollow().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == 0) {
                    mFollowText.setVisibility(View.GONE);
                } else if (integer == 1) {
                    mFollowText.setVisibility(View.VISIBLE);
                    mFollowText.setSelected(false);
                    mFollowText.setText("关注");
                } else if (integer == 2) {
                    mFollowText.setVisibility(View.VISIBLE);
                    mFollowText.setSelected(true);
                    mFollowText.setText("已关注");
                }
            }
        });

        getViewModel().getNotifyNickname().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mNicknameText.setText(s);
            }
        });

        getViewModel().getNotifyProfile().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mProfileText.setText(s);
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == mFollowCountLayout) {
            if (isSelf()) {
                FollowUserListActivity.launch(this, mTargetUser, FollowUserListActivity.TYPE_MY_FOLLOW);
            } else {
                FollowUserListActivity.launch(this, mTargetUser, FollowUserListActivity.TYPE_OTHER_FOLLOW);
            }

        } else if (v == mFanCountLayout) {
            if (isSelf()) {
                FollowUserListActivity.launch(this, mTargetUser, FollowUserListActivity.TYPE_MY_FAN);
            } else {
                FollowUserListActivity.launch(this, mTargetUser, FollowUserListActivity.TYPE_OTHER_FAN);
            }
        } else if (v == mFollowText) {
            follow();
        }
    }

    private boolean isSelf() {
        if (BmobUser.isLogin() && mTargetUser != null) {
            if (BmobUser.getCurrentUser(User.class).getObjectId().equals(mTargetUser.getObjectId())) {
                return true;
            }
        }

        return false;
    }

    private void follow() {
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            if (user.getFollowIdList() != null && user.getFollowIdList().contains(mTargetUser.getObjectId())) {
                getViewModel().cancelFollow(mTargetUser, user);
            } else {
                getViewModel().follow(mTargetUser, user);
            }
        } else {
            showToast("请先登录哦~");
            LoginActivity.launch(this);
        }
    }
}

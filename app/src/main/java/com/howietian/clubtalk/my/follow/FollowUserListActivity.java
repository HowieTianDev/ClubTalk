package com.howietian.clubtalk.my.follow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.my.personpage.PersonPageActivity;

public class FollowUserListActivity extends BaseActivity<FollowUserListViewModel> implements FollowUserListAdapter.OnFollowUserClickListener {

    public static final int TYPE_MY_FOLLOW = 0;
    public static final int TYPE_MY_FAN = 1;
    public static final int TYPE_OTHER_FOLLOW = 2;
    public static final int TYPE_OTHER_FAN = 3;

    private static final String TAG_TYPE = "type";
    private static final String TAG_USER = "user";

    public static void launch(Context context, User user, int type) {
        if (user == null) {
            return;
        }
        Intent intent = new Intent(context, FollowUserListActivity.class);
        intent.putExtra(TAG_TYPE, type);
        intent.putExtra(TAG_USER, new Gson().toJson(user));
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private FollowUserListAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mType;
    private User mUser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_follow_list;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readArguments();
        findView();
        initRecycler();
        observe();

        getViewModel().start(mType, mUser);
    }

    private void readArguments() {
        if (getIntent() != null) {
            mType = getIntent().getIntExtra(TAG_TYPE, -1);
            String userJson = getIntent().getStringExtra(TAG_USER);
            mUser = new Gson().fromJson(userJson, User.class);

            if (mType == -1 || mUser == null) {
                showToast("系统错误");
                return;
            }
        }
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_follow);
        setSupportActionBar(mToolbar);

        if (mType == TYPE_MY_FOLLOW) {
            mToolbar.setTitle("我的关注");
        } else if (mType == TYPE_MY_FAN) {
            mToolbar.setTitle("我的粉丝");
        } else if (mType == TYPE_OTHER_FOLLOW) {
            mToolbar.setTitle("他的关注");
        } else if (mType == TYPE_OTHER_FAN) {
            mToolbar.setTitle("他的粉丝");
        }
        mRecyclerView = findViewById(R.id.recycler_follow_list);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecycler() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FollowUserListAdapter(this);
        getViewModel().bindData(mAdapter);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void observe() {
        getViewModel().getNotifyData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onClickUser(User user) {
        if (user != null) {
            PersonPageActivity.launch(this, user);
        }
    }
}

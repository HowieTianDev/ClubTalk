package com.howietian.clubtalk.my.work;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.home.event.detail.EventDetailActivity;
import com.howietian.clubtalk.home.event.list.EventListAdapter;
import com.howietian.clubtalk.views.LoadingAdapter;

public class WorkListActivity extends BaseActivity<WorkListViewModel> implements SwipeRefreshLayout.OnRefreshListener, EventListAdapter.OnEventItemClickListener {

    public static void launch(Context context, int type) {
        Intent intent = new Intent(context, WorkListActivity.class);
        intent.putExtra(TAG_TYPE, type);
        context.startActivity(intent);
    }

    public static final int TYPE_MY_JOINED = 0;
    public static final int TYPE_MY_PUBLISH = 1;
    public static final int TYPE_MY_COLLECT = 2;

    private static final String TAG_TYPE = "type";

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private VirtualLayoutManager mLayoutManager;
    private DelegateAdapter mAdapter;
    private EventListAdapter mEventListAdapter;
    private LoadingAdapter mLoadingAdapter;

    private int mType;

    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.getItemCount() == mLayoutManager.findLastVisibleItemPosition() + 1) {
                getViewModel().next();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_work_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readArguments();
        findView();
        initRecyclerView();
        observe();

        getViewModel().start(mType);
    }

    private void readArguments() {
        if (getIntent() != null) {
            mType = getIntent().getIntExtra(TAG_TYPE, -1);
            if (mType == -1) {
                showToast("系统错误");
                finish();
            }
        }
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_work);
        setSupportActionBar(mToolbar);
        if (mType == TYPE_MY_JOINED) {
            mToolbar.setTitle("报名活动");
        } else if (mType == TYPE_MY_PUBLISH) {
            mToolbar.setTitle("发起活动");
        } else if (mType == TYPE_MY_COLLECT) {
            mToolbar.setTitle("我的收藏");
        }

        mSwipeRefreshLayout = findViewById(R.id.layout_swipe_work);
        mRecyclerView = findViewById(R.id.recycler_work_list);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        mLayoutManager = new VirtualLayoutManager(this);
        mLayoutManager.setItemPrefetchEnabled(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DelegateAdapter(mLayoutManager);

        mEventListAdapter = new EventListAdapter(this, this);
        getViewModel().bindData(mEventListAdapter);
        mAdapter.addAdapter(mEventListAdapter);

        mLoadingAdapter = new LoadingAdapter(this);
        mAdapter.addAdapter(mLoadingAdapter);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void observe() {
        getViewModel().getNotifyData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                mEventListAdapter.notifyDataSetChanged();
            }
        });

        getViewModel().getNotifyFooter().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mLoadingAdapter.showLoading(aBoolean);
            }
        });

        getViewModel().getNotifySwipeRefresh().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mSwipeRefreshLayout.setRefreshing(true);
                } else {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        getViewModel().refresh();
    }

    @Override
    public void onClick(Event event) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.TAG_EVENT_DETAIL, new Gson().toJson(event));

        EventDetailActivity.launch(this, intent);
    }
}

package com.howietian.clubtalk.home.event.list;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseFragment;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.home.event.detail.EventDetailActivity;
import com.howietian.clubtalk.views.LoadingAdapter;

public class EventListFragment extends BaseFragment<EventListViewModel> implements EventListAdapter.OnEventItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG_TYPE = "type";
    public static final String TAG_TARGET_USER = "target_user";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private VirtualLayoutManager mLayoutManager;
    private DelegateAdapter mAdapter;
    private EventListAdapter mEventListAdapter;
    private LoadingAdapter mLoadingAdapter;

    private int mType;
    private User mTargetUser;

    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.getItemCount() == mLayoutManager.findLastVisibleItemPosition() + 1) {
                getViewModel().next();
            }
        }
    };

    public EventListFragment() {
        // Required empty public constructor
    }

    public static EventListFragment newInstance(int type) {
        EventListFragment fragment = new EventListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAG_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home_list;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        readArguments();
        findView();
        initRecyclerView();
        observe();

        getViewModel().start(mType, mTargetUser);
    }

    private void readArguments() {
        if (getArguments() != null) {
            mType = getArguments().getInt(TAG_TYPE);
            mTargetUser = new Gson().fromJson(getArguments().getString(TAG_TARGET_USER), User.class);
        }
    }

    private void findView() {
        mSwipeRefreshLayout = getView().findViewById(R.id.layout_swipe);
        mRecyclerView = getView().findViewById(R.id.recycler_home_list);

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        mLayoutManager = new VirtualLayoutManager(getActivity());
        mLayoutManager.setItemPrefetchEnabled(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DelegateAdapter(mLayoutManager);

        mEventListAdapter = new EventListAdapter(getActivity(), this);
        getViewModel().bindData(mEventListAdapter);
        mAdapter.addAdapter(mEventListAdapter);

        mLoadingAdapter = new LoadingAdapter(getActivity());
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
    public void onClick(Event event) {
        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
        intent.putExtra(EventDetailActivity.TAG_EVENT_DETAIL, new Gson().toJson(event));

        EventDetailActivity.launch(getActivity(), intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onRefresh() {
        getViewModel().refresh();
    }
}

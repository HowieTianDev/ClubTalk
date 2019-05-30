package com.howietian.clubtalk.circle;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseFragment;
import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.bean.Dynamic;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.circle.adapter.CircleTopAdapter;
import com.howietian.clubtalk.circle.adapter.CircleTopListAdapter;
import com.howietian.clubtalk.circle.adapter.DynamicListAdapter;
import com.howietian.clubtalk.entrance.login.LoginActivity;
import com.howietian.clubtalk.my.personpage.PersonPageActivity;
import com.howietian.clubtalk.publish.PublishDynamicActivity;
import com.howietian.clubtalk.publish.PublishEventActivity;
import com.howietian.clubtalk.search.SearchActivity;
import com.howietian.clubtalk.search.feedback.SearchFeedActivity;
import com.howietian.clubtalk.views.CommentDialog;
import com.howietian.clubtalk.views.LoadingAdapter;

import cn.bmob.v3.BmobUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class CircleFragment extends BaseFragment<CircleViewModel> implements CircleTopListAdapter.OnTopItemClickListener,
        View.OnClickListener, PopupMenu.OnMenuItemClickListener, SwipeRefreshLayout.OnRefreshListener, DynamicListAdapter.OnDynamicClickListener {

    public static final String TAG_TARGET_USER = "target_user";

    private TextView mSearchText;
    private LinearLayout mPublishLayout;
    private SwipeRefreshLayout mSwipeLayout;
    private RecyclerView mRecyclerView;
    private LinearLayout mTopBarLayout;

    private DelegateAdapter mAdapter;
    private VirtualLayoutManager mLayoutManager;
    private CircleTopAdapter mCircleTopAdapter;
    private DynamicListAdapter mDynamicListAdapter;

    private LoadingAdapter mLoadingAdapter;

    private String mCircleType = "";
    private User mTargetUser = null;

    User mUser = BmobUser.getCurrentUser(User.class);

    RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.getItemCount() == mLayoutManager.findLastVisibleItemPosition() + 1) {
                getViewModel().next(mCircleType, mTargetUser);
            }
        }
    };

    public CircleFragment() {
        // Required empty public constructor
    }

    public static CircleFragment newInstance() {
        Bundle bundle = new Bundle();

        CircleFragment circleFragment = new CircleFragment();
        circleFragment.setArguments(bundle);
        return circleFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        readArguments();
        findView();
        initRecyclerView();
        observe();

        getViewModel().start(mCircleType, mTargetUser);
    }

    private void readArguments() {
        if (getArguments() != null) {
            String userJson = getArguments().getString(TAG_TARGET_USER);
            mTargetUser = new Gson().fromJson(userJson, User.class);
        }
    }

    private void findView() {
        mTopBarLayout = getView().findViewById(R.id.layout_top_bar);
        mSearchText = getView().findViewById(R.id.text_search);
        mSearchText.setText("搜索社团反馈~");
        mPublishLayout = getView().findViewById(R.id.layout_publish);
        mSwipeLayout = getView().findViewById(R.id.layout_swipe_circle);
        mRecyclerView = getView().findViewById(R.id.recycler_circle_list);

        mSearchText.setOnClickListener(this);
        mPublishLayout.setOnClickListener(this);
        mSwipeLayout.setOnRefreshListener(this);
    }

    private void initRecyclerView() {
        mLayoutManager = new VirtualLayoutManager(getActivity());
        mLayoutManager.setItemPrefetchEnabled(true);
        mLayoutManager.setInitialPrefetchItemCount(10);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DelegateAdapter(mLayoutManager);
        if (mTargetUser == null) {
            mCircleTopAdapter = new CircleTopAdapter(this);
            mAdapter.addAdapter(mCircleTopAdapter);
            mTopBarLayout.setVisibility(View.VISIBLE);
        } else {
            mTopBarLayout.setVisibility(View.GONE);
        }

        mDynamicListAdapter = new DynamicListAdapter(this);
        getViewModel().bindData(mDynamicListAdapter);
        mAdapter.addAdapter(mDynamicListAdapter);

        mLoadingAdapter = new LoadingAdapter(getActivity());
        mAdapter.addAdapter(mLoadingAdapter);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
    }

    private void observe() {
        getViewModel().getNotifyData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                mDynamicListAdapter.notifyDataSetChanged();
            }
        });

        getViewModel().getNotifyItemData().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                int position = Integer.valueOf(strings[0]);
                mDynamicListAdapter.notifyItemChanged(position, strings[1]);
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
                    mSwipeLayout.setRefreshing(true);
                } else {
                    if (mSwipeLayout.isRefreshing()) {
                        mSwipeLayout.setRefreshing(false);
                    }
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onTopItemClick(String title) {
        mCircleType = title;
        getViewModel().selectCircle(title);
    }

    @Override
    public void onClick(View view) {
        if (view == mSearchText) {
            SearchFeedActivity.launch(getActivity());
        } else if (view == mPublishLayout) {
            showPublishMenu(view);
        }
    }

    private void showPublishMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_publish, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity:
                if (BmobUser.isLogin()) {
                    if (BmobUser.getCurrentUser(User.class).getClub()) {
                        PublishEventActivity.launch(getActivity());
                    } else {
                        showToast("只有社团用户才能发布活动哦，请前往个人中心申请");
                    }
                } else {
                    showToast("请先登录哦~");
                    LoginActivity.launch(getActivity());
                }
                break;
            case R.id.menu_dynamic:
                if (BmobUser.isLogin()) {
                    PublishDynamicActivity.launch(getActivity());
                } else {
                    showToast("请先登录哦~");
                    LoginActivity.launch(getActivity());
                }
                break;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        getViewModel().refresh(mCircleType, mTargetUser);
    }

    @Override
    public void onLike(boolean isLike, int position, Dynamic dynamic) {
        if (BmobUser.isLogin()) {
            getViewModel().like(isLike, position, dynamic, mUser);
        } else {
            showToast("请您先登录哦~");
            LoginActivity.launch(getActivity());
        }
    }

    @Override
    public void onComment(int position, Dynamic dynamic) {
        if (BmobUser.isLogin()) {
            showCommentDialog(position, dynamic);
        } else {
            showToast("请您先登录哦~");
            LoginActivity.launch(getActivity());
        }
    }

    @Override
    public void onCommentItemClick(int position, Comment comment) {
        showCommentDialogForReply(position, comment);
    }

    private void showCommentDialogForReply(final int position, final Comment comment) {
        if (comment == null) {
            return;
        }
        if (BmobUser.isLogin()) {
            CommentDialog commentDialog = new CommentDialog(getActivity(), true);

            commentDialog.setOnSubmitCommentListener(new CommentDialog.OnSubmitCommentListener() {
                @Override
                public void onSubmitComment(String text) {
                    Comment cm = new Comment();
                    cm.setContent(text);
                    cm.setDynamic(comment.getDynamic());
                    cm.setDynamicId(comment.getDynamicId());
                    cm.setUser(mUser);
                    cm.setReplyUser(comment.getUser());
                    getViewModel().comment(position, cm);
                }
            });
            commentDialog.show();
            if (comment.getUser() != null) {
                commentDialog.setHint("回复：" + comment.getUser().getNickName());
            }
        } else {
            showToast("请先登录哦~");
            LoginActivity.launch(getActivity());
        }
    }

    private void showCommentDialog(final int position, final Dynamic dynamic) {
        if (BmobUser.isLogin()) {
            CommentDialog commentDialog = new CommentDialog(getActivity(), true);
            commentDialog.setOnSubmitCommentListener(new CommentDialog.OnSubmitCommentListener() {
                @Override
                public void onSubmitComment(String text) {
                    Comment comment = new Comment();
                    comment.setContent(text);
                    comment.setDynamic(dynamic);
                    comment.setUser(mUser);
                    comment.setDynamicId(dynamic.getObjectId());
                    getViewModel().comment(position, comment);
                }
            });
            commentDialog.show();
        } else {
            showToast("请先登录哦~");
            LoginActivity.launch(getActivity());
        }
    }

    @Override
    public void onUserClick(User user) {
        if (user != null) {
            PersonPageActivity.launch(getActivity(), user);
        }
    }
}

package com.howietian.clubtalk.my.dynamic;

import androidx.annotation.NonNull;
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
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.bean.Dynamic;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.circle.adapter.DynamicListAdapter;
import com.howietian.clubtalk.entrance.login.LoginActivity;
import com.howietian.clubtalk.views.CommentDialog;
import com.howietian.clubtalk.views.LoadingAdapter;

import cn.bmob.v3.BmobUser;

public class MyDynamicActivity extends BaseActivity<MyDynamicViewModel> implements SwipeRefreshLayout.OnRefreshListener, DynamicListAdapter.OnDynamicClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, MyDynamicActivity.class);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private VirtualLayoutManager mLayoutManager;
    private DelegateAdapter mAdapter;
    private DynamicListAdapter mDynamicListAdapter;
    private LoadingAdapter mLoadingAdapter;

    private User mUser = BmobUser.getCurrentUser(User.class);

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
        return R.layout.activity_my_dynamic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        findView();
        initRecyclerView();
        observe();

        getViewModel().start();
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_my_dynamic);
        setSupportActionBar(mToolbar);

        mSwipeRefreshLayout = findViewById(R.id.layout_swipe_dynamic);
        mRecyclerView = findViewById(R.id.recycler_dynamic_list);

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

        mDynamicListAdapter = new DynamicListAdapter(this);
        getViewModel().bindData(mDynamicListAdapter);
        mAdapter.addAdapter(mDynamicListAdapter);

        mLoadingAdapter = new LoadingAdapter(this);
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

        getViewModel().getNotifyItemData().observe(this, new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                int position = Integer.valueOf(strings[0]);
                mDynamicListAdapter.notifyItemChanged(position, strings[1]);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRecyclerView.removeOnScrollListener(mOnScrollListener);
    }

    @Override
    public void onRefresh() {
        getViewModel().refresh();
    }

    @Override
    public void onLike(boolean isLike, int position, Dynamic dynamic) {
        if (BmobUser.isLogin()) {
            getViewModel().like(isLike, position, dynamic, mUser);
        } else {
            showToast("请您先登录哦~");
            LoginActivity.launch(this);
        }
    }

    @Override
    public void onComment(int position, Dynamic dynamic) {
        if (BmobUser.isLogin()) {
            showCommentDialog(position, dynamic);
        } else {
            showToast("请您先登录哦~");
            LoginActivity.launch(this);
        }
    }

    @Override
    public void onCommentItemClick(int position, Comment comment) {
        showCommentDialogForReply(position, comment);
    }

    @Override
    public void onUserClick(User user) {

    }

    private void showCommentDialogForReply(final int position, final Comment comment) {
        if (comment == null) {
            return;
        }
        if (BmobUser.isLogin()) {
            CommentDialog commentDialog = new CommentDialog(this, true);

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
            LoginActivity.launch(this);
        }
    }

    private void showCommentDialog(final int position, final Dynamic dynamic) {
        if (BmobUser.isLogin()) {
            CommentDialog commentDialog = new CommentDialog(this, true);
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
            LoginActivity.launch(this);
        }
    }
}

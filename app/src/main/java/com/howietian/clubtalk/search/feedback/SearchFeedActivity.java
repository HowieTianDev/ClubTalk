package com.howietian.clubtalk.search.feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
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
import com.howietian.clubtalk.search.SearchActivity;
import com.howietian.clubtalk.search.SearchResultAdapter;
import com.howietian.clubtalk.views.CommentDialog;

import cn.bmob.v3.BmobUser;

public class SearchFeedActivity extends BaseActivity<SearchFeedViewModel> implements DynamicListAdapter.OnDynamicClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, SearchFeedActivity.class);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;


    private DelegateAdapter mAdapter;
    private VirtualLayoutManager mLayoutManager;
    private DynamicListAdapter mDynamicListAdapter;

    private User mUser = BmobUser.getCurrentUser(User.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        initRecyclerView();
        observe();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(mToolbar);

        mRecyclerView = findViewById(R.id.recycler_search_result);

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

        mDynamicListAdapter = new DynamicListAdapter(this);
        getViewModel().bindData(mDynamicListAdapter);
        mAdapter.addAdapter(mDynamicListAdapter);

        mRecyclerView.setAdapter(mAdapter);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchAutoComplete = mSearchView.findViewById(R.id.search_src_text);

        mSearchView.onActionViewExpanded();
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    getViewModel().search(query);
                } else {
                    showToast("查询条件不能为空哦~");
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    getViewModel().clear();
                }
                return true;
            }
        });
        return true;
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



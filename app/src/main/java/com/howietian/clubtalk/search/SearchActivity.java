package com.howietian.clubtalk.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.home.event.detail.EventDetailActivity;

public class SearchActivity extends BaseActivity<SearchViewModel> implements SearchResultAdapter.OnSearchItemClickListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;


    private DelegateAdapter mAdapter;
    private VirtualLayoutManager mLayoutManager;
    private SearchResultAdapter mSearchResultAdapter;


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

        mSearchResultAdapter = new SearchResultAdapter(this);
        getViewModel().bindData(mSearchResultAdapter);
        mAdapter.addAdapter(mSearchResultAdapter);

        mRecyclerView.setAdapter(mAdapter);
    }

    private void observe() {
        getViewModel().getNotifySearchResult().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                mSearchResultAdapter.notifyDataSetChanged();
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
                if (!TextUtils.isEmpty(newText)) {
                    getViewModel().search(newText);
                }else {
                    getViewModel().clear();
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public void onClick(Event event) {
        Intent intent = new Intent(this, EventDetailActivity.class);
        String eventJson = new Gson().toJson(event);
        intent.putExtra(EventDetailActivity.TAG_EVENT_DETAIL, eventJson);

        EventDetailActivity.launch(this, intent);
    }
}

package com.howietian.clubtalk.publish;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.publish.adapter.TypeAdapter;

public class ChooseFeedEventActivity extends BaseActivity<ChooseFeedEventViewModel> implements TypeAdapter.OnTypeItemClickListener {

    public static void launchForResult(Context context, int requestCode) {
        Intent intent = new Intent(context, ChooseFeedEventActivity.class);
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).startActivityForResult(intent, requestCode);
        }
    }

    public static final String EXTRA_EVENT = "extra_event";
    public static final String EXTRA_CLUB = "extra_club";
    public static final String EXTRA_BUNDLE = "extra_bundle";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TypeAdapter mTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
        initRecyclerView();

        observe();
        getViewModel().start();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_feed_event;
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_choose_feed);
        setSupportActionBar(mToolbar);
        mRecyclerView = findViewById(R.id.recycler_feed_list);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this);

        mTypeAdapter = new TypeAdapter(this, getViewModel().getFeedEvent(), this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mTypeAdapter);
    }


    private void observe() {
        getViewModel().getNotifyData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                mTypeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(String title) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CLUB,getViewModel().getClubName(title));
        bundle.putString(EXTRA_EVENT,title);
        bundle.putString(ChooseTypeActivity.EXTRA_TYPE,"反馈");

        intent.putExtra(EXTRA_BUNDLE,bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
}

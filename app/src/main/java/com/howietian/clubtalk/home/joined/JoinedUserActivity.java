package com.howietian.clubtalk.home.joined;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JoinedUserActivity extends BaseActivity<JoinedUserViewModel> {


    private static final int REQUEST_STORAGE = 0;

    public static void launch(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static final String TAG_EVENT = "tag_event";

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    private JoinedUserAdapter mJoinedUserAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readArguments();
        findView();
        initRecyclerView();
        observe();

        getViewModel().start(mEvent,this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_joined_user;
    }

    private void readArguments() {
        if (getIntent() != null) {
            mEvent = new Gson().fromJson(getIntent().getStringExtra(TAG_EVENT), Event.class);
            if (mEvent == null) {
                showToast("系统错误");
                finish();
            }
        }
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_joined_user);
        setSupportActionBar(mToolbar);

        mRecyclerView = findViewById(R.id.recycler_joined_user);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mJoinedUserAdapter = new JoinedUserAdapter();
        getViewModel().bindData(mJoinedUserAdapter);
        mRecyclerView.setAdapter(mJoinedUserAdapter);
    }

    private void observe() {
        getViewModel().getNotifyData().observe(this, new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                mJoinedUserAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_export, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_export) {
            if (mEvent.getJoinedIdList() == null || mEvent.getJoinedIdList().isEmpty()) {
                showToast("当前报名人数为0，无法导出报名表");
            } else {
                requestExportExcel();
            }
        }
        return true;
    }

    private void requestExportExcel() {
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE);
        } else {
            getViewModel().exportExcel(mEvent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getViewModel().exportExcel(mEvent);
            } else {
                showToast("STORAGE PERMISSION DENIED");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

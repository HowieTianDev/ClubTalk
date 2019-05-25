package com.howietian.clubtalk.publish;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.publish.adapter.TypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChooseTypeActivity extends BaseActivity implements TypeAdapter.OnTypeItemClickListener {

    public static void launchForResult(Context context, int requestCode) {
        Intent intent = new Intent(context, ChooseTypeActivity.class);
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).startActivityForResult(intent, requestCode);
        }
    }

    public static final String EXTRA_TYPE = "type";

    private String[] mTypes = new String[]{"学校：你的学校发生了什么...", "挑战：打卡活动", "情感：恋爱、情侣、暗恋、喜欢...", "体育：足球、篮球、跑步、健身...", "娱乐：音乐、影视、游戏、动漫...", "文艺：阅读、摄影...", "生活：萌宠、美食...", "比赛：竞赛、辩论、商赛...", "学习：高数、大物、英语..."};

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TypeAdapter mTypeAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        initRecyclerView();
    }

    private void findView() {
        mToolbar = findViewById(R.id.toolbar_choose_type);
        setSupportActionBar(mToolbar);
        mRecyclerView = findViewById(R.id.recycler_type_list);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        List<String> types = new ArrayList<>();
        for (int i = 0; i < mTypes.length; i++) {
            types.add(mTypes[i]);
        }
        mTypeAdapter = new TypeAdapter(this, types, this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mTypeAdapter);
    }

    @Override
    public void onClick(String type) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TYPE, type.split("：")[0]);
        setResult(RESULT_OK, intent);
        finish();
    }
}

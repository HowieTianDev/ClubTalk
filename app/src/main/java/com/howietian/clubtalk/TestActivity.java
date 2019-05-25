package com.howietian.clubtalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.howietian.clubtalk.views.LoadingAdapter;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    private Button mButton;
    private RecyclerView mRecyclerView;

    private VirtualLayoutManager mLayoutManager;
    private DelegateAdapter mAdapter;
    private LoadingAdapter mLoadingAdapter;
    private TestAdapter mTestAdapter;

    private ArrayList<String> mTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initData();
        findView();
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            mTitles.add(i + "   test");
        }
    }

    private void findView() {
        mButton = findViewById(R.id.btn_test);
        mRecyclerView = findViewById(R.id.recycler_test);

        mLayoutManager = new VirtualLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DelegateAdapter(mLayoutManager);

        mTestAdapter = new TestAdapter(mTitles);
        mAdapter.addAdapter(mTestAdapter);

        mLoadingAdapter = new LoadingAdapter(this);
        mAdapter.addAdapter(mLoadingAdapter);

        mRecyclerView.setAdapter(mAdapter);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeData();
            }
        });
    }

    int count = 3;

    private void changeData() {
        mTitles.clear();
        for (int i = 0; i < count; i++) {
            mTitles.add(i + "hhhhhhhhh");
        }
        count++;

        mTestAdapter.notifyDataSetChanged();
    }


}

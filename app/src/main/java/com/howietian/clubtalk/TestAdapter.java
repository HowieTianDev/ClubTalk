package com.howietian.clubtalk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;

import java.util.ArrayList;

public class TestAdapter extends DelegateAdapter.Adapter<TestAdapter.TestViewHolder> {

    private ArrayList<String> mTitles = new ArrayList<>();

    public TestAdapter(ArrayList<String> titles) {
        for (int i = 0; i < 20; i++) {
            mTitles.add(i + "   test");
        }

    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_single, parent, false);
        TestViewHolder holder = new TestViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        holder.fill(mTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text_type);
        }

        public void fill(String title) {
            mTextView.setText(title);
        }
    }
}

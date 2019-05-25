package com.howietian.clubtalk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.utils.CommonUtil;

public class LoadingView extends FrameLayout {

    private LinearLayout mLoadingLayout;
    private TextView mEmptyTextView;

    public LoadingView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_view, null);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.height = CommonUtil.dip2px(context,50);
        setLayoutParams(layoutParams);

        addView(view);

        mLoadingLayout = view.findViewById(R.id.layout_loading);
        mEmptyTextView = view.findViewById(R.id.text_empty);
    }

    public void showLoading(boolean isLoading) {
        if (isLoading) {
            mLoadingLayout.setVisibility(VISIBLE);
            mEmptyTextView.setVisibility(GONE);
        } else {
            mLoadingLayout.setVisibility(GONE);
            mEmptyTextView.setVisibility(VISIBLE);
        }

    }

}

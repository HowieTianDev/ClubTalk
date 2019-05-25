package com.howietian.clubtalk.views.circle.listener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

import android.text.TextPaint;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.entrance.login.LoginActivity;
import com.howietian.clubtalk.my.personpage.PersonPageActivity;
import com.howietian.clubtalk.utils.UIHelper;

import cn.bmob.v3.BmobUser;


public class CommentClick extends ClickableSpanEx {
    private Context mContext;
    private int textSize;
    private User mUserInfo;


    private CommentClick() {
    }

    private CommentClick(Builder builder) {
        super(builder.color, builder.clickEventColor);
        mContext = builder.mContext;
        mUserInfo = builder.mUserInfo;
        this.textSize = builder.textSize;
    }

    @Override
    public void onClick(View widget) {
        if (mUserInfo != null) {
            PersonPageActivity.launch(mContext, mUserInfo);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setTextSize(textSize);
        ds.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public static class Builder {
        private int color;
        private Context mContext;
        private int textSize = 16;
        private User mUserInfo;
        private int clickEventColor;


        public Builder(Context context, @NonNull User info) {
            mContext = context;
            mUserInfo = info;
        }

        public Builder setTextSize(int textSize) {
            this.textSize = UIHelper.sp2px(textSize);
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            return this;
        }

        public Builder setClickEventColor(int color) {
            this.clickEventColor = color;
            return this;
        }

        public CommentClick build() {
            return new CommentClick(this);
        }
    }


}
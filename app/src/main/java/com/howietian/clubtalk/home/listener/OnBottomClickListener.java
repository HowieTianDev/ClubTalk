package com.howietian.clubtalk.home.listener;

import com.howietian.clubtalk.bean.Comment;

public interface OnBottomClickListener {
    void onLikeSucceed(int likeCount);

    void onLikeFail();

    void cancelLikeSucceed(int likeCount);

    void cancelLikeFail();

    void onCollectSucceed();

    void onCollectFail();

    void cancelCollectSucceed();

    void cancelCollectFail();

    void onCommentSucceed(Comment comment,int commentCount);
}

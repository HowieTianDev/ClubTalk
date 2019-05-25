package com.howietian.clubtalk.home.listener;

import com.howietian.clubtalk.bean.Comment;

public interface OnCommentClickListener {
    void commentLike(int positon, Comment comment);

    void commentCancelLike(int position, Comment comment);
}

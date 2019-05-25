package com.howietian.clubtalk.home.event.detail;

import com.howietian.clubtalk.bean.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventCommentDataHelper {

    public static final int TYPE_COMMENT_TITLE = 0;
    public static final int TYPE_COMMENT_ITEN = 1;

    private List<Comment> mComments;

    private int mEventLikeCount;

    public boolean setCommentList(List<Comment> comments) {
        if (!Objects.equals(comments, mComments)) {
            mComments = comments;
            return true;
        }
        return false;
    }

    public void insertComment(Comment comment) {
        if (comment == null) {
            return;
        }

        if (mComments == null) {
            mComments = new ArrayList<>();
            mComments.add(comment);
        } else {
            mComments.add(0, comment);
        }
    }

    public int getCount() {
        if (mComments == null) {
            return 1;
        }

        return mComments.size() + 1;
    }

    public int getCommentCount() {
        if (mComments == null) {
            return 0;
        }
        return mComments.size();
    }

    public int getEventLikeCount() {
        return mEventLikeCount;
    }

    public Comment getCommentItem(int position) {
        position--;
        return mComments.get(position);
    }

    public void setItemLiked(int position, boolean isLike) {
        if (mComments != null && position < mComments.size() && mComments.get(position) != null) {
            mComments.get(position).setLiked(isLike);
        }
    }

    public void setEventLikeCount(int count) {
        mEventLikeCount = count;
    }
}

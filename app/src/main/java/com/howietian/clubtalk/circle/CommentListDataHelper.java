package com.howietian.clubtalk.circle;

import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.bean.CommentList;

import java.util.Objects;

public class CommentListDataHelper {

    private CommentList mCommentList;

    public boolean setCommentList(CommentList comments) {
        if (!Objects.equals(comments, mCommentList)) {
            mCommentList = comments;
            return true;
        }

        return false;
    }

    public int getCount() {
        if (mCommentList == null) {
            return 0;
        }
        return mCommentList.size();
    }

    public Comment getItem(int position) {
        if (mCommentList != null) {
            return mCommentList.get(position);
        }
        return null;
    }

    public void insertComment(Comment comment) {
        if (comment == null) {
            return;
        }
        if (mCommentList == null) {
            mCommentList = new CommentList();
            mCommentList.add(comment);
        } else {
            mCommentList.add(0, comment);
        }
    }

}

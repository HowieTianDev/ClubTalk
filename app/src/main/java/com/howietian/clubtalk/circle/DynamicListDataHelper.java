package com.howietian.clubtalk.circle;

import com.howietian.clubtalk.bean.Comment;
import com.howietian.clubtalk.bean.CommentList;
import com.howietian.clubtalk.bean.Dynamic;
import com.howietian.clubtalk.bean.DynamicList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DynamicListDataHelper {

    private int mOffset;
    private boolean mHasMore = false;
    private int mPageCount = 10;
    private DynamicList mDynamicList;
    private Map<String, CommentList> mCommentListMap = new HashMap<>();

    public boolean setDynamicList(DynamicList list, CommentList comments, int offset) {
        boolean flag;
        if (!Objects.equals(list, mDynamicList)) {
            if (offset == 0) {
                handleData(comments, list);
                mDynamicList = list;
            } else {
                handleData(comments, list);
                mDynamicList.addAll(list);
            }
            flag = true;
        } else {
            flag = false;
        }

        if (list == null || list.size() < mPageCount) {
            mHasMore = false;
        } else {
            mOffset += list.size();
        }

        return flag;
    }

    private void handleData(CommentList comments, DynamicList dynamicList) {
        if(mCommentListMap == null){
            mCommentListMap = new HashMap<>();
        }
        for (Comment comment : comments) {
            CommentList commentList = mCommentListMap.get(comment.getDynamicId());
            if (commentList == null) {
                commentList = new CommentList();
                mCommentListMap.put(comment.getDynamicId(), commentList);
            }
            commentList.add(comment);
        }


        for (Dynamic dynamic : dynamicList) {
            CommentList commentList = mCommentListMap.get(dynamic.getObjectId());
            CommentListDataHelper commentListDataHelper = dynamic.getCommentDataHelper();
            if (commentListDataHelper == null) {
                commentListDataHelper = new CommentListDataHelper();
            }

            commentListDataHelper.setCommentList(commentList);

            dynamic.setCommentDataHelper(commentListDataHelper);
        }
    }

    public int getOffset() {
        return mOffset;
    }

    public boolean isHasMore() {
        return mHasMore;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public int getCount() {
        if (mDynamicList == null) {
            return 0;
        }
        return mDynamicList.size();
    }

    public Dynamic getItem(int position) {
        return mDynamicList.get(position);
    }

    public void updateItem(int position,Dynamic dynamic){
        if(mDynamicList != null){
            mDynamicList.set(position,dynamic);
        }
    }

    public void clear() {
        if (mCommentListMap != null) {
            mCommentListMap.clear();
        }
        if (mDynamicList != null) {
            mDynamicList.clear();
        }
        mHasMore = false;
        mOffset = 0;
    }

}

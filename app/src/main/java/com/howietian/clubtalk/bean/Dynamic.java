package com.howietian.clubtalk.bean;

import com.howietian.clubtalk.circle.CommentListDataHelper;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Dynamic extends BmobObject {

    private User user;
    private String content;
    private String type;

    private Integer commentCount = 0;
    private Integer likeCount = 0;

    private ArrayList<String> ImageUrlList;
    private ArrayList<String> likedIdList;

    private BmobRelation like;

    private CommentListDataHelper mCommentDataHelper;

    // 动态针对反馈类型
    private String eventName;
    private String clubName;

    private Boolean isLiked = false;

    public Dynamic(){
        if(likedIdList != null){
            likeCount = likedIdList.size();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public ArrayList<String> getImageUrlList() {
        return ImageUrlList;
    }

    public void setImageUrlList(ArrayList<String> imageUrlList) {
        ImageUrlList = imageUrlList;
    }

    public ArrayList<String> getLikedIdList() {
        return likedIdList;
    }

    public void setLikedIdList(ArrayList<String> likedIdList) {
        this.likedIdList = likedIdList;
    }

    public BmobRelation getLike() {
        return like;
    }

    public void setLike(BmobRelation like) {
        this.like = like;
    }

    public CommentListDataHelper getCommentDataHelper() {
        return mCommentDataHelper;
    }

    public void setCommentDataHelper(CommentListDataHelper commentDataHelper) {
        mCommentDataHelper = commentDataHelper;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }
}

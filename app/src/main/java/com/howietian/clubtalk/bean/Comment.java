package com.howietian.clubtalk.bean;

import java.util.ArrayList;
import java.util.Objects;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Comment extends BmobObject {

    private User user;
    private User replyUser;
    private Event event;
    private Dynamic dynamic;
    private String dynamicId;

    private String content;
    private Integer likeCount = 0;

    private Boolean isLiked = false;

    private BmobRelation like;
    private ArrayList<String> likedIdList;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public BmobRelation getLike() {
        return like;
    }

    public void setLike(BmobRelation like) {
        this.like = like;
    }

    public ArrayList<String> getLikedIdList() {
        return likedIdList;
    }

    public void setLikedIdList(ArrayList<String> likedIdList) {
        this.likedIdList = likedIdList;
    }

    public Boolean getLiked() {
        return isLiked;
    }

    public void setLiked(Boolean liked) {
        isLiked = liked;
    }

    public Dynamic getDynamic() {
        return dynamic;
    }

    public void setDynamic(Dynamic dynamic) {
        this.dynamic = dynamic;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public User getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(user, comment.user) &&
                Objects.equals(event, comment.event) &&
                Objects.equals(content, comment.content) &&
                Objects.equals(likeCount, comment.likeCount) &&
                Objects.equals(likedIdList, comment.likedIdList);
    }

}

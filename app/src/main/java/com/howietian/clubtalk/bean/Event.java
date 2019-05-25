package com.howietian.clubtalk.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Event extends BmobObject {

    public static final int TYPE_EVENT = 0; // 活动页面类型
    public static final int TYPE_ELEGANT = 1; // 精选页面类型
    public static final int TYPE_BANNER = 2;  // banner 页面类型
    public static final int TYPE_PERSON_PAGE = 3; // 个人主页类型

    private Integer type = 0; // 活动类型，默认是活动类型 0，精选类型需要在后台配 1

    private User user;
    private String title;
    private String content;
    private String url = ""; // 只有精选类型 URL 会在后端赋值

    private String nickname;

    private Integer likeCount = 0;
    private Integer commentCount = 0;

    private String deadLine;
    private String startTime;

    private String organizer;
    private String phone;
    private String place;

    private BmobFile cover;

    private BmobRelation like;
    private BmobRelation collect;
    private BmobRelation join;

    private ArrayList<String> likedIdList;
    private ArrayList<String> collectedIdList;
    private ArrayList<String> joinedIdList;

    public Event() {
        if (likedIdList != null && !likedIdList.isEmpty()) {
            Set<String> likeIdSet = new HashSet<>();
            for (int i = 0; i < likedIdList.size(); i++) {
                likeIdSet.add(likedIdList.get(i));
            }
            likeCount = likeIdSet.size();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user != null) {
            this.nickname = user.getNickName();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BmobRelation getLike() {
        return like;
    }

    public void setLike(BmobRelation like) {
        this.like = like;
    }


    public BmobRelation getJoin() {
        return join;
    }

    public void setJoin(BmobRelation join) {
        this.join = join;
    }

    public ArrayList<String> getLikedIdList() {
        return likedIdList;
    }

    public void setLikedIdList(ArrayList<String> likedIdList) {
        this.likedIdList = likedIdList;
    }


    public ArrayList<String> getJoinedIdList() {
        return joinedIdList;
    }

    public void setJoinedIdList(ArrayList<String> joinedIdList) {
        this.joinedIdList = joinedIdList;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public BmobFile getCover() {
        return cover;
    }

    public void setCover(BmobFile cover) {
        this.cover = cover;
    }

    public BmobRelation getCollect() {
        return collect;
    }

    public void setCollect(BmobRelation collect) {
        this.collect = collect;
    }

    public ArrayList<String> getCollectedIdList() {
        return collectedIdList;
    }

    public void setCollectedIdList(ArrayList<String> collectedIdList) {
        this.collectedIdList = collectedIdList;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(user, event.user) &&
                Objects.equals(title, event.title) &&
                Objects.equals(content, event.content) &&
                Objects.equals(nickname, event.nickname) &&
                Objects.equals(likeCount, event.likeCount) &&
                Objects.equals(commentCount, event.commentCount) &&
                Objects.equals(deadLine, event.deadLine) &&
                Objects.equals(startTime, event.startTime) &&
                Objects.equals(organizer, event.organizer) &&
                Objects.equals(phone, event.phone) &&
                Objects.equals(place, event.place) &&
                Objects.equals(likedIdList, event.likedIdList) &&
                Objects.equals(collectedIdList, event.collectedIdList) &&
                Objects.equals(joinedIdList, event.joinedIdList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, title, content, likeCount, commentCount, deadLine, startTime, organizer, phone, place, cover, like, collect, join, likedIdList, collectedIdList, joinedIdList);
    }
}

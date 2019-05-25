package com.howietian.clubtalk.bean;

import java.util.ArrayList;
import java.util.Objects;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class User extends BmobUser {
    private String nickName;
    private Boolean gender = true;
    private BmobFile avatar;
    private Boolean isClub = false;
    private String clubIntro;
    private String profile;
    private String school;
    private String realName;


    private Integer publishCount = 0;
    private Integer followCount = 0;
    private Integer fanCount = 0;

    private BmobRelation follow;
    private ArrayList<String> mFollowIdList;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public Boolean getClub() {
        return isClub;
    }

    public void setClub(Boolean club) {
        isClub = club;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Integer getPublishCount() {
        return publishCount;
    }

    public void setPublishCount(Integer publishCount) {
        this.publishCount = publishCount;
    }

    public Integer getFollowCount() {
        return followCount;
    }

    public void setFollowCount(Integer followCount) {
        this.followCount = followCount;
    }

    public Integer getFanCount() {
        return fanCount;
    }

    public void setFanCount(Integer fanCount) {
        this.fanCount = fanCount;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getClubIntro() {
        return clubIntro;
    }

    public void setClubIntro(String clubIntro) {
        this.clubIntro = clubIntro;
    }

    public ArrayList<String> getFollowIdList() {
        return mFollowIdList;
    }

    public void setFollowIdList(ArrayList<String> followIdList) {
        mFollowIdList = followIdList;
    }

    public BmobRelation getFollow() {
        return follow;
    }

    public void setFollow(BmobRelation follow) {
        this.follow = follow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(nickName, user.nickName) &&
                Objects.equals(gender, user.gender) &&
                Objects.equals(avatar, user.avatar) &&
                Objects.equals(isClub, user.isClub) &&
                Objects.equals(profile, user.profile) &&
                Objects.equals(school, user.school) &&
                Objects.equals(realName, user.realName) &&
                Objects.equals(publishCount, user.publishCount) &&
                Objects.equals(followCount, user.followCount) &&
                Objects.equals(fanCount, user.fanCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickName, gender, avatar, isClub, profile, school, realName, publishCount, followCount, fanCount);
    }
}

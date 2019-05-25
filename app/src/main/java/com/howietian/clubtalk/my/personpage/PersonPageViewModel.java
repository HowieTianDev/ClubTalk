package com.howietian.clubtalk.my.personpage;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.User;

import java.util.ArrayList;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.UpdateListener;

public class PersonPageViewModel extends BaseViewModel {

    private MutableLiveData<String> mNotifyAvatar = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNotifyIsClub = new MutableLiveData<>();
    private MutableLiveData<Integer> mNotifyFollowCount = new MutableLiveData<>();
    private MutableLiveData<Integer> mNotifyFanCount = new MutableLiveData<>();
    private MutableLiveData<String> mNotifyNickname = new MutableLiveData<>();
    private MutableLiveData<String> mNotifyProfile = new MutableLiveData<>();
    // 0 不显示 1 关注 2 已关注
    private MutableLiveData<Integer> mNotifyIsFollow = new MutableLiveData<>();

    private int mFanCount = 0;


    public void start(User targetUser, User currentUser) {
        requestFanCount(targetUser);

        if (targetUser.getAvatar() != null) {
            mNotifyAvatar.postValue(targetUser.getAvatar().getUrl());
        }
        mNotifyIsClub.postValue(targetUser.getClub());
        mNotifyFollowCount.postValue(targetUser.getFollowCount());
        mNotifyNickname.postValue(targetUser.getNickName());
        mNotifyProfile.postValue(targetUser.getProfile());
        if (currentUser != null && targetUser.getObjectId().equals(currentUser.getObjectId())) {
            mNotifyIsFollow.postValue(0);
        } else {
            if (BmobUser.isLogin() && currentUser.getFollowIdList() != null &&
                    currentUser.getFollowIdList().contains(targetUser.getObjectId())) {
                mNotifyIsFollow.postValue(2);
            } else {
                mNotifyIsFollow.postValue(1);
            }
        }
    }

    private void requestFanCount(User targetUser) {
        BmobQuery<User> query = new BmobQuery<>();
        query.include("follow");

        BmobQuery<BmobUser> innerQuery = new BmobQuery<>();
        innerQuery.addWhereEqualTo("objectId", targetUser.getObjectId());
        query.addWhereMatchesQuery("follow", "_User", innerQuery);

        query.count(User.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    mNotifyFanCount.postValue(integer);
                    mFanCount = integer;
                } else {
                    toast("网络开小差了");
                }
            }
        });
    }


    public void follow(User targetUser, final User currentUser) {

        BmobRelation relation = new BmobRelation();
        relation.add(targetUser);
        currentUser.setFollow(relation);

        ArrayList<String> followIdList;
        if (currentUser.getFollowIdList() == null) {
            followIdList = new ArrayList<>();
        } else {
            followIdList = currentUser.getFollowIdList();
        }

        followIdList.add(targetUser.getObjectId());
        currentUser.setFollowIdList(followIdList);
        currentUser.setFollowCount(followIdList.size());

        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mNotifyIsFollow.postValue(2);
                    mNotifyFanCount.postValue(++mFanCount);
                    toast("关注成功");
                } else {
                    toast("网络开小差了");
                }
            }
        });


    }

    public void cancelFollow(User targetUser, User currentUser) {
        BmobRelation relation = new BmobRelation();
        relation.remove(targetUser);
        currentUser.setFollow(relation);

        final ArrayList<String> followIdList;
        if (currentUser.getFollowIdList() == null) {
            followIdList = new ArrayList<>();
        } else {
            followIdList = currentUser.getFollowIdList();
        }
        if (followIdList.contains(targetUser.getObjectId())) {
            followIdList.remove(targetUser.getObjectId());
        }
        currentUser.setFollowIdList(followIdList);
        currentUser.setFollowCount(followIdList.size());

        currentUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    mNotifyIsFollow.postValue(1);
                    mNotifyFanCount.postValue(--mFanCount);
                    toast("取消关注");
                } else {
                    toast("网络开小差了");
                }
            }
        });

    }

    public MutableLiveData<String> getNotifyAvatar() {
        return mNotifyAvatar;
    }

    public MutableLiveData<Boolean> getNotifyIsClub() {
        return mNotifyIsClub;
    }

    public MutableLiveData<Integer> getNotifyFollowCount() {
        return mNotifyFollowCount;
    }

    public MutableLiveData<Integer> getNotifyFanCount() {
        return mNotifyFanCount;
    }

    public MutableLiveData<String> getNotifyNickname() {
        return mNotifyNickname;
    }

    public MutableLiveData<String> getNotifyProfile() {
        return mNotifyProfile;
    }

    public MutableLiveData<Integer> getNotifyIsFollow() {
        return mNotifyIsFollow;
    }
}

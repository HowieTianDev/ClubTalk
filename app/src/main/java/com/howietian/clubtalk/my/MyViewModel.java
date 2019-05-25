package com.howietian.clubtalk.my;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;

public class MyViewModel extends BaseViewModel {
    private MutableLiveData<String> mNotifyAvatar = new MutableLiveData<>();
    private MutableLiveData<String> mNotifyNickName = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNotifyIsClub = new MutableLiveData<>();
    private MutableLiveData<Integer> mNotifyPublishCount = new MutableLiveData<>();
    private MutableLiveData<Integer> mNotifyFollowCount = new MutableLiveData<>();
    private MutableLiveData<Integer> mNotifyFanCount = new MutableLiveData<>();

    public void start() {
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            requestFanCount(user);
            if (user.getAvatar() != null) {
                mNotifyAvatar.postValue(user.getAvatar().getUrl());
            }
            mNotifyNickName.postValue(user.getNickName());
            mNotifyIsClub.postValue(user.getClub());
            mNotifyPublishCount.postValue(user.getPublishCount());
            mNotifyFollowCount.postValue(user.getFollowCount());

        }
    }

    private void requestFanCount(User targetUser) {
        BmobQuery<User> query = new BmobQuery<>();
        query.include("follow");

        BmobQuery<User> innerQuery = new BmobQuery<>();
        innerQuery.addWhereEqualTo("objectId", targetUser.getObjectId());
        query.addWhereMatchesQuery("follow", "_User", innerQuery);

        query.count(User.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    mNotifyFanCount.postValue(integer);
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

    public MutableLiveData<Integer> getNotifyPublishCount() {
        return mNotifyPublishCount;
    }

    public MutableLiveData<Integer> getNotifyFollowCount() {
        return mNotifyFollowCount;
    }

    public MutableLiveData<Integer> getNotifyFanCount() {
        return mNotifyFanCount;
    }

    public MutableLiveData<String> getNotifyNickName() {
        return mNotifyNickName;
    }
}

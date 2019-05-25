package com.howietian.clubtalk.home.joined;

import android.icu.lang.UScript;

import com.howietian.clubtalk.bean.User;

import java.net.UnknownServiceException;
import java.util.List;
import java.util.Objects;

public class JoinedUserDataHelper {
    private List<User> mUserList;

    public boolean setJoinedUserList(List<User> userList) {
        if (!Objects.equals(userList, mUserList)) {
            mUserList = userList;
            return true;
        }
        return false;
    }

    public int getCount() {
        if (mUserList == null) {
            return 0;
        }
        return mUserList.size();
    }

    // 从1开始
    public User getItem(int positon) {
        if (positon < 1) {
            return null;
        }
        return mUserList.get(positon - 1);
    }

    public List<User> getUserList(){
        return mUserList;
    }
}

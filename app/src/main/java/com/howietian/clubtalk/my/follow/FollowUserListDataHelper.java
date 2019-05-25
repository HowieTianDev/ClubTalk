package com.howietian.clubtalk.my.follow;

import com.howietian.clubtalk.bean.User;

import java.util.List;
import java.util.Objects;

public class FollowUserListDataHelper {
    private List<User> mUserList;

    public boolean setFollowUserList(List<User> userList) {
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

        return mUserList.get(positon);
    }

    public List<User> getUserList(){
        return mUserList;
    }
}

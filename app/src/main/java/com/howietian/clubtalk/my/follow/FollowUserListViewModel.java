package com.howietian.clubtalk.my.follow;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.listener.IDataBinder;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FollowUserListViewModel extends BaseViewModel {

    private MutableLiveData<Void> mNotifyData = new MutableLiveData<>();

    private FollowUserListDataHelper mDataHelper = new FollowUserListDataHelper();

    public void start(int type, User targetUser) {
        requestFollowList(type, targetUser);
    }


    private void requestFollowList(final int type, final User targetUser) {
        if (targetUser == null) {
            return;
        }
        showProgressText("正在查询...");
        showProgressDialog(true);
        final BmobQuery<User> query = new BmobQuery<>();

        if (type == FollowUserListActivity.TYPE_MY_FOLLOW || type == FollowUserListActivity.TYPE_OTHER_FOLLOW) {
            query.addWhereRelatedTo("follow", new BmobPointer(targetUser));

        } else if (type == FollowUserListActivity.TYPE_MY_FAN || type == FollowUserListActivity.TYPE_OTHER_FAN) {
            query.include("follow");
            BmobQuery<User> innerQuery = new BmobQuery<>();
            innerQuery.addWhereEqualTo("objectId", targetUser.getObjectId());
            query.addWhereMatchesQuery("follow", "_User", innerQuery);
        }

        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        if (mDataHelper.setFollowUserList(list)) {
                            mNotifyData.postValue(null);
                        }
                    }
                } else {
                    toast("网络不给力");
                }

                showProgressDialog(false);
            }
        });
    }

    public void bindData(IDataBinder<FollowUserListDataHelper> dataBinder) {
        dataBinder.bind(mDataHelper);
    }

    public MutableLiveData<Void> getNotifyData() {
        return mNotifyData;
    }
}




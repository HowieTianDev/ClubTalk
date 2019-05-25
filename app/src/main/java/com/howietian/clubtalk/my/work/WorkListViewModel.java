package com.howietian.clubtalk.my.work;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.EventList;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.home.event.list.EventDataHelper;
import com.howietian.clubtalk.listener.IDataBinder;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class WorkListViewModel extends BaseViewModel {

    private MutableLiveData<Boolean> mNotifyFooter = new MutableLiveData<>();
    private MutableLiveData<Void> mNotifyData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNotifySwipeRefresh = new MutableLiveData<>();

    private EventDataHelper mDataHelper = new EventDataHelper();
    private int mType;
    User mUser = BmobUser.getCurrentUser(User.class);


    public void start(int type) {
        mType = type;
        requestEvent(0);
    }

    public void refresh() {
        mDataHelper.clear();
        requestEvent(0);
    }

    private void requestEvent(final int offset) {
        BmobQuery<Event> query = new BmobQuery<>();
        query.setSkip(offset)
                .setLimit(mDataHelper.getPageCount())
                .order("-createdAt");
        if (mType == WorkListActivity.TYPE_MY_COLLECT) {
            query.include("user,collect");
            BmobQuery<User> innerQuery = new BmobQuery<>();
            innerQuery.addWhereEqualTo("objectId", mUser.getObjectId());
            query.addWhereMatchesQuery("collect", "_User", innerQuery);

        } else if (mType == WorkListActivity.TYPE_MY_JOINED) {
            query.include("user,join");
            BmobQuery<User> innerQuery = new BmobQuery<>();
            innerQuery.addWhereEqualTo("objectId", mUser.getObjectId());
            query.addWhereMatchesQuery("join", "_User", innerQuery);

        } else if (mType == WorkListActivity.TYPE_MY_PUBLISH) {
            query.addWhereEqualTo("user", mUser);
        }

        query.findObjects(new FindListener<Event>() {
            @Override
            public void done(List<Event> list, BmobException e) {
                if (e == null) {
                    EventList eventList = new EventList(list);
                    if (mDataHelper.setEventList(eventList, offset)) {
                        mNotifyData.postValue(null);
                    }
                } else {
                    toast("网络开小差了");
                }
                mNotifyFooter.postValue(mDataHelper.isHasMore());
                if (offset == 0) {
                    mNotifySwipeRefresh.postValue(false);
                }
            }
        });
    }

    public void next() {
        if (mDataHelper.isHasMore()) {
            requestEvent(mDataHelper.getOffset());
        } else {
            mNotifyFooter.postValue(false);
        }
    }

    public void bindData(IDataBinder dataBinder) {
        dataBinder.bind(mDataHelper);
    }

    public MutableLiveData<Boolean> getNotifyFooter() {
        return mNotifyFooter;
    }

    public MutableLiveData<Void> getNotifyData() {
        return mNotifyData;
    }

    public MutableLiveData<Boolean> getNotifySwipeRefresh() {
        return mNotifySwipeRefresh;
    }


}

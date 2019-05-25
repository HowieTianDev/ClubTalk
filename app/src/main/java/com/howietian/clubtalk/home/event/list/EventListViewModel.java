package com.howietian.clubtalk.home.event.list;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.EventList;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.listener.IDataBinder;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class EventListViewModel extends BaseViewModel {

    private MutableLiveData<Boolean> mNotifyFooter = new MutableLiveData<>();
    private MutableLiveData<Void> mNotifyData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNotifySwipeRefresh = new MutableLiveData<>();

    private EventDataHelper mDataHelper = new EventDataHelper();
    private int mType;
    private User mTargetUser;

    public void start(int type, User targetUser) {
        mTargetUser = targetUser;
        mType = type;
        requestEvent(0);
    }

    public void refresh() {
        mDataHelper.clear();
//        mNotifyData.postValue(null);
        requestEvent(0);
    }

    private void requestEvent(final int offset) {
        BmobQuery<Event> query = new BmobQuery<>();
        query.setSkip(offset)
                .setLimit(mDataHelper.getPageCount())
                .order("-createdAt");

        if (mType == Event.TYPE_PERSON_PAGE && mTargetUser != null) {
            query.addWhereEqualTo("user", mTargetUser);
        } else {
            query.addWhereEqualTo("type", mType);
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

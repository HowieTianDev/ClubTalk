package com.howietian.clubtalk.home;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.EventList;

import java.util.List;
import java.util.Objects;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class HomeFragmentViewModel extends BaseViewModel {

    private MutableLiveData<List<Event>> mNotifyBannerData = new MutableLiveData<>();

    private List<Event> mEventList;

    public void start() {
        requestEvent();
    }

    private void requestEvent() {
        BmobQuery<Event> query = new BmobQuery<>();
        query.order("-createdAt")
                .addWhereEqualTo("type", Event.TYPE_BANNER)
                .findObjects(new FindListener<Event>() {
                    @Override
                    public void done(List<Event> list, BmobException e) {
                        if (e == null) {
                            EventList eventList = new EventList(list);
                            if (setEventList(eventList)) {
                                mNotifyBannerData.postValue(list);
                            }
                        } else {
                            toast("网络开小差了");
                        }
                    }
                });
    }

    private boolean setEventList(List<Event> list) {
        if (!Objects.equals(list, mEventList)) {
            mEventList = list;
            return true;
        }

        return false;
    }


    public MutableLiveData<List<Event>> getNotifyBannerData() {
        return mNotifyBannerData;
    }
}

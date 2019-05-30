package com.howietian.clubtalk.publish;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.EventList;
import com.howietian.clubtalk.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ChooseFeedEventViewModel extends BaseViewModel {

    private MutableLiveData<Void> mNotifyData = new MutableLiveData<>();

    private List<String> mFeedEventList = new ArrayList<>();
    private Map<String, String> mClubNameMap = new HashMap<>();

    private User mUser = BmobUser.getCurrentUser(User.class);

    public void start() {
        requestJoinedEvent();
    }

    private void requestJoinedEvent() {
        BmobQuery<Event> query = new BmobQuery<>();
        query.order("-createdAt");
        query.include("user,join");
        BmobQuery<User> innerQuery = new BmobQuery<>();
        innerQuery.addWhereEqualTo("objectId", mUser.getObjectId());
        query.addWhereMatchesQuery("join", "_User", innerQuery);
        query.findObjects(new FindListener<Event>() {
            @Override
            public void done(List<Event> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        mFeedEventList.clear();
                        mClubNameMap.clear();
                        for (int i = 0; i < list.size(); i++) {
                            mFeedEventList.add(list.get(i).getTitle());
                            if (list.get(i).getUser() != null) {
                                mClubNameMap.put(list.get(i).getTitle(), list.get(i).getUser().getNickName());
                            }
                        }
                        mNotifyData.postValue(null);
                    }

                } else {
                    toast("网络开小差了");
                }
            }
        });
    }

    public String getClubName(String eventName) {
        return mClubNameMap.get(eventName);
    }

    public List<String> getFeedEvent() {
        return mFeedEventList;
    }

    public MutableLiveData<Void> getNotifyData() {
        return mNotifyData;
    }
}

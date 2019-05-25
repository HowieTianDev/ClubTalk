package com.howietian.clubtalk.search;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.EventList;
import com.howietian.clubtalk.listener.IDataBinder;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchViewModel extends BaseViewModel {

    private MutableLiveData<Void> mNotifySearchResult = new MutableLiveData<>();

    private SearchResultDataHelper mDataHelper = new SearchResultDataHelper();

    public void search(final String keyword) {
        BmobQuery<Event> query = new BmobQuery<>();
        query.order("-createdAt")
                .findObjects(new FindListener<Event>() {
                    @Override
                    public void done(List<Event> list, BmobException e) {
                        if (e == null) {
                            if (list != null) {
                                EventList eventList = new EventList(list);
                                if (mDataHelper.setEventList(eventList, keyword)) {
                                    mNotifySearchResult.postValue(null);
                                } else {
                                    toast("暂时没有符合条件的数据哦~");
                                }
                            }

                        } else {
                            toast("网络开小差了");
                        }
                    }
                });
    }

    public void clear() {
        mDataHelper.clear();
        mNotifySearchResult.postValue(null);
    }

    public void bindData(IDataBinder<SearchResultDataHelper> dataBinder) {
        dataBinder.bind(mDataHelper);
    }


    public MutableLiveData<Void> getNotifySearchResult() {
        return mNotifySearchResult;
    }
}

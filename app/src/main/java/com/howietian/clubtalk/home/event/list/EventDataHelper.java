package com.howietian.clubtalk.home.event.list;

import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.EventList;

import java.util.Objects;

public class EventDataHelper {

    private int mOffset;
    private boolean mHasMore = true;
    private int mPageCount = 10;
    private EventList mEventList;

    public boolean setEventList(EventList list, int offset) {
        boolean flag;
        if (!Objects.equals(list, mEventList)) {
            if (offset == 0) {
                mEventList = list;
            } else {
                mEventList.addAll(list);
            }
            flag = true;
        } else {
            flag = false;
        }

        if (list == null || list.size() < mPageCount) {
            mHasMore = false;
        } else {
            mOffset += list.size();
        }

        return flag;
    }

    public int getOffset() {
        return mOffset;
    }

    public boolean isHasMore() {
        return mHasMore;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public int getCount() {
        if (mEventList == null) {
            return 0;
        }
        return mEventList.size();
    }

    public Event getItem(int position) {
        return mEventList.get(position);
    }

    public void clear() {
        mHasMore = true;
        mOffset = 0;
    }

}

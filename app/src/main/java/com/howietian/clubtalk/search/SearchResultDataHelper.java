package com.howietian.clubtalk.search;

import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.EventList;

import java.util.Objects;

public class SearchResultDataHelper {
    private EventList mEventList;

    public boolean setEventList(EventList list, String keyword) {
        if (!Objects.equals(list, mEventList)) {
            mEventList = filterList(list, keyword);
            if (mEventList.isEmpty()) {
                return false;
            }
            return true;
        }
        return false;
    }

    private EventList filterList(EventList list, String keyword) {
        EventList eventList = new EventList();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                Event event = list.get(i);
                if (event.getTitle() != null && event.getTitle().contains(keyword)) {
                    eventList.add(event);
                }
            }
        }
        return eventList;
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
        mEventList.clear();
    }
}

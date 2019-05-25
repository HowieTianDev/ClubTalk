package com.howietian.clubtalk.home.event.detail;

import com.howietian.clubtalk.bean.Event;

import java.util.Objects;

public class EventTopDataHelper {

    private Event mEvent;

    public boolean setEvent(Event event) {
        if (!Objects.equals(event, mEvent)) {
            mEvent = event;
            return true;
        }
        return false;
    }

    public Event getEvent() {
        return mEvent;
    }

    public void setLikeCount(int likeCount) {
        if (mEvent != null) {
            mEvent.setLikeCount(likeCount);
        }
    }

    public void setCommentCount(int commentCount) {
        if (mEvent != null) {
            mEvent.setCommentCount(commentCount);
        }
    }

}

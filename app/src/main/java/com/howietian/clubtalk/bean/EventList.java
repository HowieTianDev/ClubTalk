package com.howietian.clubtalk.bean;

import java.util.ArrayList;
import java.util.List;

public class EventList extends ArrayList<Event> {

    public EventList(){}

    public EventList(List<Event> list) {
        if (list != null && list.size() > 0) {
            for (Event event : list) {
                add(event);
            }
        }
    }

}

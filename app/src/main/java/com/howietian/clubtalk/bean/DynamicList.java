package com.howietian.clubtalk.bean;

import java.util.ArrayList;
import java.util.List;

public class DynamicList extends ArrayList<Dynamic> {
    public DynamicList() {
    }

    public DynamicList(List<Dynamic> list) {
        if (list != null && list.size() > 0) {
            for (Dynamic dynamic : list) {
                add(dynamic);
            }
        }
    }
}

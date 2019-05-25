package com.howietian.clubtalk.bean;

import java.util.ArrayList;
import java.util.List;

public class UserList extends ArrayList<User> {
    public UserList() {
    }

    public UserList(List<User> list) {
        if (list != null && list.size() > 0) {
            for (User user : list) {
                add(user);
            }
        }
    }
}

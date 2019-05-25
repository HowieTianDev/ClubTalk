package com.howietian.clubtalk.bean;

import java.util.ArrayList;
import java.util.List;

public class CommentList extends ArrayList<Comment> {
    public CommentList() {
    }

    public CommentList(List<Comment> list) {
        if (list != null && list.size() > 0) {
            for (Comment comment : list) {
                add(comment);
            }
        }
    }
}

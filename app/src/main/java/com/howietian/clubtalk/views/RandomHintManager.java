package com.howietian.clubtalk.views;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class RandomHintManager {

    private static final class RandomHintManagerHolder {

        private static final RandomHintManager instance = new RandomHintManager();

    }

    private final ArrayList<String> mHints = new ArrayList();

    {
        mHints.add("此刻，我们都是神评论");
        mHints.add("我有一支笔，足以写评论");
        mHints.add("我叫马上评，评论的评");
        mHints.add("多写评论，更能减肥");
        mHints.add("身为文化人，必须写评论");
        mHints.add("我思故我在，评论显真爱");
        mHints.add("对此，我必须讲几句");
        mHints.add("评论更显才华横溢");
        mHints.add("优秀，从评论开始");
        mHints.add("年少的欢喜，从评论开启");
        mHints.add("看帖不如评论，走起~");
        mHints.add("走心不走肾，看完有评论");
        mHints.add("神来之笔就差你");
        mHints.add("此情此景，我必须评论");
        mHints.add("吃饭睡觉写评论");
        mHints.add("因为评论，所以火热");
        mHints.add("只有评论，方显我心");
        mHints.add("走个心，评个论");
        mHints.add("评论才是人生态度");
    }

    private HashMap<Integer, Integer> mContextCountMap = new HashMap();

    private HashMap<Integer, String> mHintCacheMap = new HashMap();

    private RandomHintManager() {

    }

    public static final RandomHintManager get() {
        return RandomHintManagerHolder.instance;
    }

    public String getHint(int contextId) {
        String hint = mHintCacheMap.get(contextId);
        if (TextUtils.isEmpty(hint)) {
            hint = random();
            mHintCacheMap.put(contextId, hint);
        }

        Integer count = mContextCountMap.get(contextId);
        if (count == null) {
            count = 0;
        }
        count++;
        mContextCountMap.put(contextId, count);

        return hint;
    }

    public void removeHint(int contextId) {
        Integer count = mContextCountMap.get(contextId);
        if (count == null) {
            count = 0;
        }

        if (count - 1 <= 0) {
            mContextCountMap.remove(contextId);
            mHintCacheMap.remove(contextId);
        } else {
            mContextCountMap.put(contextId, count - 1);
        }
    }

    private String random() {
        return mHints.get(new Random().nextInt(mHints.size()));
    }
}
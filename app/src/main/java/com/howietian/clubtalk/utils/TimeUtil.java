package com.howietian.clubtalk.utils;

import android.text.TextUtils;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobUser;

/**
 * Created by 83624 on 2018/3/30 0030.
 */

public class TimeUtil {

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    public static final String PATTERN_NORMAL = "yyyy-MM-dd HH:mm:ss";

    public final static String END_JOIN = "报名截止";
    public final static String ON_JOIN = "正在报名";
    public final static String JOINED = "已报名";

    /**
     * 返回文字描述的日期
     *
     * @param date
     * @return
     */
    public static String getTimeFormatText(Date date) {
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

    public static String formatDate(Date date, String pattern) {
        if (date == null || TextUtils.isEmpty(pattern)) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static Date getSimpleDateFormat(String date, String pattern) {
        Date mdate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            mdate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mdate;
    }

    public static String getJoinStatus(Event event) {
        String status = ON_JOIN;
        String deadLine = event.getDeadLine();
        if(TextUtils.isEmpty(deadLine)){
            return "";
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date deadline = dateFormat.parse(deadLine);
            Date currentDate = new Date();
            String currentDateString = dateFormat.format(currentDate);

            if (currentDate.before(deadline) || deadLine.equals(currentDateString)) {
                status = ON_JOIN;
            } else {
                status = END_JOIN;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (BmobUser.isLogin()) {
            if (event.getJoinedIdList() != null) {
                if (event.getJoinedIdList().contains(BmobUser.getCurrentUser(User.class).getObjectId())) {
                    status = JOINED;
                }
            }
        }

        return status;
    }
}
package com.howietian.clubtalk.publish;

import android.text.TextUtils;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.Event;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PublishEventViewModel extends BaseViewModel {

    public void publish(final Event event) {
        if (event == null) {
            return;
        }
        if (checkData(event)) {
            showProgressText("正在上传...");
            showProgressDialog(true);

            event.getCover().uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        event.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    toast("发布成功");
                                    finishActivity();
                                } else {
                                    toast("发布失败");
                                }
                                showProgressDialog(false);
                            }
                        });
                    } else {
                        toast("图片上传失败");
                        showProgressDialog(false);
                    }
                }
            });
        }
    }

    private boolean checkData(Event event) {
        if (event == null) {
            return false;
        }
        if (TextUtils.isEmpty(event.getTitle())) {
            toast("标题不能为空");
            return false;
        } else if (TextUtils.isEmpty(event.getOrganizer())) {
            toast("主办方不能为空");
            return false;
        } else if (TextUtils.isEmpty(event.getPlace())) {
            toast("活动地点不能为空");
            return false;
        } else if (TextUtils.isEmpty(event.getStartTime())) {
            toast("活动时间不能为空");
            return false;
        } else if (TextUtils.isEmpty(event.getPhone())) {
            toast("联系方式不能为空");
            return false;
        } else if (TextUtils.isEmpty(event.getDeadLine())) {
            toast("请选择截止时间");
            return false;
        } else if (TextUtils.isEmpty(event.getContent())) {
            toast("活动描述不能为空");
            return false;
        } else if (event.getCover() == null) {
            toast("请选择一张封面");
            return false;
        }
        return true;
    }
}

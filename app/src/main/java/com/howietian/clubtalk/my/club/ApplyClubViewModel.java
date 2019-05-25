package com.howietian.clubtalk.my.club;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.utils.CommonUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ApplyClubViewModel extends BaseViewModel {

    private MutableLiveData<User> mNotifyUser = new MutableLiveData<>();

    public void start(User user) {
        mNotifyUser.postValue(user);
    }

    public void saveClubInfo(User user) {
        if (checkData(user)) {
            user.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        toast("恭喜您成为社团用户");
                        finishActivity();
                    } else {
                        toast("网络开小差了");
                    }
                }
            });
        }

    }

    private boolean checkData(User user) {
        if (user == null) {
            return false;
        }
        if (TextUtils.isEmpty(user.getNickName())) {
            toast("社团名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(user.getRealName())) {
            toast("真实姓名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(user.getSchool())) {
            toast("学校不能为空");
            return false;
        }
        if (TextUtils.isEmpty(user.getMobilePhoneNumber())) {
            toast("联系方式不能为空");
            return false;
        }

        if (!CommonUtil.isPhoneNum(user.getMobilePhoneNumber())) {
            toast("手机号码不合法");
            return false;
        }
        if (TextUtils.isEmpty(user.getClubIntro())) {
            toast("社团简介不能为空");
            return false;
        }
        return true;
    }

    public MutableLiveData<User> getNotifyUser() {
        return mNotifyUser;
    }
}

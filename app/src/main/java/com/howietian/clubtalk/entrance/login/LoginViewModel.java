package com.howietian.clubtalk.entrance.login;

import android.text.TextUtils;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.utils.CommonUtil;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginViewModel extends BaseViewModel {

    public void login(String phone, String pwd, final OnLoginListener loginListener) {
        if (checkData(phone, pwd)) {
            showProgressText("正在登录...");
            showProgressDialog(true);

            User user = new User();
            user.setUsername(phone);
            user.setPassword(pwd);

            user.login(new SaveListener<Object>() {
                @Override
                public void done(Object o, BmobException e) {
                    if (e == null) {
                        toast("登录成功~");
                        if(loginListener != null){
                            loginListener.onLoginSucceed();
                        }
                        finishActivity();
                    } else {
                        toast("用户名或密码错误");
                    }
                    showProgressDialog(false);
                }
            });
        }
    }

    private boolean checkData(String phone, String pwd) {
        if (TextUtils.isEmpty(phone)) {
            toast("手机号码不能为空");
            return false;
        } else if (TextUtils.isEmpty(pwd)) {
            toast("密码不能为空");
            return false;
        } else if (!CommonUtil.isPhoneNum(phone)) {
            toast("手机号码格式错误");
            return false;
        } else if (pwd.length() < 6) {
            toast("密码长度至少为6位");
            return false;
        }
        return true;
    }

}

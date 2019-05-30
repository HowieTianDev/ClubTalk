package com.howietian.clubtalk.entrance.register;

import android.os.CountDownTimer;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.utils.CommonUtil;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterViewModel extends BaseViewModel {

    private MutableLiveData<String> mNotifyGetCodeText = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNotifyGetCodeBg = new MutableLiveData<>();

    public void register(final String phone, String smsCode, final String nickName, final String pwd, final OnRegisterListener listener) {
        if (checkData(phone, smsCode, nickName, pwd)) {
            showProgressText("正在注册...");
            showProgressDialog(true);
            BmobSMS.verifySmsCode(phone, smsCode, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        final User user = new User();
                        user.setUsername(phone);
                        user.setPassword(pwd);
                        user.setNickName(nickName);
                        user.setMobilePhoneNumber(phone);

                        user.signUp(new SaveListener<User>() {
                            @Override
                            public void done(User user, BmobException e) {
                                if (e == null) {
                                    toast("注册成功");
                                    if (listener != null) {
                                        listener.registerSucceed(phone, pwd);
                                    }
                                    finishActivity();
                                } else {
                                    toast("注册失败，稍后再试");
                                }
                                showProgressDialog(false);
                            }
                        });
                    } else {
                        toast("验证码验证失败");
                        showProgressDialog(false);
                    }

                }
            });
        }
    }

    public void getSmsCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            toast("手机号码不能为空");
            return;
        } else if (!CommonUtil.isPhoneNum(phone)) {
            toast("手机号码格式不正确");
            return;
        }

        MyCounter counter = new MyCounter(60000, 1000);
        counter.start();
        BmobSMS.requestSMSCode(phone, "社团说", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    toast("发送验证码成功~");
                } else {
                    toast("发送验证码失败，请稍后再试~");
                }
            }
        });
    }

    private boolean checkData(String phone, String smsCode, String nickName, String pwd) {
        if (TextUtils.isEmpty(phone)) {
            toast("手机号码不能为空");
            return false;
        } else if (!CommonUtil.isPhoneNum(phone)) {
            toast("手机号码格式错误");
            return false;
        } else if (TextUtils.isEmpty(smsCode)) {
            toast("验证码不能为空");
            return false;
        } else if (TextUtils.isEmpty(pwd)) {
            toast("密码不能为空");
            return false;
        } else if (pwd.length() < 6) {
            toast("密码长度至少为6位");
            return false;
        } else if (TextUtils.isEmpty(nickName)) {
            toast("昵称不能为空");
            return false;
        } else if (!CommonUtil.isStringFormatCorrect(nickName)) {
            toast("昵称只能为数字、字母、下划线、中文");
            return false;
        }
        return true;
    }

    class MyCounter extends CountDownTimer {

        public MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            mNotifyGetCodeText.postValue("再次发送" + l / 1000 + "s");
            mNotifyGetCodeBg.postValue(false);
        }

        @Override
        public void onFinish() {
            mNotifyGetCodeText.postValue("重新发送");
            mNotifyGetCodeBg.postValue(true);
        }
    }

    public MutableLiveData<String> getNotifyGetCodeText() {
        return mNotifyGetCodeText;
    }

    public MutableLiveData<Boolean> getNotifyGetCodeBg() {
        return mNotifyGetCodeBg;
    }
}

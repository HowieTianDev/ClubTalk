package com.howietian.clubtalk.my.modify;

import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.utils.CommonUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class ModifyViewModel extends BaseViewModel {

    private MutableLiveData<String> mNotifyAvatar = new MutableLiveData<>();
    private MutableLiveData<String> mNotifyNickname = new MutableLiveData<>();
    private MutableLiveData<Boolean> mNotifyGender = new MutableLiveData<>();
    private MutableLiveData<String> mNotifyProfile = new MutableLiveData<>();
    private MutableLiveData<String> mNotifyRealName = new MutableLiveData<>();
    private MutableLiveData<String> mNotifySchool = new MutableLiveData<>();
    private MutableLiveData<String> mNotifyPhone = new MutableLiveData<>();
    private MutableLiveData<String> mNotifyClubIntro = new MutableLiveData<>();

    public void start() {
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            if (user.getAvatar() != null) {
                if (!TextUtils.isEmpty(user.getAvatar().getUrl())) {
                    mNotifyAvatar.postValue(user.getAvatar().getUrl());
                }
            }
            mNotifyGender.postValue(user.getGender());
            mNotifyNickname.postValue(user.getNickName());
            mNotifyProfile.postValue(user.getProfile());
            mNotifyPhone.postValue(user.getMobilePhoneNumber());
            mNotifySchool.postValue(user.getSchool());
            mNotifyRealName.postValue(user.getRealName());
            mNotifyClubIntro.postValue(user.getClubIntro());
        } else {
            finishActivity();
            toast("请先登录");
        }
    }

    public void saveInfo(User user) {
        if (user == null) {
            return;
        }
        if (TextUtils.isEmpty(user.getNickName())) {
            toast("昵称不能为空");
            return;
        } else if (!CommonUtil.isStringFormatCorrect(user.getNickName())) {
            toast("昵称只能为数字、字母、下划线、中文");
            return;
        }

        if (!CommonUtil.isPhoneNum(user.getMobilePhoneNumber())) {
            toast("手机号码不合法");
            return;
        }

        user.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toast("保存成功!");
                    finishActivity();
                } else {
                    toast("保存失败");
                }
            }
        });
    }

    public void uploadAvatar(BmobFile avatar, final String url) {
        showProgressText("正在上传...");
        showProgressDialog(true);
        avatar.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toast("上传成功!");
                    mNotifyAvatar.postValue(url);
                } else {
                    toast("上传失败！");
                }
                showProgressDialog(false);
            }
        });
    }

    public MutableLiveData<String> getNotifyAvatar() {
        return mNotifyAvatar;
    }

    public MutableLiveData<String> getNotifyNickname() {
        return mNotifyNickname;
    }

    public MutableLiveData<Boolean> getNotifyGender() {
        return mNotifyGender;
    }

    public MutableLiveData<String> getNotifyProfile() {
        return mNotifyProfile;
    }

    public MutableLiveData<String> getNotifyRealName() {
        return mNotifyRealName;
    }

    public MutableLiveData<String> getNotifySchool() {
        return mNotifySchool;
    }

    public MutableLiveData<String> getNotifyPhone() {
        return mNotifyPhone;
    }

    public MutableLiveData<String> getNotifyClubIntro() {
        return mNotifyClubIntro;
    }
}

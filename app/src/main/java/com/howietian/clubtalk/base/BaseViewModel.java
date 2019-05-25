package com.howietian.clubtalk.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {
    private MutableLiveData<Boolean> mNotifyProgress = new MutableLiveData<>();
    private MutableLiveData<String> mNotifyToast = new MutableLiveData<>();
    private MutableLiveData<String> mNotifyProgressText = new MutableLiveData<>();
    private MutableLiveData<Void> mNotifyFinish = new MutableLiveData<>();

    MutableLiveData<Boolean> getNotifyProgress() {
        return mNotifyProgress;
    }

    MutableLiveData<String> getNotifyToast() {
        return mNotifyToast;
    }

    MutableLiveData<String> getNotifyProgressText() {
        return mNotifyProgressText;
    }

    public MutableLiveData<Void> getNotifyFinish() {
        return mNotifyFinish;
    }

    public void toast(String s) {
        mNotifyToast.postValue(s);
    }

    public void showProgressDialog(boolean isShow) {
        mNotifyProgress.postValue(isShow);
    }

    public void showProgressText(String s) {
        mNotifyProgressText.postValue(s);
    }

    public void finishActivity() {
        mNotifyFinish.postValue(null);
    }
}

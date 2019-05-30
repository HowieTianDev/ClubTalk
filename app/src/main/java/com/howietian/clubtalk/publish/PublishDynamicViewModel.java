package com.howietian.clubtalk.publish;

import android.text.TextUtils;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.Dynamic;
import com.howietian.clubtalk.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class PublishDynamicViewModel extends BaseViewModel {

    public void publish(final Dynamic dynamic, final String[] imagePaths) {
        if (dynamic == null) {
            return;
        }

        if (TextUtils.isEmpty(dynamic.getContent())) {
            toast("动态不能为空哦");
            return;
        }

        showProgressText("正在发布...");
        showProgressDialog(true);
        if (imagePaths == null || imagePaths.length == 0) {
            publishDynamicOnlyText(dynamic);
        } else {
            publishDynamicWithImages(dynamic, imagePaths);
        }
    }

    private void publishDynamicWithImages(final Dynamic dynamic, final String[] imagePaths) {
        BmobFile.uploadBatch(imagePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                if (list1.size() == imagePaths.length) {
                    dynamic.setImageUrlList((ArrayList<String>) list1);
                    dynamic.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                updateUserDynamicCount();
                            } else {
                                toast("发布失败");
                            }
                            showProgressDialog(false);
                        }
                    });
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {

            }

            @Override
            public void onError(int i, String s) {
                showProgressDialog(false);
            }
        });
    }

    private void publishDynamicOnlyText(Dynamic dynamic) {
        dynamic.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    updateUserDynamicCount();
                } else {
                    toast("发布失败");
                }
                showProgressDialog(false);
            }
        });
    }

    private void updateUserDynamicCount() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {
            int count = user.getPublishCount();
            count++;
            user.setPublishCount(count);
            user.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        toast("发布成功");
                        finishActivity();
                    } else {
                        toast("发布失败");
                    }
                }
            });
        }
    }
}

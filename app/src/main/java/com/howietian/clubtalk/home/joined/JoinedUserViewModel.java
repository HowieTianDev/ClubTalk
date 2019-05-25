package com.howietian.clubtalk.home.joined;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.howietian.clubtalk.base.BaseViewModel;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.listener.IDataBinder;
import com.howietian.clubtalk.utils.ExcelUtils;
import com.howietian.clubtalk.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class JoinedUserViewModel extends BaseViewModel {

    private MutableLiveData<Void> mNotifyData = new MutableLiveData<>();

    private JoinedUserDataHelper mDataHelper = new JoinedUserDataHelper();

    private Context mContext;

    public void start(Event event, Context context) {
        mContext = context;
        requestJoinedUser(event);
    }

    private void requestJoinedUser(Event event) {
        showProgressText("正在查询...");
        showProgressDialog(true);
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereRelatedTo("join", new BmobPointer(event));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (mDataHelper.setJoinedUserList(list)) {
                        mNotifyData.postValue(null);
                    }
                } else {
                    toast("网络开小差了");
                }
                showProgressDialog(false);
            }
        });
    }

    public void bindData(IDataBinder<JoinedUserDataHelper> dataBinder) {
        dataBinder.bind(mDataHelper);
    }

    public void exportExcel(Event event) {
        String[] title = {"编号", "姓名", "学校", "联系方式", "社团说ID", "注册手机号"};

        File file = new File(FileUtil.getSDPath() + "/" + "社团说");
        FileUtil.makeDir(file);
        ExcelUtils.initExcel(file.toString() + "/" + event.getTitle() + "报名表.xls", title);
        String fileName = FileUtil.getSDPath() + "/" + "社团说" + "/" + event.getTitle() + "报名表.xls";
        ExcelUtils.writeObjListToExcel(transferData(mDataHelper.getUserList()), fileName, mContext);
    }

    private ArrayList<ArrayList<String>> transferData(List<User> userList) {
        ArrayList<ArrayList<String>> joinedExcel = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            int num = i + 1;
            ArrayList<String> beanList = new ArrayList<>();
            beanList.add(num + "");
            beanList.add(user.getRealName());
            beanList.add(user.getSchool());
            beanList.add(user.getMobilePhoneNumber());
            beanList.add(user.getNickName());
            beanList.add(user.getUsername());
            joinedExcel.add(beanList);
        }

        return joinedExcel;
    }


    public MutableLiveData<Void> getNotifyData() {
        return mNotifyData;
    }
}

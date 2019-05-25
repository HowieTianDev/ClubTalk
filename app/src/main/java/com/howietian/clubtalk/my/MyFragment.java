package com.howietian.clubtalk.my;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.howietian.clubtalk.R;
import com.howietian.clubtalk.base.BaseFragment;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.entrance.login.LoginActivity;
import com.howietian.clubtalk.my.club.ApplyClubActivity;
import com.howietian.clubtalk.my.dynamic.MyDynamicActivity;
import com.howietian.clubtalk.my.follow.FollowUserListActivity;
import com.howietian.clubtalk.my.modify.ModifyActivity;
import com.howietian.clubtalk.my.work.WorkListActivity;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends BaseFragment<MyViewModel> implements View.OnClickListener {
    private CircleImageView mAvatarImage;
    private TextView mNickNameText;
    private ImageView mVipImage;
    private LinearLayout mPublishLayout;
    private TextView mPublishCountText;
    private LinearLayout mFollowLayout;
    private TextView mFollowCountText;
    private LinearLayout mFanLayout;
    private TextView mFanCountText;
    private TextView mInfoText;
    private TextView mJoinText;
    private TextView mPublishActivityText;
    private TextView mClubText;
    private TextView mCollectText;
    private TextView mLogoutText;


    public MyFragment() {
        // Required empty public constructor
    }

    public static MyFragment newInstance() {
        Bundle bundle = new Bundle();

        MyFragment myFragment = new MyFragment();
        myFragment.setArguments(bundle);
        return myFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void beforeSetContent() {
        super.beforeSetContent();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findView();
        observe();
    }

    @Override
    public void onResume() {
        super.onResume();
        getViewModel().start();
    }

    private void findView() {
        mAvatarImage = getView().findViewById(R.id.image_avatar);
        mNickNameText = getView().findViewById(R.id.text_nickname);
        mVipImage = getView().findViewById(R.id.image_vip);

        mPublishLayout = getView().findViewById(R.id.layout_publish);
        mPublishCountText = getView().findViewById(R.id.text_publish_count);
        mFollowLayout = getView().findViewById(R.id.layout_follow_count);
        mFollowCountText = getView().findViewById(R.id.text_follow_count);
        mFanLayout = getView().findViewById(R.id.layout_fan_count);
        mFanCountText = getView().findViewById(R.id.text_fan_count);

        mInfoText = getView().findViewById(R.id.text_info);
        mJoinText = getView().findViewById(R.id.text_join);
        mPublishActivityText = getView().findViewById(R.id.text_publish_activity);
        mClubText = getView().findViewById(R.id.text_club);
        mCollectText = getView().findViewById(R.id.text_collect);
        mLogoutText = getView().findViewById(R.id.text_logout);

        mAvatarImage.setOnClickListener(this);
        mVipImage.setOnClickListener(this);
        mNickNameText.setOnClickListener(this);

        mPublishLayout.setOnClickListener(this);
        mFollowLayout.setOnClickListener(this);
        mFanLayout.setOnClickListener(this);

        mInfoText.setOnClickListener(this);
        mJoinText.setOnClickListener(this);
        mPublishActivityText.setOnClickListener(this);
        mClubText.setOnClickListener(this);
        mCollectText.setOnClickListener(this);
        mLogoutText.setOnClickListener(this);
    }

    private void observe() {

        getViewModel().getNotifyAvatar().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!TextUtils.isEmpty(s)) {
                    loadImage(s, mAvatarImage);
                }
            }
        });

        getViewModel().getNotifyIsClub().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mVipImage.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
            }
        });

        getViewModel().getNotifyNickName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mNickNameText.setText(s);
            }
        });

        getViewModel().getNotifyFanCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mFanCountText.setText(integer.toString());
            }
        });

        getViewModel().getNotifyFollowCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mFollowCountText.setText(integer.toString());
            }
        });

        getViewModel().getNotifyPublishCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                mPublishCountText.setText(integer.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == mAvatarImage || view == mVipImage || view == mNickNameText) {
            if (BmobUser.isLogin()) {
                ModifyActivity.launch(getActivity());
            } else {
                LoginActivity.launch(getActivity());
            }
        } else if (view == mPublishLayout) {
            if (BmobUser.isLogin()) {
                MyDynamicActivity.launch(getActivity());
            } else {
                showToast("请先登录哦~");
                LoginActivity.launch(getActivity());
            }
        } else if (view == mFollowLayout) {
            if (BmobUser.isLogin()) {
                FollowUserListActivity.launch(getActivity(), BmobUser.getCurrentUser(User.class), FollowUserListActivity.TYPE_MY_FOLLOW);
            } else {
                showToast("请先登录哦~");
                LoginActivity.launch(getActivity());
            }

        } else if (view == mFanLayout) {
            if (BmobUser.isLogin()) {
                FollowUserListActivity.launch(getActivity(), BmobUser.getCurrentUser(User.class), FollowUserListActivity.TYPE_MY_FAN);
            } else {
                showToast("请先登录哦~");
                LoginActivity.launch(getActivity());
            }
        } else if (view == mInfoText) {
            if (BmobUser.isLogin()) {
                ModifyActivity.launch(getActivity());
            } else {
                showToast("请先登录哦~");
                LoginActivity.launch(getActivity());
            }
        } else if (view == mJoinText) {
            if (BmobUser.isLogin()) {
                WorkListActivity.launch(getActivity(), WorkListActivity.TYPE_MY_JOINED);
            } else {
                showToast("请先登录哦~");
                LoginActivity.launch(getActivity());
            }
        } else if (view == mPublishActivityText) {
            if (BmobUser.isLogin()) {
                WorkListActivity.launch(getActivity(), WorkListActivity.TYPE_MY_PUBLISH);
            } else {
                showToast("请先登录哦~");
                LoginActivity.launch(getActivity());
            }
        } else if (view == mCollectText) {
            if (BmobUser.isLogin()) {
                WorkListActivity.launch(getActivity(), WorkListActivity.TYPE_MY_COLLECT);
            } else {
                showToast("请先登录哦~");
                LoginActivity.launch(getActivity());
            }
        } else if (view == mClubText) {
            if (BmobUser.isLogin()) {
                if (BmobUser.getCurrentUser(User.class).getClub()) {
                    showToast("您已经是社团用户了");
                } else {
                    ApplyClubActivity.launch(getActivity());
                }
            } else {
                showToast("请先登录哦~");
                LoginActivity.launch(getActivity());
            }
        } else if (view == mLogoutText) {
            logout();
        }
    }

    private void logout() {
        if (BmobUser.isLogin()) {
            AlertDialog dialog = new AlertDialog.Builder(getActivity())
                    .setTitle("温馨提示")
                    .setMessage("您确认要退出登录吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            BmobUser.logOut();
                            mAvatarImage.setImageResource(R.drawable.ic_account_circle_blue_grey_100_36dp);
                            mVipImage.setVisibility(View.GONE);
                            mNickNameText.setText("请登录");
                            mFanCountText.setText("0");
                            mFollowCountText.setText("0");
                            mPublishCountText.setText("0");
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create();
            dialog.show();
        } else {
            showToast("您还没有登录哦~");
        }

    }
}

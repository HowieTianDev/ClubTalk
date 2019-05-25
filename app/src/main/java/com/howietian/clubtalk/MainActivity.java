package com.howietian.clubtalk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.howietian.clubtalk.base.BaseActivity;
import com.howietian.clubtalk.circle.CircleFragment;
import com.howietian.clubtalk.home.HomeFragment;
import com.howietian.clubtalk.my.MyFragment;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    public static void launch(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private BottomNavigationBar mBottomNavigationBar;

    private HomeFragment mHomeFragment;
    private CircleFragment mCircleFragment;
    private MyFragment mMyFragment;

    private long mLastClickBackTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findView();
        initBottomNavigation();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void findView() {
        mBottomNavigationBar = findViewById(R.id.bottom_navigation);

    }

    private void initBottomNavigation() {

        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT)
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setActiveColor(R.color.colorPrimary)
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_explore_black_24dp, "圈子"))
                .addItem(new BottomNavigationItem(R.drawable.ic_face_black_24dp, "我的"))
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(this);

        selectTab(0);

    }

    @Override
    public void onTabSelected(int position) {
        selectTab(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void selectTab(int position) {
        hideFragments();

        Fragment tempFragment = null;
        switch (position) {
            case 0:
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.newInstance();
                }
                tempFragment = mHomeFragment;
                break;
            case 1:
                if (mCircleFragment == null) {
                    mCircleFragment = CircleFragment.newInstance();
                }
                tempFragment = mCircleFragment;
                break;
            case 2:
                if (mMyFragment == null) {
                    mMyFragment = MyFragment.newInstance();
                }
                tempFragment = mMyFragment;
                break;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!tempFragment.isAdded()) {
            fragmentManager.beginTransaction().add(R.id.layout_content, tempFragment).commit();
        } else if (tempFragment.isHidden()) {
            fragmentManager.beginTransaction().show(tempFragment).commit();
        }
    }

    private void hideFragments() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (mHomeFragment != null) {
            ft.hide(mHomeFragment);
        }
        if (mCircleFragment != null) {
            ft.hide(mCircleFragment);
        }
        if (mMyFragment != null) {
            ft.hide(mMyFragment);
        }
        ft.commit();
    }

    // 两秒内连续按退出键退出APP
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mLastClickBackTime > 2000) {
            Toast.makeText(this, "再点一次退出", Toast.LENGTH_SHORT).show();
            mLastClickBackTime = System.currentTimeMillis();
        } else { // 关掉app
            super.onBackPressed();
        }
    }
}

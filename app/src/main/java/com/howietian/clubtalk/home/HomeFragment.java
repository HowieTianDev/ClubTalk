package com.howietian.clubtalk.home;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.howietian.clubtalk.R;
import com.howietian.clubtalk.adapter.PageAdapter;
import com.howietian.clubtalk.base.BaseFragment;
import com.howietian.clubtalk.bean.Event;
import com.howietian.clubtalk.bean.User;
import com.howietian.clubtalk.entrance.login.LoginActivity;
import com.howietian.clubtalk.home.event.detail.EventDetailActivity;
import com.howietian.clubtalk.home.event.list.EventListFragment;
import com.howietian.clubtalk.publish.PublishDynamicActivity;
import com.howietian.clubtalk.publish.PublishEventActivity;
import com.howietian.clubtalk.search.SearchActivity;
import com.howietian.clubtalk.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment<HomeFragmentViewModel> implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private TextView mSearchText;
    private LinearLayout mPublishLayout;

    private Banner mBanner;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private PageAdapter mPagerAdapter;
    private Fragment mArticleFragment;
    private EventListFragment mActivityFragment;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        Bundle bundle = new Bundle();

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findView();
        initViews();

        observe();

        getViewModel().start();
    }

    private void findView() {
        mSearchText = getView().findViewById(R.id.text_search);
        mSearchText.setText("搜索你想要的活动~");
        mPublishLayout = getView().findViewById(R.id.layout_publish);

        mBanner = getView().findViewById(R.id.banner_home);
        mTabLayout = getView().findViewById(R.id.tabLayout_home);
        mViewPager = getView().findViewById(R.id.viewpager_person);

        mSearchText.setOnClickListener(this);
        mPublishLayout.setOnClickListener(this);
    }

    private void observe() {

        getViewModel().getNotifyBannerData().observe(this, new Observer<List<Event>>() {
            @Override
            public void onChanged(final List<Event> list) {

                mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                mBanner.setImageLoader(new GlideImageLoader());
                mBanner.setBannerAnimation(Transformer.Default);
                mBanner.setDelayTime(5000);
                mBanner.setIndicatorGravity(BannerConfig.CENTER);

                List<String> urls = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Event event = list.get(i);
                    if (event.getCover() != null && !TextUtils.isEmpty(event.getCover().getUrl())) {
                        urls.add(event.getCover().getUrl());
                    }
                }
                mBanner.setImages(urls);
                mBanner.setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                        String eventJson = new Gson().toJson(list.get(position));
                        intent.putExtra(EventDetailActivity.TAG_EVENT_DETAIL, eventJson);

                        EventDetailActivity.launch(getActivity(), intent);
                    }
                });
                mBanner.start();


            }
        });
    }


    private void initViews() {

        // 初始化 tabLayout
        List<String> titles = new ArrayList<>();
        titles.add("精选");
        titles.add("活动");

        List<Fragment> fragments = new ArrayList<>();
        if (mArticleFragment == null) {
            mArticleFragment = EventListFragment.newInstance(Event.TYPE_ELEGANT);
        }
        if (mActivityFragment == null) {
            mActivityFragment = EventListFragment.newInstance(Event.TYPE_EVENT);
        }

        fragments.add(mArticleFragment);
        fragments.add(mActivityFragment);

        mPagerAdapter = new PageAdapter(getChildFragmentManager(), fragments, titles);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public void onClick(View view) {
        if (view == mSearchText) {
            SearchActivity.launch(getActivity());
        } else if (view == mPublishLayout) {
            showPublishMenu(view);
        }
    }

    private void showPublishMenu(View v) {
        PopupMenu popup = new PopupMenu(getActivity(), v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_publish, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_activity:
                if (BmobUser.isLogin()) {
                    if (BmobUser.getCurrentUser(User.class).getClub()) {
                        PublishEventActivity.launch(getActivity());
                    } else {
                        showToast("只有社团用户才能发布活动哦，请前往个人中心申请");
                    }
                } else {
                    showToast("请先登录哦~");
                    LoginActivity.launch(getActivity());
                }
                break;
            case R.id.menu_dynamic:
                if (BmobUser.isLogin()) {
                    PublishDynamicActivity.launch(getActivity());
                } else {
                    showToast("请先登录哦~");
                    LoginActivity.launch(getActivity());
                }
                break;
        }
        return false;
    }
}

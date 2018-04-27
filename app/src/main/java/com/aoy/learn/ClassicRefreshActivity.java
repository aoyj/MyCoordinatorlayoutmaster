package com.aoy.learn;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.aoy.learn.adapter.ViewPagerItemAdapter;
import com.aoy.learn.fragment.MyBebaviorFragment;
import com.aoy.learn.widget.BannerLinearLayout;
import com.aoy.learn.widget.ClassicRefreshHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drizzt on 2018/4/27.
 */

public class ClassicRefreshActivity extends AppCompatActivity {


    @BindView(R.id.bottom_viewpager)
    ViewPager bottomViewpager;
    @BindView(R.id.refresh_header_layout)
    ClassicRefreshHeaderView refreshHeaderLayout;
    @BindView(R.id.top_view)
    BannerLinearLayout topView;

    List<Uri> mList = new ArrayList<>();
    ViewPagerItemAdapter mAdapter;
    ViewPager bannerViewPager;

    List<Fragment> mBotttomList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_refresh);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        bannerViewPager = topView.getViewPager();
        mList.add(new Uri.Builder()
                .scheme("res")
                .path(String.valueOf(R.mipmap.img_0))
                .build());
        mList.add(new Uri.Builder()
                .scheme("res")
                .path(String.valueOf(R.mipmap.img_1))
                .build());
        mList.add(new Uri.Builder()
                .scheme("res")
                .path(String.valueOf(R.mipmap.img_2))
                .build());
        mList.add(new Uri.Builder()
                .scheme("res")
                .path(String.valueOf(R.mipmap.img_3))
                .build());
        mList.add(new Uri.Builder()
                .scheme("res")
                .path(String.valueOf(R.mipmap.img_4))
                .build());

        mBotttomList.add(MyBebaviorFragment.newInstance());
        mBotttomList.add(MyBebaviorFragment.newInstance());
        mBotttomList.add(MyBebaviorFragment.newInstance());
        mBotttomList.add(MyBebaviorFragment.newInstance());
        mBotttomList.add(MyBebaviorFragment.newInstance());
        bottomViewpager.setAdapter(new viewPagerAdapter(getSupportFragmentManager()));
        mAdapter = new ViewPagerItemAdapter(mList);
        bannerViewPager.setAdapter(mAdapter);
        topView.getPagerIndicator().setBoxAcount(mList.size());
        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                topView.getPagerIndicator().setSelectedPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener
                (topView.getToolbarTab()));
        topView.getToolbarTab().setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener
                (bottomViewpager));
    }

    class viewPagerAdapter extends FragmentStatePagerAdapter {

        public viewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mBotttomList.get(position);
        }

        @Override
        public int getCount() {
            return mBotttomList.size();
        }
    }
}


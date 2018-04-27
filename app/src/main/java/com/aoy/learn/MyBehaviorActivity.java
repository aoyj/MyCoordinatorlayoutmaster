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
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drizzt on 2018/4/23.
 */

public class MyBehaviorActivity extends AppCompatActivity {

    @BindView(R.id.top_view)
    BannerLinearLayout topView;

    List<Uri> mList = new ArrayList<>();
    ViewPagerItemAdapter mAdapter;
    ViewPager viewPager;

    List<Fragment> mBotttomList = new ArrayList<>();
    @BindView(R.id.bottom_viewpager)
    ViewPager bottomViewpager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bihavior_2);
        ButterKnife.bind(this);
        init();
        Fresco.initialize(this);
    }

    private void init() {
        viewPager = topView.getViewPager();
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
        viewPager.setAdapter(mAdapter);
        topView.getPagerIndicator().setBoxAcount(mList.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
       // topView.getToolbarTab().setupWithViewPager(bottomViewpager);
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

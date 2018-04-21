package com.aoy.learn;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aoy.learn.adapter.ItemAdapter;
import com.aoy.learn.adapter.TopRecyclerviewAdapter;
import com.aoy.learn.adapter.ViewPagerItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drizzt on 2018/4/18.
 */

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.root_layout)
    CoordinatorLayout rootLayout;

    ItemAdapter itemAdapter;
    List<Integer> mList = new ArrayList<>();
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar_tab)
    TabLayout toolbarTab;
    ViewPagerItemAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mList.add(R.mipmap.img_1);
        mList.add(R.mipmap.img_2);
        mList.add(R.mipmap.img_3);
        mList.add(R.mipmap.img_4);
        itemAdapter = new ItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        mAdapter = new ViewPagerItemAdapter(mList);
        viewPager.setAdapter(mAdapter);
    }
}

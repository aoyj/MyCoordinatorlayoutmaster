package com.aoy.learn;

import android.content.Intent;
import android.net.Uri;
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
import com.aoy.learn.adapter.ViewPagerItemAdapter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by drizzt on 2018/4/18.
 */

public class MainActivity extends AppCompatActivity {

    public static MainActivity INSTANCE;

    @BindView(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.root_layout)
    CoordinatorLayout rootLayout;

    ItemAdapter itemAdapter;
    List<Uri> mList = new ArrayList<>();
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
        INSTANCE = this;
       // ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this).setDownsampleEnabled(true).build();
        Fresco.initialize(this);
    }

    private void init() {
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
        itemAdapter = new ItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);

        mAdapter = new ViewPagerItemAdapter(mList);
        viewPager.setAdapter(mAdapter);
        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                appBarLayout.setExpanded(true, true);
            }
        }, 1000);
    }

    public void toMyBehavior() {
        Intent intent = new Intent(this, MyBehaviorActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.app_bar_layout)
    public void onViewClicked() {
        toMyBehavior();
    }
}

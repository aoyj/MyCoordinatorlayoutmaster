package com.aoy.learn.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aoy.learn.R;
import com.aoy.learn.uitls.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drizzt on 2018/4/23.
 */

public class BannerLinearLayout extends LinearLayout {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.toolbar_tab)
    TabLayout toolbarTab;
    View view;
    @BindView(R.id.root_layout)
    LinearLayout rootLayout;
    @BindView(R.id.pager_indicator)
    PagerIndicator pagerIndicator;
    @BindView(R.id.relative_layout)
    RelativeLayout relativeLayout;

    public BannerLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public BannerLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BannerLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setMinimumHeight(Tools.dip2px(context, 248f));
        view = LayoutInflater.from(context).inflate(R.layout.layout_top_banner, this);
        ButterKnife.bind(this);
    }

    /**
     * {@link BannerLinearLayout}可滑动的最大距离
     *
     * @return
     */
    public int getTotalScrollRange() {
        int range = 0;
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) getChildAt(0)).getChildAt(0);
        for (int i = 0, z = viewGroup.getChildCount(); i < z; i++) {
            final View child = viewGroup.getChildAt(i);
            if (child instanceof ViewPager) {
                range = child.getMeasuredHeight();
                break;
            }
        }
        return range;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public View getScaleView() {
        return rootLayout;
    }

    public TabLayout getToolbarTab() {
        return toolbarTab;
    }

    public LinearLayout getMyRootLayout() {
        return rootLayout;
    }

    public View getView() {
        return view;
    }

    public PagerIndicator getPagerIndicator() {
        return pagerIndicator;
    }

    public RelativeLayout getRelativeLayout() {
        return relativeLayout;
    }
}

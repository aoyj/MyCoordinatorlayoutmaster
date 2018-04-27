package com.aoy.learn.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.aoy.learn.widget.BannerLinearLayout;

import java.util.List;

/**
 * Created by drizzt on 2018/4/23.
 */

public class MyScrollingViewBehavior extends MyHeaderScrollingViewBehavior<BannerLinearLayout> {

    public MyScrollingViewBehavior() {}

    public MyScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof BannerLinearLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        final CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior();
        if (behavior instanceof ScaleRefreshAppBarBehavior) {
            ViewCompat.offsetTopAndBottom(child, (dependency.getBottom() - child.getTop()));
        }
    }

    @Override
    BannerLinearLayout findFirstDependency(List<View> views) {
        for(View v : views){
            if(v instanceof BannerLinearLayout){
                return (BannerLinearLayout) v;
            }
        }
        return null;
    }

    @Override
   public int getScrollRange(View v) {
        if(v instanceof BannerLinearLayout){
            return ((BannerLinearLayout) v).getTotalScrollRange();
        }else {
            return super.getScrollRange(v);
        }
    }
}

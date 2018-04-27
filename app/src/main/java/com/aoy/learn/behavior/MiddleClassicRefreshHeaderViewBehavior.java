package com.aoy.learn.behavior;

import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.view.View;

import com.aoy.learn.widget.BannerLinearLayout;
import com.aoy.learn.widget.ClassicRefreshHeaderView;

import java.util.List;

/**
 * Created by drizzt on 2018/4/27.
 */

public class MiddleClassicRefreshHeaderViewBehavior extends ClassicRefreshHeaderViewBehavior<ClassicRefreshHeaderView> {

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ClassicRefreshHeaderView child, View dependency) {
        return dependency instanceof BannerLinearLayout;
    }

    @Override
    public void layoutChild(CoordinatorLayout parent, ClassicRefreshHeaderView child, int layoutDirection) {
        final List<View> dependencies = parent.getDependencies(child);
        final View header = findFirstDependency(dependencies);
        if(header != null){
            final CoordinatorLayout.LayoutParams lp =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            final Rect available = mTempRect1;
            available.set(parent.getPaddingLeft() + lp.leftMargin,
                    header.getBottom() + lp.topMargin,
                    parent.getWidth() - parent.getPaddingRight() - lp.rightMargin,
                    header.getBottom() + lp.topMargin + lp.bottomMargin + child.getMeasuredHeight());
            final Rect out = mTempRect2;
            GravityCompat.apply(resolveGravity(lp.gravity), child.getMeasuredWidth(),
                    child.getMeasuredHeight(), available, out, layoutDirection);
            child.layout(out.left, out.top, out.right, out.bottom );
        }else {
            super.layoutChild(parent, child, layoutDirection);
        }
    }

    @Override
    View findFirstDependency(List<View> views) {
        for(View v : views){
            if(v instanceof BannerLinearLayout){
                return (BannerLinearLayout) v;
            }
        }
        return null;
    }
}

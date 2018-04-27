package com.aoy.learn.behavior;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.aoy.learn.widget.BannerLinearLayout;

import java.lang.ref.WeakReference;

/**
 * Created by drizzt on 2018/4/27.
 */

public class ClassicRefreshTopBannerBehavior extends MyHeaderBehavior<BannerLinearLayout> {

    private WeakReference<View> mLastNestedScrollingChildRef;

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BannerLinearLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        final boolean started =  (axes & ViewCompat.SCROLL_AXIS_VERTICAL )!= 0 &&
                coordinatorLayout.getHeight() - directTargetChild.getHeight() <= child.getHeight();
        mLastNestedScrollingChildRef = null;
        return started;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BannerLinearLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if(dy != 0){
            int min = 0, max = 0;
            if (dy > 0) {
                min = -child.getTotalScrollRange();
                max = 0;
                if (min != max) {
                    consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
                }
            }
        }
    }
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BannerLinearLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (dyUnconsumed < 0) {
            if(!isHeaderFiling) {
                 scroll(coordinatorLayout, child, dyUnconsumed,
                        -child.getTotalScrollRange(), 0);
            }
        }
    }
    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BannerLinearLayout child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        mLastNestedScrollingChildRef = new WeakReference<>(target);
    }
    /**
     * 判断响应的NestedScrollingViewChild是否可以向下滑动
     * @param view
     * @return
     */
    @Override
    public boolean canDragView(BannerLinearLayout view) {
        if (mLastNestedScrollingChildRef != null) {
            final View scrollingView = mLastNestedScrollingChildRef.get();
            return scrollingView != null && scrollingView.isShown()
                    && !scrollingView.canScrollVertically(-1);
        } else {
            return true;
        }
    }

    @Override
    int getMaxDragOffset(BannerLinearLayout view) {
        return -view.getTotalScrollRange();
    }

    @Override
    int getScrollRangeForDragFling(BannerLinearLayout view) {
        return view.getTotalScrollRange();
    }
}

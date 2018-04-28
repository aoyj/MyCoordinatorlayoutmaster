package com.aoy.learn.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.aoy.learn.widget.BannerLinearLayout;

import java.lang.ref.WeakReference;

import static com.aoy.learn.behavior.ClassicRefreshBottomBehavior.TAG;


/**
 * Created by drizzt on 2018/4/27.
 */

public class ClassicRefreshTopBannerBehavior extends MyHeaderBehavior<BannerLinearLayout> {

    ClassicRefreshBottomBehavior bottomBehavior;

    public void setBottomBehavior(ClassicRefreshBottomBehavior bottomBehavior) {
        this.bottomBehavior = bottomBehavior;
    }

    public ClassicRefreshTopBannerBehavior() {}

    public ClassicRefreshTopBannerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private WeakReference<View> mLastNestedScrollingChildRef;

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BannerLinearLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        final boolean started =  (axes & ViewCompat.SCROLL_AXIS_VERTICAL )!= 0 &&
                coordinatorLayout.getHeight() - directTargetChild.getHeight() <= child.getHeight();
        mLastNestedScrollingChildRef = null;
        Log.i(TAG,"Banner onStartNestedScroll");
        return started;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BannerLinearLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.i(TAG,"Banner onNestedPreScroll");
        if(dy != 0){
            int min = 0, max = 0;
            if (dy > 0) {
                min = -child.getTotalScrollRange();
                max = 0;
                if (min != max) {
                    if(bottomBehavior != null && bottomBehavior.getTopAndBottomOffset() == 0){
                        consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
                    }else if(bottomBehavior == null){
                        consumed[1] = scroll(coordinatorLayout, child, dy, min, max);
                    }
                }
            }
        }
    }
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BannerLinearLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        Log.i(TAG,"Banner onNestedScroll");
        if (dyUnconsumed < 0) {
            if(!isHeaderFiling) {
                 scroll(coordinatorLayout, child, dyUnconsumed,
                        -child.getTotalScrollRange(), 0);
            }
        }
    }
    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BannerLinearLayout child, @NonNull View target, int type) {
        Log.i(TAG,"Banner onStopNestedScroll");
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

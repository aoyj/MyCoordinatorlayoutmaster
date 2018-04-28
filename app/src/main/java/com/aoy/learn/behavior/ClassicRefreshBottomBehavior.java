package com.aoy.learn.behavior;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.aoy.learn.refresh.ClassicRefreshUIHandler;
import com.aoy.learn.uitls.Tools;
import com.aoy.learn.widget.BannerLinearLayout;

import java.util.List;

/**
 * Created by drizzt on 2018/4/27.
 */

public class ClassicRefreshBottomBehavior extends MyHeaderScrollingViewBehavior<BannerLinearLayout> {

    private static final int MAX_OFFSET_ANIMATION_DURATION = 600; // ms

    public static final String TAG = "ClassicRefresh";

    ClassicRefreshTopBannerBehavior bannerBehavior;

    private ValueAnimator mOffsetAnimator;


    int minoffset;
    int maxoffset;

    View targetView;
    int startTargetViewTop;
    int refreshDiatance;
    boolean isRefreshing = false;

    ClassicRefreshUIHandler refreshUIHandler;

    public ClassicRefreshBottomBehavior() {}

    public ClassicRefreshBottomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        targetView = child;
        startTargetViewTop = targetView.getTop();
        minoffset =  0;
        maxoffset = minoffset + Tools.dip2px(child.getContext(),200f);
        refreshDiatance =  Tools.dip2px(child.getContext(),60f);
        findRefreshHeader(parent);
    }

    private void findRefreshHeader(CoordinatorLayout parent){
        if(refreshUIHandler == null){
            for(int i = 0 ; i < parent.getChildCount() ; i++){
                View child = parent.getChildAt(i);
                if(child instanceof ClassicRefreshUIHandler){
                    refreshUIHandler = (ClassicRefreshUIHandler) child;
                    break;
                }
            }
        }
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if(dependency instanceof BannerLinearLayout){
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) dependency.getLayoutParams();
            bannerBehavior = (ClassicRefreshTopBannerBehavior) lp.getBehavior();
            bannerBehavior.setBottomBehavior(this);
        }
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
        /*if (behavior instanceof ScaleRefreshAppBarBehavior) {
            ViewCompat.offsetTopAndBottom(child, (dependency.getBottom() - child.getTop()));
        }*/
        ViewCompat.offsetTopAndBottom(child, (dependency.getBottom() - child.getTop()) );
        mViewOffsetHelper.onViewLayout();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        final boolean started =  (axes & ViewCompat.SCROLL_AXIS_VERTICAL )!= 0;
        if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
            mOffsetAnimator.cancel();
        }
        isFlingBack = false;
        Log.i(TAG,"Bottom onStartNestedScroll");
        return started;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.i(TAG,"Bottom onNestedPreScroll");
        if(dy > 0 /*&& type == ViewCompat.TYPE_TOUCH*/){
             int realyConsumed = scroll(dy,minoffset,maxoffset);
             if(realyConsumed == 0 && getTopAndBottomOffset() == 0){
                 consumed[1] = 0;
             }else {
                 consumed[1] = dy;
             }
            Log.i("consumed","dy :" + dy +"消耗的dy:"+consumed[1]);
        }
    }

    boolean isFlingBack = false;

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if(dyUnconsumed < 0) {
            if (bannerBehavior != null && type == ViewCompat.TYPE_TOUCH) {
                if (bannerBehavior.getTopAndBottomOffset() == 0){
                    scroll(dyUnconsumed/2,minoffset,maxoffset);
                }
            }else if(type == ViewCompat.TYPE_NON_TOUCH && bannerBehavior.getTopAndBottomOffset() == 0 && !isFlingBack && !isRefreshing){
                isFlingBack = true;
                scroll(dyUnconsumed/2,minoffset,maxoffset);
               animateOffsetWithDuration(minoffset,500);
            }
        }
        Log.i(TAG,"Bottom onNestedPreScroll");
    }


    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        Log.i(TAG,"Bottom onStopNestedScroll");
       // setHeaderTopBottomOffset(minoffset ,minoffset,maxoffset);
        if(type == ViewCompat.TYPE_TOUCH){
            if(getTopAndBottomOffset() >= refreshDiatance){
                startRefresh();
            }else{
                if(!isRefreshing){
                    animateOffsetWithDuration(minoffset,300);
                }
            }
        }else{
            if(!isRefreshing){
                animateOffsetWithDuration(minoffset,500);
            }
        }
    }

    private void startRefresh(){
        isRefreshing = true;
        animateOffsetWithDuration(refreshDiatance,300);
        if(refreshUIHandler != null){
            refreshUIHandler.onStartRefresh();
        }
        targetView.postDelayed(new Runnable() {
            @Override
            public void run() {
                completeRefresh();
            }
        },4000);
    }

    public void completeRefresh(){
        isRefreshing = false;
        animateOffsetWithDuration(minoffset,300);
        if(refreshUIHandler != null){
            refreshUIHandler.onComplete();
        }
    }


    private void animateOffsetWithDuration(final int offset, final int duration) {
        final int currentOffset = getTopBottomOffsetForScrollingSibling();
        if (currentOffset == offset) {
            if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
                mOffsetAnimator.cancel();
            }
            return;
        }

        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setHeaderTopBottomOffset((int) animation.getAnimatedValue(),minoffset,maxoffset);
                }
            });
        } else {
            mOffsetAnimator.cancel();
        }

        mOffsetAnimator.setDuration(Math.min(duration, MAX_OFFSET_ANIMATION_DURATION));
        mOffsetAnimator.setIntValues(currentOffset, offset);
        mOffsetAnimator.start();
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

    public final int scroll(int dy, int minOffset, int maxOffset) {
        int scrollDistance  = setHeaderTopBottomOffset(getTopBottomOffsetForScrollingSibling() - dy, minOffset, maxOffset);
        if(refreshUIHandler != null){
            refreshUIHandler.onPrograssChange(getTopAndBottomOffset() * 100 / refreshDiatance);
        }
        return scrollDistance;
    }

    int setHeaderTopBottomOffset(int newOffset, int minOffset, int maxOffset) {
        final int curOffset = getTopAndBottomOffset();
        int consumed = 0;

        if (maxOffset != 0 && curOffset >= minOffset && curOffset <= maxOffset) {
            //如果newOffset 比minOffset小取minOffset 比maxOffset大 取 maxOffset
            newOffset = MathUtils.clamp(newOffset, minOffset, maxOffset);

            if (curOffset != newOffset) {
                setTopAndBottomOffset(newOffset);
                // Update how much dy we have consumed
                consumed = curOffset - newOffset;
            }
        }

        return consumed;
    }

    @Override
    public int getTopAndBottomOffset() {
        return super.getTopAndBottomOffset();
    }

    public int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset();
    }
}

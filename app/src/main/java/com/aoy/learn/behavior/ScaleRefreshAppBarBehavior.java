package com.aoy.learn.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.aoy.learn.refresh.OnRefreshListener;
import com.aoy.learn.uitls.Tools;
import com.aoy.learn.widget.BannerLinearLayout;

import java.lang.ref.WeakReference;

/**
 * Created by drizzt on 2018/4/21.
 */

public class ScaleRefreshAppBarBehavior extends MyHeaderBehavior<BannerLinearLayout> {
    public static  float MAX_SCALE_DISTANCE;
    public static float LIMIT_REFRESH;

    float mTotalDy;
    BannerLinearLayout targetView;
    View scaleView;
    float minScaleDistance;
    float maxScaleDistance;

    Boolean isScaling = false;
    Boolean isRefresh = false;
    float progress;

    LinearLayoutManager linearLayoutManager;

    private int InterceptLastMotionY;
    private int InterceptLastMotionX;

    private WeakReference<View> mLastNestedScrollingChildRef;

    OnRefreshListener mRefreshListener;

    public void setmRefreshListener(OnRefreshListener mRefreshListener) {
        this.mRefreshListener = mRefreshListener;
    }

    public ScaleRefreshAppBarBehavior() {}

    public ScaleRefreshAppBarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, BannerLinearLayout child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        MAX_SCALE_DISTANCE = Tools.dip2px(child.getContext(),200f);
        targetView = child;
        minScaleDistance = child.getMinimumHeight();
        maxScaleDistance = minScaleDistance+MAX_SCALE_DISTANCE;
        scaleView = child.getMyRootLayout();
        LIMIT_REFRESH = minScaleDistance + Tools.dip2px(child.getContext(),80);
    }

    /**
     * 当{@link ScaleRefreshAppBarBehavior#canPerformScale(int, int)} return true 就拦截MOVE事件，则后续的MOVE事
     * 件都在{@link ScaleRefreshAppBarBehavior#onTouchEvent(CoordinatorLayout, BannerLinearLayout, MotionEvent)}
     * 中处理
     * @param parent
     * @param child
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, BannerLinearLayout child, MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                InterceptLastMotionY = (int) ev.getY();
                InterceptLastMotionX = (int) ev.getX();
                if(getTopAndBottomOffset() == 0 && mLastNestedScrollingChildRef != null){
                    RecyclerView recyclerView = (RecyclerView) mLastNestedScrollingChildRef.get();
                    recyclerView.stopScroll();
                }
                break;
            case  MotionEvent.ACTION_MOVE:
                final int y = (int) ev.getY();
                final int x = (int) ev.getX();
                int dy = InterceptLastMotionY - y;
                int dx = InterceptLastMotionX - x;
                if(canPerformScale(dy,dx)){
                    isScaling = true;
                    return true;
                }
                break;
            case  MotionEvent.ACTION_UP:
            case  MotionEvent.ACTION_CANCEL:
                recorvy();
                break;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    /**
     * 判断能否下拉放大
     * 当垂直滑动 && child已经滑到顶部 && 没有执行刷新操作 return true
     * @param dy
     * @param dx
     * @return
     */
    private Boolean canPerformScale(int dy,int dx){
       return Math.abs(dx) < Math.abs(dy) && dy < 0 && getTopAndBottomOffset() == 0 && Math.abs(dy) > ViewConfiguration.getTouchSlop() && !isRefresh;
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, BannerLinearLayout child, MotionEvent ev) {
        if(isScaling){
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    final int y = (int) ev.getY();
                    int dy = InterceptLastMotionY - y;
                    InterceptLastMotionY = (int) ev.getY();
                    scale(-dy);
                    InterceptLastMotionY = y;
                    break;
                case  MotionEvent.ACTION_UP:
                case  MotionEvent.ACTION_CANCEL:
                    recorvy();
                    break;
            }
            return true;
        }
        return super.onTouchEvent(parent, child, ev);
    }

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
        int consumed = 0;
        if (dyUnconsumed < 0) {
           //dyUnconsumed的含义是滑动事件的发起者在垂直距离没有消耗的距离，典型的场景是reyclerView 滑到最顶部了在往下滑，recyclerView就不会消耗下滑的距离，dyUnconsumed就会有具体的值
            if(!isHeaderFiling) {
                consumed = scroll(coordinatorLayout, child, dyUnconsumed,
                        -child.getTotalScrollRange(), 0);
            }
        }
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BannerLinearLayout child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        mLastNestedScrollingChildRef = new WeakReference<>(target);
        linearLayoutManager = (LinearLayoutManager) ((RecyclerView)mLastNestedScrollingChildRef.get()).getLayoutManager();
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

    /**
     * 执行发大操作
     * @param dy
     */
    private void scale(int dy){
        isScaling = true;
        float add = dy/1.5f;
        mTotalDy += add;
        float temDistanse = mTotalDy + minScaleDistance;
        temDistanse = MathUtils.clamp(temDistanse,minScaleDistance,maxScaleDistance);

        ViewGroup.LayoutParams scaleViewLp =  scaleView.getLayoutParams();
        scaleViewLp.height = (int) temDistanse;
        scaleView.setLayoutParams(scaleViewLp);

        progress = (temDistanse / LIMIT_REFRESH) ;
        progress = MathUtils.clamp(progress,0f,1.0f);

        //放大的进度
        if(mRefreshListener != null){
            mRefreshListener.onPullProgress(progress);
        }
    }

    /**
     * {@link ScaleRefreshAppBarBehavior#recorvy()}在松手后开始刷新
     */
    private void startRefresh(){
        if(mRefreshListener != null){
            mRefreshListener.startRefresh();
        }
    }

    /**
     * 完成刷新
     */
    public void completeRefresh(){
        progress = 0f;
        isRefresh = false;
    }

    private void recorvy(){
        if(isScaling) {
            mTotalDy = 0 ;
            isScaling = false;
          //  targetView.setBottom((int) minTargetDistance);

            ViewGroup.LayoutParams scaleViewLp =  scaleView.getLayoutParams();
            scaleViewLp.height = (int) minScaleDistance;
            scaleView.setLayoutParams(scaleViewLp);
            if(progress >= 1.0f){
                startRefresh();
            }
        }
    }
}

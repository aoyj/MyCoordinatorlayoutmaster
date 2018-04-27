package com.aoy.learn.behavior;

import android.content.Context;
import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

/**
 * Created by drizzt on 2018/4/27.
 */

public abstract class ClassicRefreshHeaderViewBehavior<V extends View> extends MyViewOffsetBahavior<V> {

    protected final Rect mTempRect1 = new Rect();
    protected final Rect mTempRect2 = new Rect();

    public ClassicRefreshHeaderViewBehavior() {}

    public ClassicRefreshHeaderViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        final int childLpHeight = child.getLayoutParams().height;
        if (childLpHeight > 0 || childLpHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            final List<View> dependencies = parent.getDependencies(child);
            final View header = findFirstDependency(dependencies);
            if (header != null) {
                if (ViewCompat.getFitsSystemWindows(header)
                        && !ViewCompat.getFitsSystemWindows(child)) {
                    child.setFitsSystemWindows(true);
                    if (ViewCompat.getFitsSystemWindows(child)) {
                        child.requestLayout();
                        return true;
                    }
                }
                child.measure(parentWidthMeasureSpec,parentHeightMeasureSpec);
                final int height = child.getMeasuredHeight();
                final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,View.MeasureSpec.EXACTLY);
                parent.onMeasureChild(child, parentWidthMeasureSpec,
                        widthUsed, heightMeasureSpec, heightUsed);
            }
        }
        return false;
    }

    protected static int resolveGravity(int gravity) {
        return gravity == Gravity.NO_GRAVITY ? GravityCompat.START | Gravity.TOP : gravity;
    }

    abstract View findFirstDependency(List<View> views);

}

package com.aoy.learn.adapter;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.view.MotionEvent;

/**
 * Created by drizzt on 2018/4/21.
 */

public class MyAppBarBehavior extends AppBarLayout.Behavior {


    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        return super.onTouchEvent(parent, child, ev);
    }
}

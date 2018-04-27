package com.aoy.learn.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

/**
 * Created by drizzt on 2018/4/23.
 */

public class MyHeaderBehavior<V extends View> extends MyViewOffsetBahavior<V> {
    private static final int INVALID_POINTER = -1;
    public static final int INVALID_MOVE = Integer.MAX_VALUE;

    private Runnable mFlingRunnable;
    OverScroller mScroller;


    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;
    private int mLastMotionY;
    protected int mTouchSlop = -1;
    private VelocityTracker mVelocityTracker;
    protected boolean isHeaderFiling = false;

    protected int moveDy;
    protected int consumeDy;

    public MyHeaderBehavior() {}

    public MyHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    String tag = "";

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent ev) {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }

        final int action = ev.getAction();

        // Shortcut since we're being dragged
        if (action == MotionEvent.ACTION_MOVE && mIsBeingDragged) {
            return true;
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                //解决抖动问题
                if (mFlingRunnable != null) {
                    child.removeCallbacks(mFlingRunnable);
                    mFlingRunnable = null;
                }
                moveDy = INVALID_MOVE;
                consumeDy = INVALID_MOVE;
                isHeaderFiling = false;
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                if ((canDragView(child) && parent.isPointInChildBounds(child, x, y)) /*|| getTopAndBottomOffset() == 0*/) {
                    mLastMotionY = y;
                    mActivePointerId = ev.getPointerId(0);
                    ensureVelocityTracker();
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {

                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {

                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    break;
                }

                final int y = (int) ev.getY(pointerIndex);
                final int yDiff = Math.abs(y - mLastMotionY);
                if (yDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    mLastMotionY = y;
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            }
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(ev);
        }

        return mIsBeingDragged;
    }

    String touchTag = "";

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent ev) {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(parent.getContext()).getScaledTouchSlop();
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                mActivePointerId = INVALID_POINTER;
                if (parent.isPointInChildBounds(child, x, y) && canDragView(child)) {
                    mLastMotionY = y;
                    mActivePointerId = ev.getPointerId(0);
                    ensureVelocityTracker();

                } else {
                    return false;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    return false;
                }

                final int y = (int) ev.getY(activePointerIndex);
                int dy = mLastMotionY - y;

                if (!mIsBeingDragged && Math.abs(dy) > mTouchSlop) {
                    mIsBeingDragged = true;
                    if (dy > 0) {
                        dy -= mTouchSlop;
                    } else {
                        dy += mTouchSlop;
                    }
                }

                if (mIsBeingDragged) {
                    mLastMotionY = y;
                    moveDy = dy;
                    // We're being dragged so scroll the ABL
                   /* if(!isScaling) {
                        consumeDy = scroll(parent, child, dy, getMaxDragOffset(child), 0);
                    }*/
                    consumeDy = scroll(parent, child, dy, getMaxDragOffset(child), 0);
                }
                break;
            }

            case MotionEvent.ACTION_UP:
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(ev);
                    mVelocityTracker.computeCurrentVelocity(1000);
                    float yvel = mVelocityTracker.getYVelocity(mActivePointerId);
                    fling(parent, child, -getScrollRangeForDragFling(child), 0, yvel);
                }
                // $FALLTHROUGH
            case MotionEvent.ACTION_CANCEL: {
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;
            }
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(ev);
        }
        return true;
    }

    int setHeaderTopBottomOffset(CoordinatorLayout parent, V header, int newOffset) {
        return setHeaderTopBottomOffset(parent, header, newOffset,
                Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    int setHeaderTopBottomOffset(CoordinatorLayout parent, V header, int newOffset,
                                 int minOffset, int maxOffset) {
        final int curOffset = getTopAndBottomOffset();
        int consumed = 0;

        if (minOffset != 0 && curOffset >= minOffset && curOffset <= maxOffset) {
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

    public int getTopBottomOffsetForScrollingSibling() {
        return getTopAndBottomOffset();
    }

   public final int scroll(CoordinatorLayout coordinatorLayout, V header,
                     int dy, int minOffset, int maxOffset) {
        return setHeaderTopBottomOffset(coordinatorLayout, header,
                getTopBottomOffsetForScrollingSibling() - dy, minOffset, maxOffset);
    }

    final boolean fling(CoordinatorLayout coordinatorLayout, V layout, int minOffset,
                        int maxOffset, float velocityY) {
        if (mFlingRunnable != null) {
            layout.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }

        if (mScroller == null) {
            mScroller = new OverScroller(layout.getContext());
        }

        mScroller.fling(
                0, getTopAndBottomOffset(), // curr
                0, Math.round(velocityY), // velocity.
                0, 0, // x
                minOffset, maxOffset); // y

        if (mScroller.computeScrollOffset()) {
            isHeaderFiling = true;
            mFlingRunnable = new FlingRunnable(coordinatorLayout, layout);
            ViewCompat.postOnAnimation(layout, mFlingRunnable);
            return true;
        } else {
            onFlingFinished(coordinatorLayout, layout);
            return false;
        }
    }

    void onFlingFinished(CoordinatorLayout parent, V layout) {
        // no-op
    }

    public boolean canDragView(V view) {
        return false;
    }

    int getMaxDragOffset(V view) {
        return -view.getHeight();
    }

    int getScrollRangeForDragFling(V view) {
        return view.getHeight();
    }

    private void ensureVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final V mLayout;

        FlingRunnable(CoordinatorLayout parent, V layout) {
            mParent = parent;
            mLayout = layout;
        }

        @Override
        public void run() {
            if (mLayout != null && mScroller != null) {
                if (mScroller.computeScrollOffset()) {
                    setHeaderTopBottomOffset(mParent, mLayout, mScroller.getCurrY());
                    // Post ourselves so that we run on the next animation
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onFlingFinished(mParent, mLayout);
                }
            }
        }
    }
}

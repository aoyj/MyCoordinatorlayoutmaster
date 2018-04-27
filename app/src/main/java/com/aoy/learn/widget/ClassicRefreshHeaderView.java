package com.aoy.learn.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.aoy.learn.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drizzt on 2018/4/27.
 */

public class ClassicRefreshHeaderView extends RelativeLayout {
    @BindView(R.id.dh_circle_bar)
    CircleProgressBar dhCircleBar;
    @BindView(R.id.dh_animation_img)
    SimpleDraweeView dhAnimationImg;

    private AnimationDrawable refreshAnimation;
    Context mContext;


    public ClassicRefreshHeaderView(Context context) {
        super(context);
        init(context);
    }

    public ClassicRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ClassicRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_classic_refresh_header, this);
        ButterKnife.bind(this);
        dhAnimationImg.setImageResource(R.drawable.refresh_animation);
        refreshAnimation = (AnimationDrawable) dhAnimationImg.getDrawable();
        resetView();
    }

    private void resetView() {
        dhCircleBar.setVisibility(View.VISIBLE);
        refreshAnimation.stop();
        refreshAnimation.selectDrawable(0);
    }
}

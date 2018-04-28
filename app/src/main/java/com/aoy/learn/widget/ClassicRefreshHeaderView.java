package com.aoy.learn.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.math.MathUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.aoy.learn.R;
import com.aoy.learn.refresh.ClassicRefreshUIHandler;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by drizzt on 2018/4/27.
 */

public class ClassicRefreshHeaderView extends RelativeLayout implements ClassicRefreshUIHandler {
    @BindView(R.id.dh_circle_bar)
    CircleProgressBar dhCircleBar;
    @BindView(R.id.dh_animation_img)
    SimpleDraweeView dhAnimationImg;

    private AnimationDrawable refreshAnimation;
    Context mContext;

    boolean isRefresh = false;


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
        isRefresh = false;
        dhCircleBar.setVisibility(View.VISIBLE);
        refreshAnimation.stop();
        refreshAnimation.selectDrawable(0);
    }

    public void onPrograssChange(int progress){
        progress = MathUtils.clamp(progress,0,100);
        dhCircleBar.setProgress(progress);
    }

    public void onStartRefresh(){
        isRefresh = true;
        dhCircleBar.setVisibility(GONE);
        refreshAnimation.start();
    }

    public void onComplete(){
        resetView();
    }
}

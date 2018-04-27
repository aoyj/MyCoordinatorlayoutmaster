package com.aoy.learn.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by drizzt on 2018/4/8.
 */

public class PagerIndicator extends View {

    private int boxWidth = 20;
    private int boxHeight = 20;
    //boxSize 是圆点的外切方形的长宽
    private String normalColor   = "#ffffff";
    private String selectedColor = "#b3ffffff";
    private Paint mPaint;
    private Context mContext;
    private int boxAcount;
    private int childPadding = 7;
    private int selectedPosition = 0;

    public PagerIndicator(Context context) {
        super(context);
        init(context,null);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public PagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context,AttributeSet attrs){
        this.mContext = context;

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    public void setBoxAcount(int boxAcount) {
        this.boxAcount = boxAcount;
        requestLayout();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = boxWidth * boxAcount + (boxAcount -1) * childPadding + getPaddingStart() + getPaddingEnd();
        int height = boxHeight + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0; i < boxAcount; i++ ){
            if(i == selectedPosition){
                mPaint.setColor(Color.parseColor(selectedColor));
            }else{
                mPaint.setColor(Color.parseColor(normalColor));
            }
            int centerX = getPaddingStart() + i * boxWidth + i * childPadding + boxWidth/2;
            int centerY = getPaddingTop() + boxHeight /2;
            canvas.drawCircle(centerX,centerY,boxWidth/2,mPaint);
        }
    }
}

package com.aoy.learn.uitls;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by drizzt on 2018/4/24.
 */

public class Tools {

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取手机宽度和高度
     * @param context
     * @return 返回的是个数组，第一个字段是宽度，第二个字段是高度
     */
    public static Point getScreenWidthAndHeight(Context context){
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        return point;
    }
}

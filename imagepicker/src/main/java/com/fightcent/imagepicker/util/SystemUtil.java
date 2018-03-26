package com.fightcent.imagepicker.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

/**
 * Created by andy.guo on 2016/10/18.
 * 手机系统及硬件相关工具类
 */
public class SystemUtil {

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(@NonNull Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(@NonNull Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }



}

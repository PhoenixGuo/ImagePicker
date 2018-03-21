package com.fightcent.imagepicker.util;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by andy.guo on 2016/10/10.
 */
public class ToastUtil {

    private static ToastUtil mToastUtil;
    private Toast mToast = null;

    private ToastUtil() {

    }

    public static ToastUtil getInstance() {
        return mToastUtil;
    }

    static {
        mToastUtil = new ToastUtil();
    }

    public void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    public void showToast(Context context, int resId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(resId);
        }
        mToast.show();
    }

}

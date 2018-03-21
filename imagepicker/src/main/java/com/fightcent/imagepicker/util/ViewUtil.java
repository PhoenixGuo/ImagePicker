package com.fightcent.imagepicker.util;

import android.view.View;

/**
 * Created by andy.guo on 2018/2/2.
 */

public class ViewUtil {

    public static void hideView(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() == View.GONE) {
            return;
        }
        setVisibility(view, View.GONE);
    }

    public static void setViewInvisible(View view) {
        setVisibility(view, View.INVISIBLE);
    }

    public static void showView(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() == View.VISIBLE) {
            return;
        }
        setVisibility(view, View.VISIBLE);
    }

    private static void setVisibility(View view, int visibility) {
        if (view == null) {
            return;
        }
        view.setVisibility(visibility);
    }

}

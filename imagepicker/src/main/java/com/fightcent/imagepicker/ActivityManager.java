package com.fightcent.imagepicker;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by andy.guo on 2018/3/23.
 */

public class ActivityManager {

    private static Stack<Activity> sActivityStack;
    private static volatile ActivityManager sActivityManager;

    private ActivityManager() {
    }

    public static ActivityManager getAppManager() {
        if (sActivityManager == null) {
            synchronized (ActivityManager.class) {
                if (sActivityManager == null) {
                    sActivityManager = new ActivityManager();
                }
            }
        }
        return sActivityManager;
    }

    public void addActivity(Activity activity) {
        if (sActivityStack == null) {
            sActivityStack = new Stack<>();
        }
        sActivityStack.add(activity);
    }

    public void removeActivity(Activity activity) {
        if (activity != null && sActivityStack != null) {
            sActivityStack.remove(activity);
        }
    }

    public void finishAllActivity() {
        for (int i = 0, size = sActivityStack.size(); i < size; i++) {
            if (null != sActivityStack.get(i)) {
                sActivityStack.get(i).finish();
            }
        }
        sActivityStack.clear();
    }

}

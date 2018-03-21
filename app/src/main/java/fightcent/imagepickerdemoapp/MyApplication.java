package fightcent.imagepickerdemoapp;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by andy.guo on 2018/3/13.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLeakCanary();
    }

    private void initLeakCanary() {
        LeakCanary.install(this);
    }

}

package com.oodi.jingoo.application;

import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by pc on 5/8/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

    }

}
package com.v2cc.im.blah.base.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 10/26/2015.
 * If this class works, I created it. If not, I didn't.
 */
public class App extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

}
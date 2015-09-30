package com.v2cc.im.blah;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/30
 * If it works, I created this. If not, I didn't.
 */

public class ActivityCollector {
    public static List<AppCompatActivity> activities = new ArrayList<>();
    private long exitTime = 0;
    private long lastTime = 0;

    public static void addActivity(AppCompatActivity activity) {
        activities.add(activity);
    }

    public static void removeActivity(AppCompatActivity activity) {
        activities.remove(activity);
    }

    public void finishActivity(AppCompatActivity activity) {
        activities.remove(activity);
        activity.finish();
    }

    public void exitApp(Context oldActivity) {
        exitTime = System.currentTimeMillis() - lastTime;
        if (exitTime <= 2000) {
            // 释放所有Activity
            for (AppCompatActivity activity : ActivityCollector.activities) {
                activity.finish();
            }
            // 关闭所有正在执行的线程
            ThreadPoolUtil.closeAllThreadPool();
            System.exit(0);
        } else {
            lastTime = System.currentTimeMillis();
            Toast.makeText(oldActivity, "Please click again.", Toast.LENGTH_SHORT).show();
        }
    }
}

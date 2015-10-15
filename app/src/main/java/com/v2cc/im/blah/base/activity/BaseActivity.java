package com.v2cc.im.blah.base.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.v2cc.im.blah.base.utils.ActivityCollector;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/8/19
 * If it works, I created this. If not, I didn't.
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity", getClass().getSimpleName());

        // 创建了新的Activity，将其存入集合
        if (!ActivityCollector.activities.contains(this)) {
            ActivityCollector.addActivity(this);
        }

        // 初始化各种控件
        initViews();
        // 初始化mTitles、mFragments等ViewPager需要的数据
        initData();
        // 对各种控件进行设置、适配、填充数据
        configViews();
    }

    public void initViews() {
    }

    public void initData() {
    }

    public void configViews() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("BaseActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("BaseActivity", "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("BaseActivity", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("BaseActivity", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("BaseActivity", "onDestroy");
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("BaseActivity", "onRestart");
    }
}

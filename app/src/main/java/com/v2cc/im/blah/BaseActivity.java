package com.v2cc.im.blah;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.v2cc.im.blah.utils.ActivityCollector;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/8/19
 * If it works, I created this. If not, I didn't.
 */

public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(getClass().getSimpleName(), getClass().getSimpleName());

        // 创建了新的Activity，将其存入集合
        if (!ActivityCollector.activities.contains(this)) {
            ActivityCollector.addActivity(this);
        }

        // 初始化各种控件
        initView();
        // 初始化mTitles、mFragments等ViewPager需要的数据
        initData();
    }

    public void initView() {
    }

    public void initData() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(getClass().getSimpleName(), " on Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getClass().getSimpleName(), " on Resume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(getClass().getSimpleName(), " on Stop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(getClass().getSimpleName(), " on Pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(getClass().getSimpleName(), " on Destroy");
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(getClass().getSimpleName(), " on Restart");
    }
}

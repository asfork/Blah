package com.v2cc.im.blah.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/30.
 * If it works, I created this. If not, I didn't.
 */
public abstract class BaseFragment extends Fragment {
    protected View mRootView;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "onCreateView...");

        mRootView = inflater.inflate(setRootViewId(), container, false);
        initViews(mRootView);
        init();
        return mRootView;
    }

    /**
     * 设置根布局的资源id
     */
    protected int setRootViewId() {
        return 0;
    }

    /**
     * 初始化组件
     */
    protected void initViews(View rootView) {
    }

    /**
     * 初始化数据
     */
    protected void init() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(getClass().getSimpleName(), "on Create");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(getClass().getSimpleName(), "on Start");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(getClass().getSimpleName(), "on Resume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(getClass().getSimpleName(), "on Pause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(getClass().getSimpleName(), "on Stop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(getClass().getSimpleName(), "on Destroy View");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(getClass().getSimpleName(), "on Destroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(getClass().getSimpleName(), "on Detach");
        super.onDetach();
    }
}

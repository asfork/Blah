package com.v2cc.im.blah;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/30.
 * If it works, I created this. If not, I didn't.
 */
public class BaseFragment extends ListFragment {
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("BaseFragment", "onCreateView...");

        rootView = inflater.inflate(setRootViewId(), container, false);
        initViews(rootView);
        initData();
        return rootView;
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
    protected void initData() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("BaseFragment", "onCreate...");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d("BaseFragment", "onStart...");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("BaseFragment", "onResume...");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("BaseFragment", "onPause...");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d("BaseFragment", "onStop...");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d("BaseFragment", "onDestroyView...");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d("BaseFragment", "onDestroy...");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d("BaseFragment", "onDetach...");
        super.onDetach();
    }
}

package com.v2cc.im.blah.base.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.v2cc.im.blah.R;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/22.
 * If it works, I created this. If not, I didn't.
 */
public class FragmentTest extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static FragmentTest newInstance() {
        FragmentTest fragmentTest = new FragmentTest();
        Bundle bundle = new Bundle();
        fragmentTest.setArguments(bundle);
        return fragmentTest;
    }

    @Override
    protected int setRootViewId() {
        return R.layout.frag_test;
    }

    @Override
    protected void initViews(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.id_swiperefreshlayout);
    }

    @Override
    protected void configViews() {
        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary_light, R.color.primary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {

    }
}

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
public class FragmentTest extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static FragmentTest newInstance() {
        FragmentTest fragmentTest = new FragmentTest();
        Bundle bundle = new Bundle();
        fragmentTest.setArguments(bundle);
        return fragmentTest;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_test, container, false);
    }

    @Override
    public void onRefresh() {

    }
}

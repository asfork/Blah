package com.v2cc.im.blah.chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.db.DataBaseHelperUtil;
import com.v2cc.im.blah.message.MessageActivity;
import com.v2cc.im.blah.message.MessageBean;
import com.v2cc.im.blah.base.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class ChatFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, ChatStaggeredViewAdapter.OnItemClickListener {

    private ArrayList<MessageBean> mList;
    private ChatStaggeredViewAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final int SPAN_COUNT = 2;
    private final static int DATA_SUCCESS = 10000;// 数据查询成功标识

    private DataBaseHelperUtil util;

    public static ChatFragment newInstance(int position) {
        ChatFragment chatFragment = new ChatFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_POSITION, position);
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Override
    protected int setRootViewId() {
        return R.layout.frag_chat;
    }

    @Override
    protected void initViews(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.id_swiperefreshlayout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.id_recyclerview);

        mList = new ArrayList<MessageBean>();
        mAdapter = new ChatStaggeredViewAdapter(getActivity(), mList);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void configViews() {
        // 刷新时，指示器旋转后变化的颜色
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary_light, R.color.primary);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("name", mList.get(position).getName());
        bundle.putString("phoneNum", mList.get(position).getPhoneNum());
        // start up MessageActivity
        MessageActivity.actionStart(getActivity(), bundle);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onResume() {
        super.onResume();

        util = DataBaseHelperUtil.getInstance(getActivity());
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (mList == null) {
                    mList = new ArrayList<>();
                }
                mList.clear();
                mList.addAll(util.getRecentChat());

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                updateUI();
            }
        }.execute();
    }

    private void updateUI() {
        mAdapter.notifyDataSetChanged();
    }
}

package com.v2cc.im.blah.chat;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.base.fragment.BaseFragment;
import com.v2cc.im.blah.db.DataBaseHelperUtil;
import com.v2cc.im.blah.message.MessageActivity;
import com.v2cc.im.blah.message.MessageBean;

import java.util.ArrayList;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class ChatFragment extends BaseFragment implements ChatStaggeredViewAdapter.OnItemClickListener {

    private ArrayList<MessageBean> mList;
    private ChatStaggeredViewAdapter mAdapter;

    RecyclerView mRecyclerView;
    public RecyclerView.LayoutManager mLayoutManager;

    private static final int SPAN_COUNT = 2;

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
        return R.layout.frag_common;
    }

    @Override
    protected void initViews(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        mList = new ArrayList<MessageBean>();
        mAdapter = new ChatStaggeredViewAdapter(getActivity(), mList);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void configViews() {

        // 设置item动画，默认生效，可取消
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("name", mList.get(position).getName());
        bundle.putString("phone", mList.get(position).getPhone());
        // start up MessageActivity
        MessageActivity.actionStart(getActivity(), bundle);
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(getClass().getSimpleName(), mList.get(position).getName());

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... phone) {
                util.deleteMessageLogsByPhone(phone[0]);
                return null;
            }
        }.execute(mList.get(position).getPhone());
        mList.remove(position);
        mAdapter.notifyItemRemoved(position);

        // TODO snake bar
    }

    @Override
    public void onResume() {
        super.onResume();
        util = DataBaseHelperUtil.getInstance(getActivity());

        new AsyncTask<Void, Void, ArrayList<MessageBean>>() {
            @Override
            protected ArrayList<MessageBean> doInBackground(Void... params) {
                ArrayList<MessageBean> list = new ArrayList<>(util.getRecentChats());

                return list;
            }

            @Override
            protected void onPostExecute(ArrayList<MessageBean> result) {
                if (mList == null) {
                    mList = new ArrayList<MessageBean>();
                }
                mList.clear();
                mList.addAll(result);
            }
        }.execute();
        mAdapter.notifyDataSetChanged();
    }
}

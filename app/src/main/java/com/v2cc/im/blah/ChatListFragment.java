package com.v2cc.im.blah;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class ChatListFragment extends BaseFragment {
    private ListView mListView;// 聊天记录列表
    private ArrayList<MessageBean> mList;
    private DataBaseHelperUtil util;
    private ChatListViewAdapter mAdapter;

    public static ChatListFragment newInstance(int position) {
        ChatListFragment chatListFragment = new ChatListFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_POSITION, position);
        chatListFragment.setArguments(bundle);
        return chatListFragment;
    }

    @Override
    protected int setRootViewId() {
        return R.layout.frag_chat_list;
    }

    @Override
    protected void initViews(View rootView) {
        mListView = (ListView) rootView.findViewById(android.R.id.list);
    }


    @Override
    protected void initData() {
        mList = new ArrayList<MessageBean>();
        mAdapter = new ChatListViewAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
//        mListView.setOnItemClickListener(this);

        //Todo OnItemClickListener unavailable
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("ChatListFrag", "onItemClick");
                Bundle bundle = new Bundle();
                bundle.putString("name", mList.get(position).getName());
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        // 注册广播
//        if (mReceiver == null) {
//            mReceiver = new SMSBroadcastReceiver();
//        }
        util = DataBaseHelperUtil.getInstance(getActivity());
        // 打开数据库
        util.openDataBase();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(800);
//        intentFilter.addAction(mACTION);
//        getActivity().registerReceiver(mReceiver, intentFilter);
        // 查询数据并展示
        searchDataAndDisplay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销广播 关闭数据库
//        if (mReceiver != null) {
//            getActivity().unregisterReceiver(mReceiver);
//        }
        // 关闭数据库
        util.closeDataBase();
    }

    /**
     * 查询数据并展示
     * <p/>
     * 2015-8-7 上午11:23:10
     */
    private void searchDataAndDisplay() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mList.clear();
                mList.addAll(util.searchRecentChat());
            }
        };
        ThreadPoolUtil.insertTaskToCatchPool(runnable);
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.d("ChatListFrag","onItemClick");
//        Toast.makeText(getActivity(),
//                "position = " + position + "\n" + "第" + id
//                        + "行", Toast.LENGTH_SHORT).show();
//        Bundle bundle = new Bundle();
//        bundle.putString("name", mList.get(position).getName());
//        bundle.putString("passName", mList.get(position).getPassName());
//        showActivity(getActivity(), MessageActivity.class, bundle);
//        Intent intent = new Intent(getActivity(), MessageActivity.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }
}

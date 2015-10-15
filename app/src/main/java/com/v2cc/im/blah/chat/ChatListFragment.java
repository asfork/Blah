package com.v2cc.im.blah.chat;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.v2cc.im.blah.db.DataBaseHelperUtil;
import com.v2cc.im.blah.message.MessageActivity;
import com.v2cc.im.blah.message.MessageBean;
import com.v2cc.im.blah.R;
import com.v2cc.im.blah.base.utils.ThreadPoolUtil;
import com.v2cc.im.blah.base.fragment.BaseFragment;

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
        getChatList();
        mAdapter = new ChatListViewAdapter(getActivity(), mList);
        setListAdapter(mAdapter);
    }

    private void getChatList() {
        util = DataBaseHelperUtil.getInstance(getActivity());
        // 打开数据库
        util.openDataBase();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.setPriority(800);
        // 另开线程查询数据并展示
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mList.clear();
                mList.addAll(util.searchRecentChat());
            }
        };
        ThreadPoolUtil.insertTaskToCatchPool(runnable);
        // 关闭数据库
        util.closeDataBase();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Bundle bundle = new Bundle();
        bundle.putString("name", mList.get(position).getName());
        bundle.putString("phoneNum", mList.get(position).getPhoneNum());
        // start up MessageActivity
        MessageActivity.actionStart(getActivity(), bundle);
    }
}

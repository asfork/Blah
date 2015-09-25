package com.v2cc.im.blah;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class ChatListFragment extends ListFragment implements AdapterView.OnItemClickListener {
    private ListView mListView;// 聊天记录列表
    private ChatListAdapter mAdapter;
    private ArrayList<ChatListBean> mList;
    private DataBaseHelperUtil util;
    private final static int DATA_SUCCESS = 10000;// 数据查询成功标识
//    private static final String ARG_POSITION = "position";
//    private static final String mACTION = "android.provider.Telephony.SMS_RECEIVED";

    public static ChatListFragment newInstance(int position) {
        ChatListFragment chatFragment = new ChatListFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_POSITION, position);
        chatFragment.setArguments(bundle);
        return chatFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("chatFrag", "Hello in ChatListFragment onCreateView");

        View chatView = inflater.inflate(R.layout.frag_chat_list, container, false);
        mListView = (ListView) chatView.findViewById(android.R.id.list);

        mListView.setOnItemClickListener(this);

        mList = new ArrayList<ChatListBean>();
        mAdapter = new ChatListAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);

        return chatView;
    }

//    /**
//     * 存储接收到的消息
//     *
//     * @param from
//     * @param content
//     * @author yeliangliang
//     * @date 2015-8-6 下午4:32:42
//     * @version V1.0
//     * @return void
//     */
//    private void saveChatRecord(final String from, final String content) {
//        Runnable runnable = new Runnable() {
//
//            @Override
//            public void run() {
//                if (content.equals("")|| from .equals("")) {
//                    handler.obtainMessage(DATA_SUCCESS, util.searchNewRecord()).sendToTarget();
//                    return;
//                }
//                ChatsBean chatRecord = new ChatsBean();
//                chatRecord.setContent(content);
//                chatRecord.setName(from.split("@")[0]);
//                chatRecord.setPassName(from);
//                chatRecord.setSource("you");
//                chatRecord.setTime(System.currentTimeMillis() + "");
//                chatRecord.setStatus("0");
//                util.insertNewRecord(chatRecord);
//                util.insertToTable(DataBaseHelperUtil.TABLE_NAME_CHAT_RECORD, chatRecord);
//                // 查询数据并展示
//                handler.obtainMessage(DATA_SUCCESS, util.searchNewRecord()).sendToTarget();
//            }
//        };
//        ThreadPoolUtil.insertTaskToCatchPool(runnable);
//    }

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("name", mList.get(position).getName());
        bundle.putString("passName", mList.get(position).getPassName());
//        showActivity(getActivity(), ChatActivity.class, bundle);
//        Intent intent = new Intent(getActivity(), ChatActivity.class);
//        intent.putExtras(bundle);
//        startActivity(intent);
    }

    /**
     * 查询数据并展示
     * <p/>
     * 2015-8-7 上午11:23:10
     */
    private void searchDataAndDisplay() {
        // 查询数据并展示
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mList.clear();
                mList.addAll(util.searchNewRecord());
            }
        };
        ThreadPoolUtil.insertTaskToCatchPool(runnable);
    }
}

package com.v2cc.im.blah.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.v2cc.im.blah.db.DataBaseHelperUtil;
import com.v2cc.im.blah.R;
import com.v2cc.im.blah.base.utils.SMSUtil;
import com.v2cc.im.blah.base.view.StatusBarCompat;
import com.v2cc.im.blah.base.utils.ThreadPoolUtil;
import com.v2cc.im.blah.base.activity.BaseActivity;

import java.util.ArrayList;


/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/29.
 * If it works, I created this. If not, I didn't.
 */
public class MessageActivity extends BaseActivity implements OnClickListener {
    private Toolbar mToolbar;
    private String name;// 昵称
    private String phoneNum;
    private DataBaseHelperUtil dbUtil;
    private final static int DATA_SUCCESS = 10000;
    private ArrayList<MessageBean> messageHistories;// 聊天信息集合
    private ListView mListView;// 聊天信息列表
    private MessageListViewAdapter mAdapter;// 聊天信息列表适配器
    private FloatingActionButton mFloatingActionButton;

    public static void actionStart(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_message);
        mListView = (ListView) findViewById(R.id.listView_message);
        mToolbar = (Toolbar) findViewById(R.id.id_toolbar_message);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.id_floatingactionbutton_send);

        // 添加监听事件
        SetClick();
    }

    @Override
    public void initData() {
        messageHistories = new ArrayList<MessageBean>();
        // 获取聊天记录
        getMessageHistory();
        mAdapter = new MessageListViewAdapter(MessageActivity.this, messageHistories);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void configViews() {
        mToolbar.setTitle(name);
        // 透明状态栏
        StatusBarCompat.compat(this);
    }

    private void SetClick() {
        // 设置FloatingActionButton的点击事件
        mFloatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_floatingactionbutton_send:// 发送按钮
                postMessage(new MessageBean("", name, phoneNum,
                        System.currentTimeMillis() + "", "blah blah", "", "0", "0"));
                break;
        }
    }

    private void getMessageHistory() {
//        passName = getIntent().getStringExtra("passName");
        name = getIntent().getStringExtra("name");
        phoneNum = getIntent().getStringExtra("phoneNum");
        dbUtil = DataBaseHelperUtil.getInstance(this);

        // 打开数据库
        dbUtil.openDataBase();

        // 获取当前对象的聊天信息
        searchCurrentChatRecord();

        // 关闭数据库
        dbUtil.closeDataBase();
    }

    private void searchCurrentChatRecord() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                handler.obtainMessage(DATA_SUCCESS,
//                        dataBaseHelperUtil.searchMessageHistory(passName)).sendToTarget();
                if (messageHistories == null) {
                    messageHistories = new ArrayList<>();
                }
                messageHistories.clear();
                messageHistories.addAll(dbUtil.searchMessageHistory(name));
//                mListView.setSelection(messageHistories.size() - 1);
            }
        };
        ThreadPoolUtil.insertTaskToCatchPool(runnable);
    }

    private void postMessage(final MessageBean messageBean) {
        messageHistories.add(messageBean);
        mAdapter.notifyDataSetChanged();
//        mListView.setSelection(messageHistories.size() - 1);

        // Todo determines whether it should send message by sms
        SMSUtil.sendSMS(phoneNum);
        Log.d("postMessage", phoneNum);

        saveMessage(name, phoneNum, "blah blah", "0");
    }

    private void saveMessage(final String name, final String phoneNum, final String content, final String source) {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                MessageBean messageBean = new MessageBean();
                messageBean.setContent(content);
                messageBean.setName(name);
                messageBean.setPhoneNum(phoneNum);
                messageBean.setSource(source);
                messageBean.setTime(System.currentTimeMillis() + "");
                messageBean.setStatus("0");
                dbUtil.insertToTable(DataBaseHelperUtil.TABLE_NAME_MESSAGE,
                        messageBean);
                dbUtil.insertRecentChat(messageBean);
            }
        };
        ThreadPoolUtil.insertTaskToCatchPool(runnable);
    }
}

package com.v2cc.im.blah.message;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
    private FloatingActionButton mFloatingActionButton;
    private ArrayList<MessageBean> messageHistories;// 聊天信息集合
    private ListView mListView;// 聊天信息列表
    private MessageListViewAdapter mAdapter;// 聊天信息列表适配器
    private DataBaseHelperUtil util;
    private SMSUtil smsUtil;

    public static void actionStart(Context context, Bundle bundle) {
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_message);
        mListView = (ListView) findViewById(R.id.id_lv);

        messageHistories = new ArrayList<MessageBean>();
        mAdapter = new MessageListViewAdapter(MessageActivity.this, messageHistories);
        mListView.setAdapter(mAdapter);

        mToolbar = (Toolbar) findViewById(R.id.id_toolbar);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.id_floatingactionbutton);

        // 设置FloatingActionButton的点击事件
        mFloatingActionButton.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void configViews() {
        mToolbar.setTitle(name);
        // 透明状态栏
        StatusBarCompat.compat(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_floatingactionbutton:// 发送按钮
                postMessage(new MessageBean("", name, phoneNum,
                        System.currentTimeMillis() + "", "blah blah", "", "0", "0"));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        name = getIntent().getStringExtra("name");

        // TODO phone number formatting
        String s = getIntent().getStringExtra("phoneNum");
        phoneNum = s.replaceAll("-", "");

        util = DataBaseHelperUtil.getInstance(this);
        new AsyncTask<String, Void, Void>() {
            protected Void doInBackground(String... s) {
                if (messageHistories == null) {
                    messageHistories = new ArrayList<>();
                }
                messageHistories.clear();
                messageHistories.addAll(util.getMessageHistory(s[0]));
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                updateUI();
            }
        }.execute(phoneNum);
    }

    private void postMessage(final MessageBean messageBean) {
        messageHistories.add(messageBean);
        mAdapter.notifyDataSetChanged();
//        mListView.setSelection(messageHistories.size() - 1);

        // Todo determines whether it should send message by sms
        smsUtil = new SMSUtil();
        smsUtil.sendSMS(phoneNum);
        Log.d("postMessage", phoneNum);

        saveSMStoDB(name, phoneNum, "blah blah", "0");
    }

    private void saveSMStoDB(final String name, final String phoneNum, final String content, final String source) {

        MessageBean mb = new MessageBean();
        mb.setContent(content);
        mb.setName(name);
        mb.setPhoneNum(phoneNum);
        mb.setSource(source);
        mb.setTime(System.currentTimeMillis() + "");
        mb.setStatus("0");

        new AsyncTask<MessageBean, Void, Void>() {
            protected Void doInBackground(MessageBean... mb) {
                util.insertToTable(DataBaseHelperUtil.TABLE_NAME_MESSAGE, mb[0]);
                util.insertRecentChat(mb[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                updateUI();
            }
        }.execute(mb);
    }

    private void updateUI() {
        mAdapter.notifyDataSetChanged();
    }

}

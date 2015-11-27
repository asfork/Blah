package com.v2cc.im.blah;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.v2cc.im.blah.action.UserAction;
import com.v2cc.im.blah.bean.ApplicationData;
import com.v2cc.im.blah.bean.ChatEntity;
import com.v2cc.im.blah.bean.MessageTabEntity;
import com.v2cc.im.blah.database.ImDB;
import com.v2cc.im.blah.managers.ActivityCollector;
import com.v2cc.im.blah.utils.SMSUtil;
import com.v2cc.im.blah.views.adapters.MessageListViewAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/29.
 * If it works, I created this. If not, I didn't.
 */
public class ChatActivity extends BaseActivity implements OnClickListener {
    private Toolbar mToolbar;
    private FloatingActionButton mFAB;
    private List<ChatEntity> mMessageHistories;// 聊天信息集合
    private ListView mMessageListView;// 聊天信息列表
    private MessageListViewAdapter mAdapter;// 聊天信息列表适配器
    private SMSUtil mSMSUtil;
    private Handler handler;

    private int friendId;
    private String friendName;

    public static void actionStart(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.activity_chat);

        mMessageListView = (ListView) findViewById(R.id.lv);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFAB = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public void initEvents() {
        friendId = getIntent().getIntExtra("friendId", 0);
        Log.d("Chat", friendId + "");
        friendName = getIntent().getStringExtra("friendName");

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mAdapter.notifyDataSetChanged();
                        mMessageListView.setSelection(mMessageHistories.size());
                        break;
                    default:
                        break;
                }
            }
        };
        ApplicationData.getInstance().setChatHandler(handler);
        mMessageHistories = ApplicationData.getInstance().getChatMessagesMap().get(friendId);
        if (mMessageHistories == null) {
            mMessageHistories = ImDB.getInstance(ChatActivity.this).getChatMessage(friendId);
            ApplicationData.getInstance().getChatMessagesMap().put(friendId, mMessageHistories);
        }
        mAdapter = new MessageListViewAdapter(ChatActivity.this, mMessageHistories);
        mMessageListView.setAdapter(mAdapter);

        mToolbar.setTitle(friendName);
        setSupportActionBar(mToolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 设置FloatingActionButton的点击事件
        mFAB.setOnClickListener(this);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(getClass().getSimpleName(),"click home");
                ActivityCollector.finishActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:// 发送按钮
                String content = "blah blah";
                ChatEntity chatMessage = new ChatEntity();
                chatMessage.setContent(content);
                chatMessage.setSenderId(ApplicationData.getInstance()
                        .getUserInfo().getId());
                chatMessage.setReceiverId(friendId);
                chatMessage.setMessageType(ChatEntity.SEND);
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
                String sendTime = sdf.format(date);
                chatMessage.setSendTime(sendTime);
                mMessageHistories.add(chatMessage);
                mAdapter.notifyDataSetChanged();
                mMessageListView.setSelection(mMessageHistories.size());
                UserAction.sendMessage(chatMessage);
                ImDB.getInstance(ChatActivity.this)
                        .saveChatMessage(chatMessage);
                break;
        }
    }

    private void postMessage(ChatEntity chatEntity) {
        mMessageHistories.add(chatEntity);
        mMessageListView.setSelection(mMessageHistories.size() - 1);
        mAdapter.refresh(mMessageHistories);

        // Todo determines whether it should send message by sms
        mSMSUtil = new SMSUtil();
//        mSMSUtil.sendSMS(phone);
//        Log.d("postMessage", phone);

        // insert sms to database
        new AsyncTask<MessageTabEntity, Void, Void>() {
            protected Void doInBackground(MessageTabEntity... mb) {
//                mDBUtil.insertRecentChats(mb[0]);
//                mDBUtil.insertMessage(DataBaseHelper.TABLE_NAME_MESSAGE_LOGS, mb[0]);
                return null;
            }
        }.execute();
    }
}

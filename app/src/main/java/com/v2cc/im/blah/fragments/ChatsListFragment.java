package com.v2cc.im.blah.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import com.v2cc.im.blah.ChatActivity;
import com.v2cc.im.blah.R;
import com.v2cc.im.blah.action.UserAction;
import com.v2cc.im.blah.bean.ApplicationData;
import com.v2cc.im.blah.bean.MessageTabEntity;
import com.v2cc.im.blah.database.ImDB;
import com.v2cc.im.blah.global.Result;
import com.v2cc.im.blah.views.adapters.ChatStaggeredViewAdapter;

import java.util.ArrayList;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class ChatsListFragment extends BaseFragment implements ChatStaggeredViewAdapter.OnItemClickListener {
    private MessageTabEntity mChatEntity;
    private ArrayList<MessageTabEntity> mChatsList;
    private RecyclerView mRecyclerView;
    private ChatStaggeredViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Handler handler;

    private int mPosition;
    private static final int SPAN_COUNT = 2;

    public static ChatsListFragment newInstance(int position) {
        ChatsListFragment chatsListFragment = new ChatsListFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_POSITION, position);
        chatsListFragment.setArguments(bundle);
        return chatsListFragment;
    }

    @Override
    protected int setRootViewId() {
        return R.layout.frag_common;
    }

    @Override
    protected void initViews(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
    }

    @Override
    protected void init() {
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mAdapter.notifyDataSetChanged();
//                        mMessageListView.setSelection(mMessageEntityList.size());
                        break;
                    default:
                        break;
                }
            }
        };
        ApplicationData.getInstance().setMessageHandler(handler);
        mChatsList = ApplicationData.getInstance().getMessageEntities();

        mAdapter = new ChatStaggeredViewAdapter(getActivity(), mChatsList);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // 设置item动画，默认生效，可取消
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onItemClick(View view, int position) {
        mChatEntity = mChatsList.get(position);
        mChatEntity.setUnReadCount(0);
        mAdapter.notifyDataSetChanged();
        ImDB.getInstance(getActivity()).updateMessages(mChatEntity);
        mPosition = position;
        if (mChatEntity.getMessageType() == MessageTabEntity.MAKE_FRIEND_REQUEST)
            requestDialog();
        else if (mChatEntity.getMessageType() == MessageTabEntity.MAKE_FRIEND_RESPONSE_ACCEPT) {

        } else {
            Bundle bundle = new Bundle();
            bundle.putString("friendName", mChatEntity.getName());
            bundle.putInt("friendId", mChatEntity.getSenderId());
            ChatActivity.actionStart(getActivity(), bundle);
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Log.d(getClass().getSimpleName(), mChatsList.get(position).getName());
        // TODO add long click event
    }

    protected void requestDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Would you like accept the friend request?");
        builder.setTitle("New friend request");
        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                UserAction.sendFriendRequest(
                        Result.FRIEND_REQUEST_RESPONSE_ACCEPT,
                        mChatEntity.getSenderId());
                mChatsList.remove(mPosition);
                ImDB.getInstance(getActivity()).deleteMessage(mChatEntity);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                UserAction.sendFriendRequest(
                        Result.FRIEND_REQUEST_RESPONSE_REJECT,
                        mChatEntity.getSenderId());
                mChatsList.remove(mPosition);
                ImDB.getInstance(getActivity()).deleteMessage(
                        mChatEntity);
                mAdapter.notifyDataSetChanged();
            }
        });
        builder.create().show();
    }
}

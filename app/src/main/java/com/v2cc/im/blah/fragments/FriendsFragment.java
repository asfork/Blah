package com.v2cc.im.blah.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.v2cc.im.blah.FriendsDetailActivity;
import com.v2cc.im.blah.R;
import com.v2cc.im.blah.bean.ApplicationData;
import com.v2cc.im.blah.bean.User;
import com.v2cc.im.blah.views.adapters.FriendsRecyclerViewAdapter;

import java.util.List;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class FriendsFragment extends BaseFragment implements FriendsRecyclerViewAdapter.OnItemClickListener {
    private List<User> mFriendsList;
    private RecyclerView mFriendsListView;
    private FriendsRecyclerViewAdapter mAdapter;
    private Handler handler;

    public static FriendsFragment newInstance(int position) {
        FriendsFragment friendsFragment = new FriendsFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_POSITION, position);
        friendsFragment.setArguments(bundle);
        return friendsFragment;
    }

    @Override
    protected int setRootViewId() {
        return R.layout.frag_common;
    }

    @Override
    protected void initViews(View rootView) {
        mFriendsListView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
    }

    @Override
    protected void init() {
        mFriendsList = ApplicationData.getInstance().getFriendList();
        mAdapter = new FriendsRecyclerViewAdapter(getActivity(), mFriendsList);
        mFriendsListView.setAdapter(mAdapter);
        mFriendsListView.setLayoutManager(new LinearLayoutManager(mFriendsListView.getContext()));

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        mAdapter.notifyDataSetChanged();
//                        mFriendsListView.setSelection(mFriendsList.size());
                        break;
                    default:
                        break;
                }
            }
        };
        ApplicationData.getInstance().setfriendListHandler(handler);

        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        User friend = new User();
        friend.setId(mFriendsList.get(position).getId());
        friend.setUserName(mFriendsList.get(position).getUserName());
        friend.setAccount(mFriendsList.get(position).getAccount());
        friend.setPhoto(mFriendsList.get(position).getPhoto());
        bundle.putSerializable("friend", friend);
        FriendsDetailActivity.actionStart(getActivity(), bundle);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}

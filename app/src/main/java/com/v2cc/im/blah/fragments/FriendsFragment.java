package com.v2cc.im.blah.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.v2cc.im.blah.FriendsDetailActivity;
import com.v2cc.im.blah.R;
import com.v2cc.im.blah.db.DataBaseHelper;
import com.v2cc.im.blah.models.FriendsBean;
import com.v2cc.im.blah.views.adapters.FriendsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class FriendsFragment extends BaseFragment implements FriendsRecyclerViewAdapter.OnItemClickListener {

    private static DataBaseHelper util;
    private List<FriendsBean> mList;
    private FriendsRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;

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
    protected void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        mList = new ArrayList<FriendsBean>();
        adapter = new FriendsRecyclerViewAdapter(getActivity(), mList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    @Override
    protected void initData(){
        util = DataBaseHelper.getInstance(getActivity());

        new AsyncTask<Void, Void, ArrayList<FriendsBean>>() {
            @Override
            protected ArrayList<FriendsBean> doInBackground(Void... params) {
                return util.getFriendsList();
            }

            @Override
            protected void onPostExecute(ArrayList<FriendsBean> result) {
                if (mList == null) {
                    mList = new ArrayList<FriendsBean>();
                }
                // sort list
                if (!result.isEmpty()) {
                    Collections.sort(result, new Comparator<FriendsBean>() {
                        @Override
                        public int compare(FriendsBean preObj, FriendsBean nextObj) {
                            return preObj.getSortKey().trim().compareTo(nextObj.getSortKey().trim());
                        }
                    });
                }

                mList.clear();
                mList.addAll(result);
            }
        }.execute();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("name", mList.get(position).getName());
        bundle.putString("phone", mList.get(position).getPhone());
        // start up MessageActivity
        FriendsDetailActivity.actionStart(getActivity(), bundle);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}

package com.v2cc.im.blah.friends;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.base.fragment.BaseFragment;
import com.v2cc.im.blah.message.MessageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class FriendsFragment extends BaseFragment implements FriendsRecyclerViewAdapter.OnItemClickListener {

    private static List<FriendsBean> mList;
    static Map<Integer, FriendsBean> friendIdMap = null;
    private static FriendsRecyclerViewAdapter adapter;

    private RecyclerView recyclerView;
    AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象

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
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

        mList = new ArrayList<FriendsBean>();
        adapter = new FriendsRecyclerViewAdapter(getActivity(), mList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        // 实例化
        asyncQueryHandler = new MyAsyncQueryHandler(getActivity().getContentResolver());

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = {ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY};
        // 按照sort_key升序查詢
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");
    }

    @Override
    public void configViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    private static class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            Log.d("FriendsFrag", "QueryComplete");
            super.onQueryComplete(token, cookie, cursor);

            if (cursor != null && cursor.getCount() > 0) {
                friendIdMap = new HashMap<Integer, FriendsBean>();
                cursor.moveToFirst(); // 游标移动到第一项
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(1);
                    String number = cursor.getString(2);
                    String sortKey = cursor.getString(3);
                    int friendId = cursor.getInt(4);
                    Long photoId = cursor.getLong(5);
                    String lookUpKey = cursor.getString(6);

                    if (friendIdMap.containsKey(friendId)) {
                        // 无操作
                    } else {
                        // 创建联系人对象
                        FriendsBean contact = new FriendsBean();
                        contact.setDesplayName(name);
                        contact.setPhoneNum(number);
                        contact.setSortKey(sortKey);
                        contact.setPhotoId(photoId);
                        contact.setLookUpKey(lookUpKey);
                        mList.add(contact);

                        friendIdMap.put(friendId, contact);
                    }
                }
                if (mList.size() > 0) {
                    updateUI();
                }
            }
        }
    }

    private static void updateUI() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("name", mList.get(position).getDesplayName());
        bundle.putString("phoneNum", mList.get(position).getPhoneNum());
        // start up MessageActivity
        MessageActivity.actionStart(getActivity(), bundle);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}

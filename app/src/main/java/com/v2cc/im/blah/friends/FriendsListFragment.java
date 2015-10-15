package com.v2cc.im.blah.friends;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.v2cc.im.blah.message.MessageActivity;
import com.v2cc.im.blah.R;
import com.v2cc.im.blah.base.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class FriendsListFragment extends BaseFragment {
    private ListView mListView;
    private List<FriendsListBean> mList;
    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
    private Map<Integer, FriendsListBean> friendIdMap = null;
    private FriendsListAdapter mAdapter;

    public static FriendsListFragment newInstance(int position) {
        FriendsListFragment friendsListFragment = new FriendsListFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_POSITION, position);
        friendsListFragment.setArguments(bundle);
        return friendsListFragment;
    }

    @Override
    protected int setRootViewId() {
        return R.layout.frag_friends_list;
    }

    @Override
    protected void initViews(View rootView) {
        mListView = (ListView) rootView.findViewById(android.R.id.list);
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

    private class MyAsyncQueryHandler extends AsyncQueryHandler {

        public MyAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            Log.d("FriendsFrag", "QueryComplete");
            super.onQueryComplete(token, cookie, cursor);

            if (cursor != null && cursor.getCount() > 0) {
                friendIdMap = new HashMap<Integer, FriendsListBean>();
                mList = new ArrayList<FriendsListBean>();
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
                        FriendsListBean contact = new FriendsListBean();
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
                    setMyAdapter(mList);
                }
            }
        }
    }

    private void setMyAdapter(List<FriendsListBean> list) {
        mAdapter = new FriendsListAdapter(getActivity(), list);
        setListAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        Bundle bundle = new Bundle();
        bundle.putString("name", mList.get(position).getDesplayName());
        bundle.putString("phoneNum", mList.get(position).getPhoneNum());
        // start up MessageActivity
        MessageActivity.actionStart(getActivity(), bundle);
    }
}

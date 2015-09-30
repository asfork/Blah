package com.v2cc.im.blah;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class FriendsListFragment extends ListFragment {
    private FriendsListAdapter mAdapter;
    private ListView mListView;
    private List<FriendsListBean> mList;
    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象

    //    private static final String ARG_POSITION = "position";
    private Map<Integer, FriendsListBean> friendIdMap = null;

    public static FriendsListFragment newInstance(int position) {
        FriendsListFragment friendsListFragment = new FriendsListFragment();
        Bundle bundle = new Bundle();
//        bundle.putInt(ARG_POSITION, position);
        friendsListFragment.setArguments(bundle);
        return friendsListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("friendsFrag", "Hello in FriendsListFragment onCreate");

        View friendsView = inflater.inflate(R.layout.frag_friends_list, container, false);
        mListView = (ListView) friendsView.findViewById(android.R.id.list);

        // 实例化
        asyncQueryHandler = new MyAsyncQueryHandler(getActivity().getContentResolver());
        initData();

        return friendsView;
    }

    /**
     * 初始化数据库查询参数
     */
    private void initData() {
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
                    setAdapter(mList);
                }
            }
            super.onQueryComplete(token, cookie, cursor);
        }
    }

    private void setAdapter(List<FriendsListBean> list) {
        mAdapter = new FriendsListAdapter(getActivity(), list);
        mListView.setAdapter(mAdapter);

        //添加ListView每一行的点击监听事件，此处可设置监听处理操作
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),
//                        "position = " + position + "\n" + "第" + id
//                                + "行", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("name", mList.get(position).getDesplayName());
//                bundle.putString("passName", mList.get(position).getDesplayName());

                Intent intent = new Intent(getActivity(), MessageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}

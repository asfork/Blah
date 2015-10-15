package com.v2cc.im.blah.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.v2cc.im.blah.friends.FriendsListBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/15.
 * If this class works, I created it. If not, I didn't.
 */
public class ContactsHelperUtil {

    private static List<FriendsListBean> mList;
    private static Map<Integer, FriendsListBean> friendIdMap = null;

    // 根据电话号码取得联系人姓名
    public synchronized static String getContactNameByPhoneNumber(Context context, String address) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        // 将自己添加到 msPeers 中
        Cursor cursor = context.getContentResolver().query(
                uri,
                projection, // Which columns to return.
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '"
                        + address + "'", // WHERE clause.
                null, // WHERE clause value substitution
                null); // Sort order.

        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);

                // 取得联系人名字
                int nameFieldColumnIndex = cursor
                        .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                String name = cursor.getString(nameFieldColumnIndex);
                Log.d("ContactsHelperUtil", name + address);
                return name;
            }
        }
        Log.d("ContactsHelperUtil", "cursor null" + address + cursor.getCount());
        return null;
    }

    /**
     * 获取所有联系人内容
     */
    public synchronized static List getContacts(Context context) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = {ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY};

        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(uri, projection,
                null, null, "sort_key COLLATE LOCALIZED asc");

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
                return mList;
            }
        }
        return null;
    }
}
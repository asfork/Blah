package com.v2cc.im.blah.friends;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.v2cc.im.blah.base.app.App;
import com.v2cc.im.blah.base.utils.PhoneFormatUtil;
import com.v2cc.im.blah.db.DataBaseHelperUtil;

/**
 * Created by steve on 11/3/15.
 * If it works, I created this. If not, I didn't.
 */
public class ContactsAsyncQueryHandler extends AsyncQueryHandler {

    public static Uri CONTACTS_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
    public static String[] CONTACTS_INFO = {ContactsContract.CommonDataKinds.Phone._ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY};

    public ContactsAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        Log.d(getClass().getSimpleName(), "QueryComplete");
        super.onQueryComplete(token, cookie, cursor);

        DataBaseHelperUtil util = DataBaseHelperUtil.getInstance(App.getContext());
        if (cursor != null && cursor.getCount() > 0) {
            // 创建联系人对象
            FriendsBean contact = new FriendsBean();

            cursor.moveToFirst(); // 游标移动到第一项
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                String name = cursor.getString(1);
                String phone = PhoneFormatUtil.removeFormatting(cursor.getString(2));
                String sortKey = cursor.getString(3);

                contact.setName(name);
                contact.setPhone(phone);
                contact.setSortKey(sortKey);
                contact.setImgPath("");
                contact.setState(FriendsBean.FRI_STATE_OFFLINE);

                util.insertFriends(contact);
            }
        }
    }
}

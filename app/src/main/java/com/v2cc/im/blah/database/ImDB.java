package com.v2cc.im.blah.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.v2cc.im.blah.bean.ApplicationData;
import com.v2cc.im.blah.bean.ChatEntity;
import com.v2cc.im.blah.bean.MessageTabEntity;
import com.v2cc.im.blah.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve Zhang
 * 15-11-24
 * <p>
 * If it works, I created it. If not, I didn't.
 */
public class ImDB {
    private static final User user = (User) ApplicationData.getInstance().getUserInfo();
    public static final String DB_NAME = "blah" + user.getId();// 数据库名称
    //    public static final String DB_NAME = "blah_local";
    public static final int VERSION = 1;
    private static ImDB mInstance;
    private SQLiteDatabase database;

    private ImDB(Context context) {
        ImDBOpenHelper dbOpenHelper = new ImDBOpenHelper(context, DB_NAME, null,
                VERSION);
        database = dbOpenHelper.getWritableDatabase();
    }

    public synchronized static ImDB getInstance(Context context) {
        if (mInstance == null)
            mInstance = new ImDB(context);
        return mInstance;
    }

    public void saveFriend(User friend) {
        ContentValues values = new ContentValues();
        values.put("userid", user.getId());
        values.put("friendid", friend.getId());
        values.put("name", friend.getUserName());
        values.put("photo", friend.getPhoto());
        database.insert("friend", null, values);

    }

    public List<User> getAllFriend() {
        List<User> friends = new ArrayList<User>();
        int id = user.getId();
        Cursor cursor = database.rawQuery(
                "select * from friend where userid = " + id, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                User friend = new User();
                friend.setId(cursor.getInt(cursor.getColumnIndex("friendid")));
                friend.setUserName(cursor.getString(cursor
                        .getColumnIndex("name")));
                friend.setPhoto(cursor.getBlob(cursor.getColumnIndex("photo")));
                friends.add(friend);
            }
        }

        if (cursor != null)
            cursor.close();
        return friends;
    }

    public void saveMessage(MessageTabEntity message) {
        ContentValues values = new ContentValues();
        values.put("userid", user.getId());
        values.put("senderid", message.getSenderId());
        values.put("name", message.getName());
        values.put("content", message.getContent());
        values.put("sendtime", message.getSendTime());
        values.put("unread", message.getUnReadCount());
        values.put("type", message.getMessageType());
        database.insert("message", null, values);
    }

    public ArrayList<MessageTabEntity> getAllMessage() {
        ArrayList<MessageTabEntity> messages = new ArrayList<MessageTabEntity>();
        Cursor cursor = database.rawQuery("select * from message where userid = "
                + user.getId(), null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                MessageTabEntity message = new MessageTabEntity();
                message.setSenderId(cursor.getInt(cursor
                        .getColumnIndex("senderid")));
                message.setName(cursor.getString(cursor.getColumnIndex("name")));
                String time = cursor.getString(cursor
                        .getColumnIndex("sendtime"));
                message.setSendTime(time);
                message.setContent(cursor.getString(cursor
                        .getColumnIndex("content")));
                message.setMessageType(cursor.getInt(cursor
                        .getColumnIndex("type")));
                message.setUnReadCount(cursor.getInt(cursor
                        .getColumnIndex("unread")));
                messages.add(message);
            }
        }
        if (cursor != null)
            cursor.close();
        return messages;

    }

    public void deleteMessage(MessageTabEntity message) {
        String sql = "delete from message where userid = " + user.getId()
                + " and senderid =" + message.getSenderId() + " and type = "
                + message.getMessageType();
        database.execSQL(sql);
    }

    public void updateMessages(MessageTabEntity message) {
        String sql = "update message set unread = " + message.getUnReadCount()
                + ", content = \"" + message.getContent() + "\",sendtime = \""
                + message.getSendTime() + "\" where userid = " + user.getId()
                + " and senderid = " + message.getSenderId() + " and type = "
                + message.getMessageType();
        database.execSQL(sql);
    }

    public void saveChatMessage(ChatEntity message) {
        ContentValues values = new ContentValues();
        values.put("userid", user.getId());
        if (user.getId() == message.getSenderId()) {
            values.put("friendid", message.getReceiverId());
            values.put("type", ChatEntity.SEND);
        } else {
            values.put("friendid", message.getSenderId());
            values.put("type", ChatEntity.RECEIVE);
        }
        values.put("content", message.getContent());
        values.put("sendtime", message.getSendTime());
        database.insert("chat_message", null, values);
    }

    public List<ChatEntity> getChatMessage(int friendId) {
        Cursor cursor = database.rawQuery(
                "select * from chat_message where userid = " + user.getId()
                        + " and friendid = " + friendId, null);
        List<ChatEntity> allMessages = new ArrayList<ChatEntity>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ChatEntity chat = new ChatEntity();
                chat.setContent(cursor.getString(cursor
                        .getColumnIndex("content")));
                chat.setMessageType(cursor.getInt(cursor.getColumnIndex("type")));
                chat.setSendTime(cursor.getString(cursor
                        .getColumnIndex("sendtime")));
                allMessages.add(chat);
            }
        }

        if (cursor != null)
            cursor.close();

        return allMessages;
    }
}

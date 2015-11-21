package com.v2cc.im.blah.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.v2cc.im.blah.models.FriendsBean;
import com.v2cc.im.blah.models.MessageBean;

import java.util.ArrayList;


/**
 * 数据库工具类
 * <p/>
 * yeliang liang
 * DataBaseHelper
 * 2015-8-5 上午10:48:03
 * <p/>
 * Modify by Steve ZHANG
 * 2015-9-29
 * <p/>
 * Modify by Steve ZHANG
 * 2015-10-27
 *
 * @version V2.0
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_MESSAGE_LOGS = "MessageLogs";// 聊天记录主库
    public static final String TABLE_NAME_RECENT_CHATS = "RecentChats";// 最近聊天记录
    public static final String TABLE_NAME_FRIENDS = "Friends";
    //    private static final String DB_NAME = "Blah" + User.getInstance().getUserName() + ".db";// 数据库名称
    private static final String DB_NAME = "Phone.db";
    private static final int DB_VERSION = 1;// 数据库版本
    static Context mContext;
    private static DataBaseHelper mInstance;
    private int openCount = 0;// 数据库打开次数
    private SQLiteDatabase database;

    public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 单例
     */
    public synchronized static DataBaseHelper getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new DataBaseHelper(mContext, DB_NAME, null, DB_VERSION);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int formerVersion, int newVersion) {
        // pass
    }

    /**
     * 打开数据库，这种方式可以防止并发操作引起crash
     */
    public synchronized SQLiteDatabase openDataBase() {
        if (database == null) {
            database = mInstance.getWritableDatabase();
        }
        openCount++;
        return database;
    }

    /**
     * 关闭数据库，这种方式可以防止并发操作引起crash
     */
    public synchronized void closeDataBase() {
        if (openCount == 0) {
            database.close();
        } else {
            openCount--;
        }
    }

    /**
     * 创建表
     */
    public synchronized void createTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME_MESSAGE_LOGS);// 如果存在先干掉
        db.execSQL("drop table if exists " + TABLE_NAME_RECENT_CHATS);
        db.execSQL("drop table if exists " + TABLE_NAME_FRIENDS);

        db.execSQL("create table "
                + TABLE_NAME_MESSAGE_LOGS
                + " (_id integer primary key autoincrement,phone char,time char,"
                + "content char,type char,state char,source char)");

        db.execSQL("create table "
                + TABLE_NAME_RECENT_CHATS
                + " (_id integer primary key autoincrement,phone char,time char,"
                + "content char,type char,state char,source char)");

        db.execSQL("create table "
                + TABLE_NAME_FRIENDS
                + " (_id integer primary key autoincrement,phone char,name char,"
                + "sortKey char,imgPath char,state char)");

        // Todo insert testing data
        db.execSQL("insert into Friends (phone,name,sortKey,imgPath,state)" +
                " values (233, 'Peter','Peter','', 0)");
        db.execSQL("insert into Friends (phone,name,sortKey,imgPath,state)" +
                " values (10101, 'Hubot','Hubot','', 0)");
        db.execSQL("insert into Friends (phone,name,sortKey,imgPath,state)" +
                " values (9000, 'HAL','HAL','', 0)");
        db.execSQL("insert into Friends (phone,name,sortKey,imgPath,state)" +
                " values (999, 'Boss','Boss','', 0)");
        db.execSQL("insert into Friends (phone,name,sortKey,imgPath,state)" +
                " values (42, '42','42','', 0)");

        db.execSQL("insert into RecentChats (phone,time,content,type,source)" +
                " values (9000, 1444806933688, 'Blah blah', 1, 2)");
        db.execSQL("insert into RecentChats (phone,time,content,type,source)" +
                " values (10101, 1442806920125, 'Blah blah', 1, 1)");
        db.execSQL("insert into RecentChats (phone,time,content,type,source)" +
                " values (233, 1445116922125, 'Blah blah', 1, 2)");
        db.execSQL("insert into RecentChats (phone,time,content,type,source)" +
                " values (999, 1441816622125, 'Blah blah', 1, 1)");
        db.execSQL("insert into RecentChats (phone,time,content,type,source)" +
                " values (42, 1442818922125, 'Blah blah', 1, 2)");

        db.execSQL("insert into MessageLogs (phone,time,content,type,source)" +
                " values (9000, 1444805933688, 'Blah blah', 1, 1)");
        db.execSQL("insert into MessageLogs (phone,time,content,type,source)" +
                " values (9000, 1446806533688, 'Blah blah', 1, 2)");
        db.execSQL("insert into MessageLogs (phone,time,content,type,source)" +
                " values (9000, 1447206833688, 'Blah blah', 1, 1)");
        db.execSQL("insert into MessageLogs (phone,time,content,type,source)" +
                " values (9000, 1448106933688, 'Blah blah', 1, 2)");
        db.execSQL("insert into MessageLogs (phone,time,content,type,source)" +
                " values (10101, 1444806720125, 'Blah blah', 1, 2)");
        db.execSQL("insert into MessageLogs (phone,time,content,type,source)" +
                " values (233, 1444803922125, 'Blah blah', 1, 2)");
        db.execSQL("insert into MessageLogs (phone,time,content,type,source)" +
                " values (10101, 1446806920125, 'Blah blah', 1, 1)");
        db.execSQL("insert into MessageLogs (phone,time,content,type,source)" +
                " values (233, 1445116922125, 'Blah blah', 1, 2)");
        db.execSQL("insert into MessageLogs (phone,time,content,type,source)" +
                " values (999, 1441816622125, 'Blah blah', 1, 1)");
        db.execSQL("insert into MessageLogs (phone,time,content,type,source)" +
                " values (42, 1442818922125, 'Blah blah', 1, 2)");
    }

    /**
     * 插入一条数据
     */
    public synchronized void insertToTable(String tableName, MessageBean messageBean) {
        database.execSQL(
                "insert into "
                        + tableName
                        + " (phone,time,content,type,state,source) values(?,?,?,?,?,?)",
                new Object[]{messageBean.getPhone(), messageBean.getTime(), messageBean.getContent(),
                        messageBean.getType(), messageBean.getState(), messageBean.getSource()});
    }

    /**
     * 最近联系：插入最近的一条记录
     */
    public synchronized void insertRecentChats(MessageBean messageBean) {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME_RECENT_CHATS
                + " where phone = ? ", new String[]{messageBean.getPhone()});
        if (cursor.moveToNext()) {
            // 存在数据 修改时间和内容、来源即可
            database.execSQL(
                    "update " + TABLE_NAME_RECENT_CHATS
                            + " set time= ? , content = ? , type = ? , source = ? where phone = '"
                            + messageBean.getPhone() + "'", new Object[]{messageBean.getTime(),
                            messageBean.getContent(), messageBean.getType(), messageBean.getSource()});
        } else {
            // 不存在，新创建
            database.execSQL(
                    "insert into "
                            + TABLE_NAME_RECENT_CHATS
                            + " (phone,time,content,type,state,source) values(?,?,?,?,?,?,?,?)",
                    new Object[]{messageBean.getPhone(), messageBean.getTime(),
                            messageBean.getContent(), messageBean.getType(),
                            messageBean.getState(), messageBean.getSource()});
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * 朋友列表：插入通讯录里的联系人数据
     */
    public synchronized void insertFriends(FriendsBean friendsBean) {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME_FRIENDS
                + " where phone = ? ", new String[]{friendsBean.getPhone()});
        if (cursor.moveToNext()) {
            // TODO update table
        } else {
            database.execSQL(
                    "insert into "
                            + TABLE_NAME_FRIENDS
                            + " (phone,name,sortKey,imgPath,state) values(?,?,?,?,?)",
                    new Object[]{friendsBean.getPhone(), friendsBean.getName(),
                            friendsBean.getSortKey(), friendsBean.getImgPath(),
                            friendsBean.getState()});
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * TODO 根据电话号码查询联系人姓名
     */
    public synchronized String getNamebyPhone(String phone) {

        return null;
    }

    /**
     * 最近联系： 获取最近的一条记录
     */
    public synchronized ArrayList<MessageBean> getRecentChats() {
        ArrayList<MessageBean> list = new ArrayList<MessageBean>();
        Cursor cursor = database.rawQuery("select " + TABLE_NAME_FRIENDS + ".name,"
                + TABLE_NAME_RECENT_CHATS + ".* from " + TABLE_NAME_RECENT_CHATS
                + " inner join " + TABLE_NAME_FRIENDS
                + " on " + TABLE_NAME_RECENT_CHATS + ".phone =" + TABLE_NAME_FRIENDS
                + ".phone order by time desc", null);
        while (cursor != null && cursor.moveToNext()) {
            list.add(new MessageBean(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }

    /**
     * 获取当前账户人的聊天记录
     */
    public synchronized ArrayList<MessageBean> getMessageLogs(String phone) {
        ArrayList<MessageBean> mList = new ArrayList<MessageBean>();
        Cursor cursor = database.rawQuery("select " + TABLE_NAME_FRIENDS + ".name,"
                + TABLE_NAME_MESSAGE_LOGS + ".* from " + TABLE_NAME_MESSAGE_LOGS
                + " inner join " + TABLE_NAME_FRIENDS
                + " on " + TABLE_NAME_MESSAGE_LOGS + ".phone =" + TABLE_NAME_FRIENDS
                + ".phone where " + TABLE_NAME_MESSAGE_LOGS + ".phone = ?", new String[]{phone});
        while (cursor != null && cursor.moveToNext()) {
            mList.add(new MessageBean(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        return mList;
    }

    /**
     * 获取当前好友列表
     */
    public synchronized ArrayList<FriendsBean> getFriendsList() {
        ArrayList<FriendsBean> mList = new ArrayList<FriendsBean>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME_FRIENDS, null);
        while (cursor != null && cursor.moveToNext()) {
            FriendsBean friendsBean = new FriendsBean();
            friendsBean.setPhone(cursor.getString(1));
            friendsBean.setName(cursor.getString(2));
            friendsBean.setSortKey(cursor.getString(3));
            friendsBean.setImgPath(cursor.getString(4));
            friendsBean.setState(cursor.getString(5));
            mList.add(friendsBean);
        }
        if (cursor != null) {
            cursor.close();
        }
        return mList;
    }

    /**
     * 根据用户名删除聊天记录
     */
    public synchronized void deleteMessageLogsByPhone(String phone) {
        database.execSQL("delete from " + TABLE_NAME_RECENT_CHATS + " where phone = '" + phone + "'");
        database.execSQL("delete from " + TABLE_NAME_MESSAGE_LOGS + " where phone = '" + phone + "'");
    }

}

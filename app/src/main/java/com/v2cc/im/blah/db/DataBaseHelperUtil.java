package com.v2cc.im.blah.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.v2cc.im.blah.message.MessageBean;

import java.util.ArrayList;


/**
 * 数据库工具类
 *
 * @author yeliangliang
 * @ClassName: DataBaseHelperUtil
 * @date 2015-8-5 上午10:48:03
 *
 * Modify by Steve ZHANG
 * 2015-9-29
 */

public class DataBaseHelperUtil extends SQLiteOpenHelper {

    static Context mContext;
    private static DataBaseHelperUtil mInstance;
    //    private static final String DB_NAME = "MessageHistory" + User.getInstance().getUserName() + ".db";// 数据库名称
    private static final String DB_NAME = "MessageHistoryTester.db";
    private static final int DB_VERSION = 1;// 数据库版本
    public static final String TABLE_NAME_MESSAGE = "Message";// 聊天记录主库
    public static final String TABLE_NAME_RECENT_CHAT = "RecentChat";// 最近聊天记录
    private int openCount = 0;// 数据库打开次数
    private SQLiteDatabase database;

    /**
     * 单例
     *
     * @param context
     * @return DataBaseHelperUtil
     * @author yeliangliang
     * @date 2015-8-5 上午10:53:07
     * @version V1.0
     */
    public synchronized static DataBaseHelperUtil getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new DataBaseHelperUtil(mContext, DB_NAME, null, DB_VERSION);
        }
        return mInstance;
    }

    public DataBaseHelperUtil(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // pass
    }

    /**
     * 打开数据库，这种方式可以防止并发操作引起crash
     *
     * @return SQLiteDatabase
     * @author yeliangliang
     * @date 2015-8-5 上午11:01:42
     * @version V1.0
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
     * <p/>
     * 2015-8-5 上午11:02:53
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
     * <p/>
     * 2015-8-5 上午11:07:28
     */
    public synchronized void createTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME_MESSAGE);// 如果存在先干掉
        db.execSQL("drop table if exists " + TABLE_NAME_RECENT_CHAT);
        db.execSQL("create table "
                + TABLE_NAME_MESSAGE
                + " (_id integer primary key autoincrement,name char,phoneNum char,time char,content char,imgPath char,status char,source char)");

        db.execSQL("create table "
                + TABLE_NAME_RECENT_CHAT
                + " (_id integer primary key autoincrement,name char,phoneNum char,time char,content char,imgPath char,status char,source char)");

        // Todo insert testing data
        db.execSQL("insert into RecentChat (name,phoneNum,time,content,source)" +
                " values ('HAL9000', 9000, 1444806933688, 'Blah blah', 0)");
        db.execSQL("insert into RecentChat (name,phoneNum,time,content,source)" +
                " values ('Hubot', 10101, 1442806920125, 'Blah blah', 0)");
        db.execSQL("insert into RecentChat (name,phoneNum,time,content,source)" +
                " values ('Peter', 233, 1444816922125, 'Blah blah', 0)");

        db.execSQL("insert into Message (name,phoneNum,time,content,source)" +
                " values ('HAL9000', 9000, 1444805933688, 'Blah blah', 0)");
        db.execSQL("insert into Message (name,phoneNum,time,content,source)" +
                " values ('HAL9000', 9000, 1444806533688, 'Blah blah', 1)");
        db.execSQL("insert into Message (name,phoneNum,time,content,source)" +
                " values ('HAL9000', 9000, 1444806833688, 'Blah blah', 1)");
        db.execSQL("insert into Message (name,phoneNum,time,content,source)" +
                " values ('HAL9000', 9000, 1444806933688, 'Blah blah', 0)");
        db.execSQL("insert into Message (name,phoneNum,time,content,source)" +
                " values ('Hubot', 10101, 1444806720125, 'Blah blah', 0)");
        db.execSQL("insert into Message (name,phoneNum,time,content,source)" +
                " values ('Peter', 233, 1444803922125, 'Blah blah', 1)");
        db.execSQL("insert into Message (name,phoneNum,time,content,source)" +
                " values ('Hubot', 10101, 1442806920125, 'Blah blah', 0)");
        db.execSQL("insert into Message (name,phoneNum,time,content,source)" +
                " values ('Peter', 233, 1444816922125, 'Blah blah', 0)");
    }

    /**
     * 插入一条数据
     * <p/>
     * 2015-8-5 上午11:19:13
     */
    public synchronized void insertToTable(String tableName, MessageBean messageBean) {
        database.execSQL(
                "insert into "
                        + tableName
                        + " (_id,name,phoneNum,time,content,imgPath,status,source) values(?,?,?,?,?,?,?,?)",
                new Object[]{messageBean.getId(), messageBean.getName(), messageBean.getPhoneNum(),
                        messageBean.getTime(), messageBean.getContent(), messageBean.getImgPath(),
                        messageBean.getStatus(), messageBean.getSource()});
    }

    /**
     * 最近联系：插入最近的一条记录
     *
     * @return void
     * @author yeliangliang
     * @date 2015-8-5 上午11:31:34
     * @version V1.0
     */
    public synchronized void insertRecentChat(MessageBean messageBean) {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME_RECENT_CHAT
                + " where phoneNum = ? ", new String[]{messageBean.getPhoneNum()});
        if (cursor.moveToNext()) {
            // 存在数据 修改时间和内容、来源即可
            database.execSQL(
                    "update " + TABLE_NAME_RECENT_CHAT
                            + " set time= ? , content = ? , source = ? where phoneNum = '"
                            + messageBean.getPhoneNum() + "'", new Object[]{messageBean.getTime(),
                            messageBean.getContent(), messageBean.getSource()});
        } else {
            // 不存在，新创建
            database.execSQL(
                    "insert into "
                            + TABLE_NAME_RECENT_CHAT
                            + " (_id,name,phoneNum,time,content,imgPath,status,source) values(?,?,?,?,?,?,?,?)",
                    new Object[]{messageBean.getId(), messageBean.getName(),
                            messageBean.getPhoneNum(), messageBean.getTime(),
                            messageBean.getContent(), messageBean.getImgPath(),
                            messageBean.getStatus(), messageBean.getSource()});
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * 最近联系： 获取最近的一条记录
     *
     * @return void
     * @author yeliangliang
     * @date 2015-8-5 上午11:31:34
     * @version V1.0
     */
    public synchronized ArrayList<MessageBean> getRecentChat() {
        ArrayList<MessageBean> cList = new ArrayList<MessageBean>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME_RECENT_CHAT
                + " order by time desc", null);
        while (cursor != null && cursor.moveToNext()) {
            cList.add(new MessageBean(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        return cList;
    }

    /**
     * 获取当前账户人的聊天记录
     *
     * @param pNum
     * @return ArrayList<ChatRecord>
     * @author yeliangliang
     * @date 2015-8-6 下午6:19:15
     * @version V1.0
     */
    public synchronized ArrayList<MessageBean> getMessageHistory(String pNum) {
        ArrayList<MessageBean> mList = new ArrayList<MessageBean>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME_MESSAGE
                + " where phoneNum = ?", new String[]{pNum});
        while (cursor != null && cursor.moveToNext()) {
            mList.add(new MessageBean(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        return mList;

    }

    /**
     * 根据用户名删除聊天记录
     * <p/>
     * 2015-8-21 下午6:05:11
     */
    public synchronized void deleteMessageHistoryByName(String name) {
        database.execSQL("delete from " + TABLE_NAME_RECENT_CHAT + " where name = '" + name + "'");
        database.execSQL("delete from " + TABLE_NAME_MESSAGE + " where name = '" + name + "'");
    }

}

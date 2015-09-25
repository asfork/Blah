package com.v2cc.im.blah;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * 数据库工具类
 *
 * @author yeliangliang
 * @ClassName: DataBaseHelperUtil
 * @date 2015-8-5 上午10:48:03
 */

public class DataBaseHelperUtil extends SQLiteOpenHelper {

    private static Context mContext;
    private static DataBaseHelperUtil mInstance;
    //    private static final String DB_NAME = "MessageRecord" + User.getInstance().getUserName() + ".db";// 数据库名称
    private static final String DB_NAME = "MessageRecordTester.db";
    private static final int DB_VERSION = 1;// 数据库版本
    public static final String TABLE_NAME_MESSAGE_RECORD = "MessageRecord";// 聊天记录主库
    public static final String TABLE_NAME_MESSAGE_NEW_RECORD = "NewMessageRecord";// 最近聊天记录
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
        creatTable(db);
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
    public synchronized void creatTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME_MESSAGE_RECORD);// 如果存在先干掉
        db.execSQL("drop table if exists " + TABLE_NAME_MESSAGE_NEW_RECORD);
        db.execSQL("create table "
                + TABLE_NAME_MESSAGE_RECORD
                + " (_id integer primary key autoincrement,name char,passName char,time char,content char,imgPath char,status char,source char)");

        db.execSQL("create table "
                + TABLE_NAME_MESSAGE_NEW_RECORD
                + " (_id integer primary key autoincrement,name char,passName char,time char,content char,imgPath char,status char,source char)");

        // Todo insert testing data
        db.execSQL("insert into NewMessageRecord (name,time,content,source)" +
                " values ('Char', 1242047966, 'Blah blah', 0)");
        db.execSQL("insert into NewMessageRecord (name,time,content,source)" +
                " values ('Coco', 1843047966, 'Blah blah', 0)");
        db.execSQL("insert into NewMessageRecord (name,time,content,source)" +
                " values ('Tom', 1942047966, 'Blah blah', 0)");
    }

    /**
     * 插入一条数据
     * <p/>
     * 2015-8-5 上午11:19:13
     */
    public synchronized void insertToTable(String tableName, ChatListBean chatRecord) {
        database.execSQL(
                "insert into "
                        + tableName
                        + " (_id,name,passName,time,content,imgPath,status,source) values(?,?,?,?,?,?,?,?)",
                new Object[]{chatRecord.getId(), chatRecord.getName(), chatRecord.getPassName(),
                        chatRecord.getTime(), chatRecord.getContent(), chatRecord.getImgPath(),
                        chatRecord.getStatus(), chatRecord.getSource()});
    }

    /**
     * 最近联系：插入最近的一条记录
     *
     * @return void
     * @author yeliangliang
     * @date 2015-8-5 上午11:31:34
     * @version V1.0
     */
    public synchronized void insertNewRecord(ChatListBean chatRecord) {
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME_MESSAGE_NEW_RECORD
                + " where name = ? ", new String[]{chatRecord.getName()});
        if (cursor.moveToNext()) {
            // 存在数据 修改时间和内容、来源即可
            database.execSQL(
                    "update " + TABLE_NAME_MESSAGE_NEW_RECORD
                            + " set time= ? , content = ? , source = ? where name = '"
                            + chatRecord.getName() + "'", new Object[]{chatRecord.getTime(),
                            chatRecord.getContent(), chatRecord.getSource()});
        } else {
            // 不存在，新创建
            database.execSQL(
                    "insert into "
                            + TABLE_NAME_MESSAGE_NEW_RECORD
                            + " (_id,name,passName,time,content,imgPath,status,source) values(?,?,?,?,?,?,?,?)",
                    new Object[]{chatRecord.getId(), chatRecord.getName(),
                            chatRecord.getPassName(), chatRecord.getTime(),
                            chatRecord.getContent(), chatRecord.getImgPath(),
                            chatRecord.getStatus(), chatRecord.getSource()});
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
    public synchronized ArrayList<ChatListBean> searchNewRecord() {
        ArrayList<ChatListBean> cList = new ArrayList<ChatListBean>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME_MESSAGE_NEW_RECORD
                + " order by time desc", null);
        while (cursor != null && cursor.moveToNext()) {
            cList.add(new ChatListBean(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        return cList;
    }

    /**
     * 获取当前账户人的聊天记录
     *
     * @param passName
     * @return ArrayList<ChatRecord>
     * @author yeliangliang
     * @date 2015-8-6 下午6:19:15
     * @version V1.0
     */
    public synchronized ArrayList<ChatListBean> searchNowChatRecord(String passName) {
        ArrayList<ChatListBean> mList = new ArrayList<ChatListBean>();
        Cursor cursor = database.rawQuery("select * from " + TABLE_NAME_MESSAGE_RECORD
                + " where passName = ?", new String[]{passName});
        while (cursor != null && cursor.moveToNext()) {
            mList.add(new ChatListBean(cursor));
        }
        if (cursor != null) {
            cursor.close();
        }
        return mList;

    }

    /**
     * 更具用户名删除聊天记录
     * <p/>
     * 2015-8-21 下午6:05:11
     */
    public synchronized void deleteRecordByName(String name) {
        database.execSQL("delete from " + TABLE_NAME_MESSAGE_NEW_RECORD + " where name = '" + name + "'");
        database.execSQL("delete from " + TABLE_NAME_MESSAGE_RECORD + " where name = '" + name + "'");
    }

}

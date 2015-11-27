package com.v2cc.im.blah.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ImDBOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_FRIEND = "create table if not exists friend("
            + "userid integer ,"
            + "friendid integer,"
            + "name text,"
            + "gender integer," + "photo blob)";
    public static final String CREATE_MESSAGE = "create table if not exists message("
            + "userid integer,"
            + "senderid integer,"
            + "name text,"
            + "sendtime text,"
            + "content text,"
            + "unread integer,"
            + "type integer)";
    public static final String CREATE_CHAT_MESSAGE = "create table if not exists chat_message("
            + "userid integer,"
            + "friendid integer,"
            + "type integer,"
            + "sendtime text," + "content text)";

    public ImDBOpenHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FRIEND);
        db.execSQL(CREATE_MESSAGE);
        db.execSQL(CREATE_CHAT_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }
}
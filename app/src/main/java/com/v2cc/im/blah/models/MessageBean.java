package com.v2cc.im.blah.models;

import android.database.Cursor;

/**
 * 聊天记录bean
 *
 * @author yeliangliang
 *         MessageBean
 *         2015-8-5 上午10:33:52
 */
public class MessageBean {
    public final static String MSG_TYPE_TEXT = "1";
    public final static String MSG_TYPE_PHOTO = "2";

    public final static String MSG_SOURCE_SEND = "1";
    public final static String MSG_SOURCE_RECEIVE = "2";

    public final static String MSG_STATE_SENDING = "1";
    public final static String MSG_STATE_SUCCESS = "2";
    public final static String MSG_STATE_FAIL = "3";

    public final static String MSG_CONTENT = "blah blah";

    private String id;// id
    private String name;// 姓名
    private String phone;// 账户
    private String time;// 时间
    private String content;// 内容
    private String type;// 0-text | 1-photo | more type ...
    private String source;// 0-send | 1-receive
    private String state;// 0-sending | 1-success | 2-fail

    public MessageBean() {
    }

    public MessageBean(String name, String id, String phone, String time, String content,
                       String type, String source, String state) {
        super();
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.time = time;
        this.content = content;
        this.type = type;
        this.source = source;
        this.state = state;
    }

    public MessageBean(Cursor cursor) {
        this.id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
        this.name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        this.phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
        this.time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
        this.content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
        this.type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
        this.source = cursor.getString(cursor.getColumnIndexOrThrow("source"));
        this.state = cursor.getString(cursor.getColumnIndexOrThrow("state"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

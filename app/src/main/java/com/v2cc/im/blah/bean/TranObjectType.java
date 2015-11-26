package com.v2cc.im.blah.bean;

/**
 * Created by steve on 15-11-23.
 * 传输对象类型
 */
public enum TranObjectType {
    REGISTER, // 注册
    REGISTER_ACCOUNT,//注册的账号验证
    LOGIN, // 用户登录
    MESSAGE, // 用户发送消息
    SEARCH_FRIEND,//找朋友
    FRIEND_REQUEST //好友申请
}

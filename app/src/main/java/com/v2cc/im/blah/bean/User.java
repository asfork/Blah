package com.v2cc.im.blah.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 类名：User 说明：用户对象
 */
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String account;
    private String userName;
    private int gender; // 0代表女生 1代表男生
    private boolean isOnline;
    private String location;
    private byte[] photo;
    private String userBriefIntro;

    public String getUserBriefIntro() {
        return userBriefIntro;
    }

    public void setUserBriefIntro(String userBriefIntro) {
        this.userBriefIntro = userBriefIntro;
    }

    private ArrayList<User> friendList;

    public ArrayList<User> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<User> friendList) {
        this.friendList = friendList;
    }

    public User(String account, String username, int gender, byte[] photo) {
        this.account = account;
        this.userName = username;
        this.gender = gender;
        this.photo = photo;
    }

    public User() {

    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        User user = (User) o;
        return this.id == user.getId();
    }
}
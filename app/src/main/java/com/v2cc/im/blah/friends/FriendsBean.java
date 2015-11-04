package com.v2cc.im.blah.friends;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 2015/9/23.
 * If it works, I created this. If not, I didn't.
 */
public class FriendsBean {
    public final static String FRI_STATE_OFFLINE = "1";
    public final static String FRI_STATE_ONLINE = "2";

    private int contactId; //id
    private String name;//姓名
    private String sortKey;
    private String phone; // 电话号码
    private String imgPath; // 图片id
    private String state; // 1-offline | 2-online

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

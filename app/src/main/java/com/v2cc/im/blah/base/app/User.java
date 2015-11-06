package com.v2cc.im.blah.base.app;

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.base.bean.UserInfo;

import java.util.Random;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/17.
 * If this class works, I created it. If not, I didn't.
 */
public class User {

    private static final Random RANDOM = new Random();

    public static int getRandomPhotosDrawable() {
        switch (RANDOM.nextInt(2)) {
            default:
            case 0:
                return R.drawable.github_girl;
            case 1:
                return R.drawable.hubot;
        }
    }

    // 用户信息
    private UserInfo userInfo = null;
    // 单例
    private static User user = null;

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }
        return user;
    }

    public UserInfo setUserInfo(UserInfo u) {
        this.userInfo = u;
        return this.userInfo;
    }

    public UserInfo getUserInfo() {
        if (userInfo == null) {
            return null;
        }
        return this.userInfo;
    }
}

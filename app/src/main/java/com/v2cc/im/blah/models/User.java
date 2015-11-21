package com.v2cc.im.blah.models;

import com.v2cc.im.blah.R;

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
}

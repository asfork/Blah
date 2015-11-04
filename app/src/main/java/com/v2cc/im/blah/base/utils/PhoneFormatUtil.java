package com.v2cc.im.blah.base.utils;

/**
 * Created by steve on 11/3/15.
 */
public class PhoneFormatUtil {
    public static String removeFormatting(String phone) {
        if (phone.length() >= 14) {
            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
            phone = phone.replace("+86", "");
        } else {
            phone = phone.replace(" ", "");
            phone = phone.replace("-", "");
        }

        return phone;
    }
}

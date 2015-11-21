package com.v2cc.im.blah.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 计算最近记录中应该显示的时间
 * <p/>
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/14.
 * If this class works, I created it. If not, I didn't.
 */
public class TimeFormatUtil {
    public static String displayTime(String time) {
        long ti = Long.valueOf(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String t1 = dateFormat.format(ti);
        String t2 = dateFormat.format(System.currentTimeMillis());
        if (t1.equals(t2)) {
            // 时间为今天
            dateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
            return dateFormat.format(ti);
        }
        dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
        String t3 = dateFormat.format(ti);
        String t4 = dateFormat.format(System.currentTimeMillis());
        if (t3.equals(t4)) {
            // 今年
            dateFormat = new SimpleDateFormat("MM-dd", Locale.CHINA);
            return dateFormat.format(ti);
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            return dateFormat.format(ti);
        }
    }
}

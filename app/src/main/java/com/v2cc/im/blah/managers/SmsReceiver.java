package com.v2cc.im.blah.managers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.v2cc.im.blah.R;
import com.v2cc.im.blah.bean.MessageTabEntity;
import com.v2cc.im.blah.utils.PhoneFormatUtil;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/15.
 * If this class works, I created it. If not, I didn't.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_EXTRA_NAME = "pdus";

    public void onReceive(Context context, Intent intent) {
        // Get SMS map from Intent
        Bundle extras = intent.getExtras();

        if (extras != null) {
            // Get received SMS array
            Object[] smsExtra = (Object[]) extras.get(SMS_EXTRA_NAME);

            // Merge long SMS
            SmsMessage[] mMessage = new SmsMessage[smsExtra.length];
            for (int i = 0; i < mMessage.length; i++) {
                mMessage[i] = SmsMessage.createFromPdu((byte[]) smsExtra[i]);
            }
            String address = mMessage[0].getOriginatingAddress();
            String fullSms = "";
            for (SmsMessage message : mMessage) {
                fullSms += message.getMessageBody();
            }

//            DataBaseHelper.getInstance(context).openDataBase();
//            util = DataBaseHelper.getInstance(context);

            // formatting address
            String phone = PhoneFormatUtil.removeFormatting(address);
//            String name = util.getNamebyPhone(phone);
            String name = "Seve";

            if (fullSms.equals("blah blah") && name != null) {
                showNotification(context, name, address);
                saveSMStoDB(context, address, fullSms, "1");

                // If you uncomment next line then received SMS will not be put to incoming
                //  4.4 版本后，只有默认短信应用才能操作短息数据库和拦截广播
//                smsUtil = new SMSUtil();
//                smsUtil.deleteSMS(context, fullSms);
//                abortBroadcast();
            } else if (fullSms.equals("blah blah") && name == null) {
                showNotification(context, "unknown", address);
            }

//            DataBaseHelper.getInstance(context).closeDataBase();
        }
    }

    private void showNotification(Context context, String name, String phone) {
        Log.d("SmsReceiver", "showNotification");

        //设置点击通知栏的动作为启动另外一个广播
        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        broadcastIntent.putExtra("phone", phone);
        broadcastIntent.putExtra("name", name);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(name)
                .setTicker("New message")// 设置在 status bar 上显示的提示文字
                .setContentText("blah blah")// TextView 中显示的详细内容
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_discuss)
//                .setDefaults(Notification.DEFAULT_SOUND) //向通知添加声音、闪灯和振动效果的最简单、
                        // 最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        // Notification.DEFAULT_ALL  Notification.DEFAULT_VIBRATE 添加震动
                .setAutoCancel(true); //设置这个标志当用户单击面板就可以让通知将自动取消

        Log.d("repeat", "showNotification");
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());
    }

    private void saveSMStoDB(Context context, String phone, String content, String source) {
        MessageTabEntity mb = new MessageTabEntity();
        mb.setContent(content);
//        mb.s(phone);
//        mb.setSource(source);
//        mb.setTime(System.currentTimeMillis() + "");
//        mb.setState("0");

//        util.insertMessage(mb);
//        util.insertRecentChats(mb);
    }
}

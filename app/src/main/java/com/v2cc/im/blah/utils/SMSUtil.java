package com.v2cc.im.blah.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/8.
 * If it works, I created this. If not, I didn't.
 */
public class SMSUtil {

    public void sendSMS(String phoneNum) {

//        String SENT = "sms_sent";
//        String DELIVERED = "sms_delivered";

//        PendingIntent sentPI = PendingIntent.getActivity(this, 0, new Intent(SENT), 0);
//        PendingIntent deliveredPI = PendingIntent.getActivity(this, 0, new Intent(DELIVERED), 0);

//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Log.i("SMS", "Activity.RESULT_OK");
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Log.i("SMS", "RESULT_ERROR_GENERIC_FAILURE");
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        Log.i("SMS", "RESULT_ERROR_NO_SERVICE");
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        Log.i("SMS", "RESULT_ERROR_NULL_PDU");
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        Log.i("SMS", "RESULT_ERROR_RADIO_OFF");
//                        break;
//                }
//            }
//        }, new IntentFilter(SENT));

//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                switch (getResultCode()) {
//                    case Activity.RESULT_OK:
//                        Log.i("====>", "RESULT_OK");
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Log.i("=====>", "RESULT_CANCELED");
//                        break;
//                }
//            }
//        }, new IntentFilter(DELIVERED));

        //直接调用短信接口发短信
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNum, null, "blah blah", null, null);
    }

    // 4.4 版本后，只有默认短信应用才能操作短息数据库
    public void deleteSMS(Context context, String smscontent) {
        try {
            // 准备系统短信收信箱的uri地址
            Uri uri = Uri.parse("content://sms/inbox");// 收信箱
            // 查询收信箱里所有的短信
            Cursor isRead =
                    context.getContentResolver().query(uri, null, "read=" + "0",
                            null, null);
            while (isRead.moveToNext()) {
                // String phone =
                // isRead.getString(isRead.getColumnIndex("address")).trim();//获取发信人
                String body =
                        isRead.getString(isRead.getColumnIndex("body")).trim();// 获取信息内容
                if (body.equals(smscontent)) {
                    int id = isRead.getInt(isRead.getColumnIndex("_id"));

                    context.getContentResolver().delete(
                            Uri.parse("content://sms"), "_id=" + id, null);
                    Log.d("DeleteSms", "_id=" + id);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

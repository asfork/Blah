package com.v2cc.im.blah.base.utils;

import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/8.
 * If it works, I created this. If not, I didn't.
 */
public class SMSUtil {

    public static void sendSMS(String phoneNum) {

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
}

package com.v2cc.im.blah.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;

import com.v2cc.im.blah.db.ContactsHelperUtil;
import com.v2cc.im.blah.db.DataBaseHelperUtil;
import com.v2cc.im.blah.message.MessageBean;

/**
 * Created by Steve ZHANG (stevzhg@gmail.com)
 * 15/10/15.
 * If this class works, I created it. If not, I didn't.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static final String SMS_EXTRA_NAME = "pdus";
    private DataBaseHelperUtil util;

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

            // TODO number formatting
            PhoneNumberUtils phoneNumberUtils = new PhoneNumberUtils();
            String phoneNum = phoneNumberUtils.formatNumber(address);

            String name = ContactsHelperUtil.getContactNameByPhoneNumber(context, phoneNum);

            if (fullSms.equals("blah blah") && name != null) {
                // TODO notification
                messageNotification(name, phoneNum);

                saveSMStoDB(context, name, phoneNum, fullSms, "1");

                // If you uncomment next line then received SMS will not be put to incoming
                //  4.4 版本后，只有默认短信应用才能操作短息数据库和拦截广播
//                smsUtil = new SMSUtil();
//                smsUtil.deleteSMS(context, fullSms);
//                abortBroadcast();
            } else if (fullSms.equals("blah blah") && name == null) {
                messageNotification("unknown", phoneNum);
            }
        }
    }

    private void messageNotification(String name, String phoneNum) {
        Log.d("SmsReceiver", name + phoneNum);
    }

    private void saveSMStoDB(final Context context, final String name, final String phoneNum, final String content, final String source) {

        MessageBean mb = new MessageBean();
        mb.setContent(content);
        mb.setName(name);
        mb.setPhoneNum(phoneNum);
        mb.setSource(source);
        mb.setTime(System.currentTimeMillis() + "");
        mb.setStatus("0");

        DataBaseHelperUtil.getInstance(context).openDataBase();
        util = DataBaseHelperUtil.getInstance(context);
        util.insertToTable(DataBaseHelperUtil.TABLE_NAME_MESSAGE, mb);
        util.insertRecentChat(mb);
        DataBaseHelperUtil.getInstance(context).closeDataBase();
    }
}

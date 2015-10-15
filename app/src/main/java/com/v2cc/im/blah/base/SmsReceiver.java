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
            String fullMessage = "";
            for (SmsMessage message : mMessage) {
                fullMessage += message.getMessageBody();
            }

            PhoneNumberUtils phoneNumberUtils = new PhoneNumberUtils();
            String phoneNum = phoneNumberUtils.formatNumber(address);

            String name = ContactsHelperUtil.getContactNameByPhoneNumber(context, phoneNum);

            if (fullMessage.equals("blah blah") && name != null) {
                // Todo notification
                messageNotification(name, phoneNum);

                // TODO send sms to db

                // If you uncomment next line then received SMS will not be put to incoming
                abortBroadcast();
            } else if (fullMessage.equals("blah blah") && name == null) {
                // TODO How to respond sms from unknown people
                messageNotification("unknown", phoneNum);
            }
        }
    }

    private void messageNotification(String name, String phoneNum) {
        Log.d("SmsReceiver", name + phoneNum);
    }
}

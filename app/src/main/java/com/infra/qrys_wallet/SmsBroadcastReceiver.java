package com.infra.qrys_wallet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_BUNDLE = "pdus";

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ON RECEIVE BROADCAST", Toast.LENGTH_LONG).show();
        Log.d("Test 0","SmsBroadcastReceiver");
        if (intent.getAction().equals(ACTION)) {
            Bundle intentExtras = intent.getExtras();
            if (intentExtras != null) {
                Log.d("Test 1", "Not null SmsBroadcastReceiver");
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                String smsMessageStr = "";
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                    String smsBody = smsMessage.getMessageBody().toString();
                    String address = smsMessage.getOriginatingAddress();
                    smsMessageStr += "SMS From: " + address + "\n";
                    smsMessageStr += smsBody + "\n";
                    Log.d("SMS", " " + smsMessageStr);
                }
                Toast.makeText(context, smsMessageStr, Toast.LENGTH_SHORT).show();
                //this will update the UI with message
                CustomerAlreadyReg inst = CustomerAlreadyReg.instance();
                inst.updateData(smsMessageStr);
            }
        }
    }
}
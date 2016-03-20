package com.infra.qrys_wallet.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.infra.qrys_wallet.CustomerAlreadyReg;

/**
 * Created by vishvendu.palawat on 03-12-2015.
 */
public class ReadSmsOTP extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Test 7","");
      intent = new Intent();
        String s =intent.getAction();
        Log.d("Action is",""+s);
        try {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                final Bundle bundle = intent.getExtras();

                CharSequence intentData = bundle.getString("message");
                System.out.println("Bundle Data " + intentData);
                  final Object[] pdusObj = (Object[]) bundle.get("pdus");
                System.out.println("pdusObj  " + pdusObj);
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])
                            pdusObj[i]);

                    System.out.println("currentMessageddd " + currentMessage);

                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    System.out.println("phoneNumber vishvendu " + phoneNumber);
                    System.out.println("senderNum vishvendu" + senderNum);
                    System.out.println("message vishvendu" + message);
                    try {
                        if (senderNum.equals("TA-DOCOMO")) {
                            CustomerAlreadyReg Sms = new CustomerAlreadyReg();
                            Sms.recivedSms(message);
                        }
                    } catch (Exception e) {
                    }

                }
            }

        } catch (Exception e) {

        }
    }
}


package com.infra.qrys_wallet;

import android.app.Application;

import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.unifiedpush.RegistrarManager;
import org.jboss.aerogear.android.unifiedpush.gcm.AeroGearGCMPushRegistrar;
import org.jboss.aerogear.android.unifiedpush.metrics.UnifiedPushMetricsMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeep.devhare on 25-11-2015.
 */
public class QRysWalletApplication extends Application {
    public static final String PUSH_REGISTER_NAME = "UNIFIED_PUSH_HELLOWORLD";
    public static final String PUSH_MESSAGE_FROM_BACKGROUND = "PUSH_MESSAGE_FROM_BACKGROUND";

    private List<String> messages;

    @Override
    public void onCreate() {
        super.onCreate();
        messages = new ArrayList<String>();

    }

    public List<String> getMessages() {
        return messages;
        //return Collections.unmodifiableList(messages);
    }

    public void addMessage(String newMessage) {
        messages.add(newMessage);
    }

    public void sendMetric(UnifiedPushMetricsMessage metricsMessage, Callback<UnifiedPushMetricsMessage> callback) {
        AeroGearGCMPushRegistrar registrar = (AeroGearGCMPushRegistrar)
                RegistrarManager.getRegistrar(PUSH_REGISTER_NAME);
        registrar.sendMetrics(metricsMessage, callback);
    }
}

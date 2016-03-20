/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.infra.qrys_wallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.infra.qrys_wallet.PushNotificationUtil.MetricsCallback;
import com.infra.qrys_wallet.PushNotificationUtil.NotificationBarMessageHandler;

import org.jboss.aerogear.android.unifiedpush.MessageHandler;
import org.jboss.aerogear.android.unifiedpush.RegistrarManager;
import org.jboss.aerogear.android.unifiedpush.gcm.UnifiedPushMessage;
import org.jboss.aerogear.android.unifiedpush.metrics.UnifiedPushMetricsMessage;

public class MessagesActivity extends AppCompatActivity implements MessageHandler {
    public static boolean fromNotification = false;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    private QRysWalletApplication application;
    private ListView listView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);
        Log.d("Test 1", "call Message Activity");
        application = (QRysWalletApplication) getApplication();
        Log.d("Test 2", "Application Cast");
        if (getIntent().getBooleanExtra(QRysWalletApplication.PUSH_MESSAGE_FROM_BACKGROUND, false)) {
            UnifiedPushMetricsMessage metricsMessage = new UnifiedPushMetricsMessage(getIntent().getExtras());
            application.sendMetric(metricsMessage, new MetricsCallback());
        }

        View emptyView = findViewById(R.id.empty);
        listView = (ListView) findViewById(R.id.messages);
        listView.setEmptyView(emptyView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RegistrarManager.registerMainThreadHandler(this);
        RegistrarManager.unregisterBackgroundThreadHandler(NotificationBarMessageHandler.instance);

        displayMessages();
    }

    @Override
    protected void onPause() {
        super.onPause();
        RegistrarManager.unregisterMainThreadHandler(this);
        RegistrarManager.registerBackgroundThreadHandler(NotificationBarMessageHandler.instance);
    }

    @Override
    public void onMessage(Context context, Bundle bundle) {
        addNewMessage(bundle);
        Log.d("Test 3", "onMessage Methode called");
    }

    @Override
    public void onDeleteMessage(Context context, Bundle message) {
    }

    @Override
    public void onError() {
    }

    private void addNewMessage(Bundle bundle) {
        String message = bundle.getString(UnifiedPushMessage.ALERT_KEY);
        application.addMessage(message);
        Log.d("Test 4", "Message Add");
        displayMessages();
    }

    private void displayMessages() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.message_item, application.getMessages());
        listView.setAdapter(adapter);
        Log.d("Test 5", "Message Display");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Test 6", "Notification Clicked" + application.getMessages());
                int len = application.getMessages().size();
                if (len > 0) {
                    if (application.getMessages().get(len - 1).startsWith("Your standing instruction has been invoked")) {
                        Log.e("yes", "its same");
                        fromNotification = true;
                        Toast.makeText(MessagesActivity.this, "standing instruction ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MessagesActivity.this,
                                InstaPay_notification.class);
                        intent.putExtra("PaymentData", application.getMessages().get(i).toString());
                        startActivity(intent);

                    } else {
                        Log.e("Notification", "is different");
                        Toast.makeText(MessagesActivity.this, "Not standing instruction ", Toast.LENGTH_SHORT).show();
                    }
                }

                application.getMessages().clear();

            }
        });
        adapter.notifyDataSetChanged();
    }
}

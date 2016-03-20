package com.infra.qrys_wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.infra.qrys_wallet.Utils.Constants;
import com.infra.qrys_wallet.Utils.DataBase;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.jboss.aerogear.android.core.Callback;
import org.jboss.aerogear.android.unifiedpush.PushRegistrar;
import org.jboss.aerogear.android.unifiedpush.RegistrarManager;
import org.jboss.aerogear.android.unifiedpush.gcm.AeroGearGCMPushJsonConfiguration;

import static com.infra.qrys_wallet.QRysWalletApplication.PUSH_REGISTER_NAME;

public class SplashScreen extends Activity {
    private static final String TAG = SplashScreen.class.getSimpleName();
    SharedPreference appPref;
    String DEVICE_ID;
    private ProgressDialog pd = null;
    private Object data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);
        register();
        /*DELETE SQLITE DATABASE*/
        DataBase.getInstance(SplashScreen.this).deleteContacts();
        Constants.checkSQLiteInserted = false;
        appPref.getInstance().Initialize(getApplicationContext());
        DEVICE_ID = appPref.getInstance().getString("Device_Id");
        System.out.println("SplashScreen Device ID " + DEVICE_ID.length());
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (DEVICE_ID != null && DEVICE_ID.length() > 0) {
                       /* Intent in = new Intent(SplashScreen.this,
                                DashBoardWallet.class);*/
                        Intent in = new Intent(SplashScreen.this,
                                Login.class);
                        startActivity(in);
                        SplashScreen.this.finish();
                    } else {
                       /* Intent in = new Intent(SplashScreen.this,
                                DashBoardWallet.class);*/
                        Intent in = new Intent(SplashScreen.this,
                                ApplicationIntro.class);
                        startActivity(in);
                        SplashScreen.this.finish();
                    }
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // finish the splash activity so it can't be returned to
                SplashScreen.this.finish();
            }
        }, 3000); // 3000 milliseconds
    }

    /*register Push Notification*/
    private void register() {
        RegistrarManager.config(PUSH_REGISTER_NAME, AeroGearGCMPushJsonConfiguration.class)
                .loadConfigJson(getApplicationContext())
                .asRegistrar();
        PushRegistrar registrar = RegistrarManager.getRegistrar(PUSH_REGISTER_NAME);
        registrar.register(getApplicationContext(), new Callback<Void>() {
            @Override
            public void onSuccess(Void data) {
                /*Toast.makeText(getApplicationContext(),
                        getApplicationContext().getString(R.string.registration_successful),
                        Toast.LENGTH_LONG).show();*/
                Log.d(TAG, "Test 0 Registration Success");
                Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, e.getMessage());
                /*Toast.makeText(getApplicationContext(),
                        getApplication().getString(R.string.registration_error),
                        Toast.LENGTH_LONG).show();*/
                finish();
            }
        });
    }
}
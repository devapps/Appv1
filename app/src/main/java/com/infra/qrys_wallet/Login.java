package com.infra.qrys_wallet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnTouchListener {
    TextView tvforgotMpin;
    EditText eMpin;
    Button bNext;
    Context c;
    JSONObject json, jsonInner;
    String mpinNO, opStatus, result, deviceId, respParams;
    ProgressDialog prgDialog;
    com.infra.qrys_wallet.Utils.MWRequest mwRequest;
    JSONObject mainObj = null;
    SharedPreference appPref;
    ArrayList<HashMap<String, String>> formList;
    private String TAG = "Login";
    private String blockCharacterSet = "~#^|$%&*!@ ()-+=?/{}[]<>,.;:'";
    String userAccountNo,shortBankName,cbsType,ifscCode,mmidCode,DeviceId,MobileNo;

    /*Special char filter */
    private InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        appPref.getInstance().Initialize(getApplicationContext());
        userAccountNo = appPref.getInstance().getString("SharedAccountNo");
        shortBankName = appPref.getInstance().getString("selectedBankDescription");
        cbsType = appPref.getInstance().getString("cBsType");
        mmidCode = appPref.getInstance().getString("SharedMMID");
        ifscCode =  appPref.getInstance().getString("SharedIFSC");
        DeviceId = appPref.getInstance().getString("Device_Id");
        MobileNo = appPref.getInstance().getString("user_mobile");



        System.out.println("SharedPreference Details In Login " + MobileNo + " " + DeviceId + " " + shortBankName + " " + cbsType + " " + userAccountNo + " " + mmidCode + " " + ifscCode);


        eMpin = (EditText) findViewById(R.id.edtmpin);
        bNext = (Button) findViewById(R.id.btngonext);
        tvforgotMpin = (TextView) findViewById(R.id.lblforgotmpin);
        tvforgotMpin.setOnTouchListener(this);
        eMpin.setFilters(new InputFilter[]{filter, new InputFilter.LengthFilter(4)});
        eMpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                bNext.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSubmitIfReady();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    public void enableSubmitIfReady() {
        if (eMpin.getText().toString().length() == 4) {
            mpinNO = eMpin.getText().toString();
            Log.d(TAG, "MPIN No " + mpinNO);
            bNext.setVisibility(View.VISIBLE);
            bNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Mpin().execute();
                }
            });
            Log.d("btn ", " shown");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.lblforgotmpin:
                new ResendOTP().execute();
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class Mpin extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, " Test 1 ");
            String resp = insertMpinNo();
            return resp;
        }        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           prgDialog = new ProgressDialog(Login.this);


            // Set Progress Dialog Text
         //   Log.d(TAG, "Test 0");
            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        private String insertMpinNo() {
            URL url = null;
            String resp = "";
            Log.d(TAG, " Test 1.0 ");

            System.out.println("Device ID " + deviceId);
            try {
                json = new JSONObject();
                json.put("ACTION", "");
                json.put("subActionId", "QRYSLOGINPIN");
                json.put("entityId", "QRYS");
                json.put("cbsType", cbsType);
                jsonInner = new JSONObject();
                jsonInner.put("DeviceId", DeviceId);
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("MobileNo", MobileNo);
                jsonInner.put("MPIN", mpinNO);
                jsonInner.put("upi_JSON_Data", "{\"action\":\"PaymentAddress\",\"subAction\":\"GetPaymentAddressList\",\"entityID\":\"mahb\",\"inputParam\":{\"entityID\":\"mahb\",\"mobileNo\":\"9867008241\",\"mpin\":\"1234\"}}");
                json.put("map", jsonInner);
                mwRequest = new MWRequest();
                resp = mwRequest.middleWareReq(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, " Test 2 " + s);
            if (s.equals("") || s == null) {

                prgDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                Log.d(TAG, " Test 2.0 ");
                JSONObject mainObj = null;
                try {
                    mainObj = new JSONObject(s);
                    respParams = mainObj.getString("responseParameterMW");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("Result");
                    if (opStatus.equals("00")) {
                        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, DashBoardWallet.class);
                        startActivity(intent);
                        finish();
                    } else {
                        prgDialog.dismiss();
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    prgDialog.dismiss();
                } catch (JSONException e) {
                    prgDialog.dismiss();
                    e.printStackTrace();
                }
                prgDialog.dismiss();
            }
        }


    }

    private class ResendOTP extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String resp = ResendOTPService();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(Login.this);
            // Set Progress Dialog Text
            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("") || s == null) {
                prgDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                JSONObject mainObj = null;
                try {
                    mainObj = new JSONObject(s);
                    respParams = mainObj.getString("responseParameterMW");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("resstatus");
                    Log.d("result", result);
                    if (result != null) {
                        if (opStatus.equals("00")) {
                            prgDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "OTP sent to mobile.", Toast.LENGTH_SHORT).show();
                            Intent callOtp = new Intent(Login.this, OTP.class);
                            startActivity(callOtp);
                            finish();
                            return;
                        } else {
                            prgDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Please try again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    prgDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //RESEND OTP SERVICE CALL ON FORGOT MPIN LINK.
    private String ResendOTPService() {
        String resp = "";
        try {
            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSRESENDOTP");
            json.put("entityId", "QRYS");
            jsonInner = new JSONObject();
            jsonInner.put("entityId", "QRYS");
            jsonInner.put("cbsType", cbsType);
            jsonInner.put("DEVICE_BUILD", "1.0");
            jsonInner.put("TYPE_OF_DEVICE", "ANDROID");
            jsonInner.put("MobileNo", MobileNo);
            jsonInner.put("DeviceId", DeviceId);
            json.put("map", jsonInner);
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("resp " + resp);
        } catch (Exception e) {
        }
        return resp;
    }

}

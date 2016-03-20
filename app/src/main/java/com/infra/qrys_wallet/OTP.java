package com.infra.qrys_wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.Utils.DataHolderClass;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by sandeep.devhare on 16-11-2015.
 */

public class OTP extends Activity implements View.OnClickListener, View.OnTouchListener {
    JSONObject json, jsonInner;
    MWRequest mwRequest;
    String mobileNo, opStatus, result, deviceId, respParams;
    ProgressDialog prgDialog;
    SharedPreference appPref;
    private Button btnValidateSubmit, btnResend;
    private EditText MobileNoOTP, getOTP;
    private TextView tvresenduserotp;

    private String otpMobileNumberValue, otpValue,userAccountNo,shortBankName,cbsType,ifscCode,mmidCode,OTP,MobileNo;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);
        appPref.getInstance().Initialize(getApplicationContext());
        mobileNo =  appPref.getInstance().getString("user_mobile");
        userAccountNo = appPref.getInstance().getString("SharedAccountNo");
        shortBankName = appPref.getInstance().getString("selectedBankDescription");
        cbsType = appPref.getInstance().getString("cBsType");
        mmidCode = appPref.getInstance().getString("SharedMMID");
        ifscCode =  appPref.getInstance().getString("SharedIFSC");

        System.out.println("SharedPreference Details In OTP " + mobileNo + " " + shortBankName + " " + cbsType + " " + userAccountNo + " " + mmidCode + " " + ifscCode);

        MobileNoOTP = (EditText) findViewById(R.id.usermobileno);
        MobileNoOTP.setText(mobileNo);
        getOTP = (EditText) findViewById(R.id.otpforaddaccount);
        tvresenduserotp = (TextView) findViewById(R.id.tvresend);
        btnValidateSubmit = (Button) findViewById(R.id.btngonext);
        btnValidateSubmit.setOnClickListener(this);
        tvresenduserotp.setOnTouchListener(this);

        Intent intent = new Intent("MyCustomIntent");

        // add data to the Intent
        intent.setAction("com.broadcastreceiver.A_CUSTOM_INTENT");
        sendBroadcast(intent);

        getOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // bNext.setEnabled(false);
                btnValidateSubmit.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSubmitIfReady();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //enableSubmitIfReady();
            }
        });
        MobileNoOTP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // bNext.setEnabled(false);
                btnValidateSubmit.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSubmitIfReady();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //enableSubmitIfReady();
            }
        });
    }


    public void recivedSms(String message)
    {


        try
        {
            getOTP.setText(message);
        }
        catch (Exception e)
        {
        }
    }
    public void enableSubmitIfReady() {
        if (getOTP.getText().toString().length() == 4 || MobileNoOTP.getText().toString().length() == 10) {
            btnValidateSubmit.setVisibility(View.VISIBLE);
            btnValidateSubmit.setOnClickListener(this);
        }
        //  boolean isReady = eMobileNo.getText().toString().length()==10;
        //  bNext.setEnabled(isReady);
    }

    @Override
    public void onClick(View v) {
        Log.d("getId =", "helo" + v.getId());
        //
        switch (v.getId()) {
            case R.id.btngonext:
                otpMobileNumberValue = MobileNoOTP.getText().toString();
                otpValue = getOTP.getText().toString();
                Log.d("btnSubmit clicked", "");
                if ((otpMobileNumberValue.matches(""))) {
                    Toast.makeText(getApplicationContext(), "Please Enter your Number", Toast.LENGTH_LONG).show();
                    return;
                } else if (otpValue.matches("")) {
                    Toast.makeText(getApplicationContext(), "Enter OTP Number.", Toast.LENGTH_LONG).show();
                    return;
                } else {
//                    Intent intent = new Intent(OTP.this, SetMpin.class);
//                    startActivity(intent);
//                    finish();
                    new OtpValidation().execute();
                }
                break;
            default:
                break;
        }
        return;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        otpMobileNumberValue = MobileNoOTP.getText().toString();
        if ((otpMobileNumberValue.matches(""))) {
            Toast.makeText(getApplicationContext(), "Please Enter your Number", Toast.LENGTH_LONG).show();
        } else {
            new ResendOTP().execute();
        }
        return false;
    }

    private String otpvalidation() {
        String resp = "";

        try {
             OTP = getOTP.getText().toString();
            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSVALOTP");
            json.put("entityId", "QRYS");
            jsonInner = new JSONObject();
            jsonInner.put("entityId", "QRYS");
            jsonInner.put("cbsType", cbsType);
            jsonInner.put("DEVICE_BUILD", "1.0");
            jsonInner.put("TYPE_OF_DEVICE", "ANDROID");
            jsonInner.put("MobileNo", mobileNo);
            jsonInner.put("accountno",userAccountNo);
            jsonInner.put("otpCode", OTP);
            json.put("map", jsonInner);
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("resp " + resp);
        } catch (Exception e) {
        }
        return resp;
    }

    private class OtpValidation extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String resp = otpvalidation();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(OTP.this);
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
                    deviceId = respObj.getString("DeviceId");
                    System.out.println("Device ID Set " + deviceId);
                    appPref.getInstance().setString("Device_Id", deviceId);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("resstatus");
                    Log.d("result", result);
                    if (result != null) {
                        if (opStatus.equals("00")) {
                            Intent in = new Intent(OTP.this, SetMpin.class);
                            startActivity(in);
                            finish();
                        } else {
                            prgDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
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



    private String ResendOTPService() {
        String resp = "";
        try {
             MobileNo = MobileNoOTP.getText().toString();
            deviceId = appPref.getInstance().getString("Device_Id");
            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSRESENDOTP");
            json.put("entityId", "QRYS");
            jsonInner = new JSONObject();
            jsonInner.put("entityId", "QRYS");
            jsonInner.put("cbsType",cbsType);
            jsonInner.put("DEVICE_BUILD", "1.0");
            jsonInner.put("TYPE_OF_DEVICE", "ANDROID");
            jsonInner.put("MobileNo", MobileNo);
            jsonInner.put("DeviceId", deviceId);
            json.put("map", jsonInner);
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("resp " + resp);
        } catch (Exception e) {
        }
        return resp;
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
            prgDialog = new ProgressDialog(OTP.this);
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
                    deviceId = respObj.getString("DeviceId");
                    System.out.println("Device ID Set " + deviceId);
                    appPref.getInstance().setString("Device_Id", deviceId);
                    DataHolderClass.getInstance().setDeviceID(deviceId);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("resstatus");
                    Log.d("result", result);
                    if (result != null) {
                        if (opStatus.equals("00")) {
                            prgDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "OTP sent to mobile.", Toast.LENGTH_SHORT).show();
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
}
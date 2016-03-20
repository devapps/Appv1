package com.infra.qrys_wallet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sagar Makwana on 15-10-2015.
 */
public class SetMpin extends Activity implements View.OnClickListener {

    String userMpin = "";
    String confirmUserMpin = "";
    boolean isConfirmMPIN = false;

    ImageView mpinImg1, mpinImg2, mpinImg3, mpinImg4;
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0;
    LinearLayout LL;
    ImageButton ClearBtnLayout;

    TextView setMPINMsg,userFullName;

    ProgressDialog prgDialog;
    JSONObject json, jsonInner;
    MWRequest mwRequest;
    String opStatus, result, respParams;
    String userAccountNo,shortBankName,cbsType,ifscCode,mmidCode,DeviceId,MobileNo;
    SharedPreference appPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_mpin);

        appPref.getInstance().Initialize(getApplicationContext());
        userAccountNo = appPref.getInstance().getString("SharedAccountNo");
        shortBankName = appPref.getInstance().getString("selectedBankDescription");
        cbsType = appPref.getInstance().getString("cBsType");
        mmidCode = appPref.getInstance().getString("SharedMMID");
        ifscCode =  appPref.getInstance().getString("SharedIFSC");
        DeviceId = appPref.getInstance().getString("Device_Id");
        MobileNo = appPref.getInstance().getString("user_mobile");
        System.out.println("SharedPreference Details In SetMpin " + MobileNo + " "+DeviceId+" " + shortBankName + " " + cbsType + " " + userAccountNo + " " + mmidCode + " " + ifscCode);

        userMpin = "";
        confirmUserMpin = "";
        isConfirmMPIN = false;

        mpinImg1 = (ImageView) findViewById(R.id.imageMpin1);
        mpinImg2 = (ImageView) findViewById(R.id.imageMpin2);
        mpinImg3 = (ImageView) findViewById(R.id.imageMpin3);
        mpinImg4 = (ImageView) findViewById(R.id.imageMpin4);

        setMPINMsg = (TextView) findViewById(R.id.textSetMpin);
        setMPINMsg.setText(getResources().getString(R.string.LBL_SET_MPIN));
        userFullName = (TextView) findViewById(R.id.tvusername);
        userFullName.setText(appPref.getInstance().getString("fName") + " " + appPref.getInstance().getString("lName"));
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        button0 = (Button) findViewById(R.id.button0);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button0.setOnClickListener(this);


        LL = (LinearLayout) findViewById(R.id.mpinSubmit);
        LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isConfirmMPIN) {
                    if (confirmUserMpin.length() < 4) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.MSG_CONFIRM_MPIN) ,
                                Toast.LENGTH_LONG).show();
                    } else {
                        if (confirmUserMpin.equals(userMpin)){
                            new SubmitMPIN().execute();
                            //Service call
                        } else {
                            confirmUserMpin = "";
                            setMPINMsg.setText(getResources().getString(R.string.LBL_CONFIRM_MPIN));
                            mpinImageUpdate();
                            Toast.makeText(getApplicationContext(), "Please re-enter valid MPIN.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    if (userMpin.length() < 4) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.MSG_ENTER_MPIN) ,
                                Toast.LENGTH_LONG).show();
                    } else {
                        isConfirmMPIN = true;
                        confirmUserMpin = "";
                        setMPINMsg.setText(getResources().getString(R.string.LBL_CONFIRM_MPIN));
                        mpinImageUpdate();
                    }
                }

            }
        });

        ClearBtnLayout = (ImageButton) findViewById(R.id.clearMpinBtn);
        ClearBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConfirmMPIN) {
                    clearConfirmMPIN();
                } else {
                    clearMPIN();
                }
            }
        });
    }

    void clearMPIN() {
        userMpin = "";
        setMPINMsg.setText(getResources().getString(R.string.LBL_SET_MPIN));
        mpinImageUpdate();
    }

    void clearConfirmMPIN() {
        confirmUserMpin = "";
        setMPINMsg.setText(getResources().getString(R.string.LBL_CONFIRM_MPIN));
        mpinImageUpdate();
    }

    void mpinImageUpdate() {

        int mpinLength;
        if (isConfirmMPIN) {
            mpinLength = confirmUserMpin.length();
        } else {
            mpinLength = userMpin.length();
        }
        switch (mpinLength) {
            case 0:
                mpinImg1.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg2.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg3.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg4.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                break;
            case 1:
                mpinImg1.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg2.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg3.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg4.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                break;
            case 2:
                mpinImg1.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg2.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg3.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg4.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                break;
            case 3:
                mpinImg1.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg2.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg3.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg4.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                break;
            case 4:
                mpinImg1.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg2.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg3.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg4.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        System.out.println(userMpin);
        if (isConfirmMPIN) {
            if (confirmUserMpin.length() < 4) {
                switch (v.getId()) {
                    case R.id.button1:
                        confirmUserMpin = confirmUserMpin + "1";
                        break;
                    case R.id.button2:
                        confirmUserMpin = confirmUserMpin + "2";
                        break;
                    case R.id.button3:
                        confirmUserMpin = confirmUserMpin + "3";
                        break;
                    case R.id.button4:
                        confirmUserMpin = confirmUserMpin + "4";
                        break;
                    case R.id.button5:
                        confirmUserMpin = confirmUserMpin + "5";
                        break;
                    case R.id.button6:
                        confirmUserMpin = confirmUserMpin + "6";
                        break;
                    case R.id.button7:
                        confirmUserMpin = confirmUserMpin + "7";
                        break;
                    case R.id.button8:
                        confirmUserMpin = confirmUserMpin + "8";
                        break;
                    case R.id.button9:
                        confirmUserMpin = confirmUserMpin + "9";
                        break;
                    case R.id.button0:
                        confirmUserMpin = confirmUserMpin + "0";
                        break;
                }
                mpinImageUpdate();
            }
        } else {
            if (userMpin.length() < 4) {
                switch (v.getId()) {
                    case R.id.button1:
                        userMpin = userMpin + "1";
                        break;
                    case R.id.button2:
                        userMpin = userMpin + "2";
                        break;
                    case R.id.button3:
                        userMpin = userMpin + "3";
                        break;
                    case R.id.button4:
                        userMpin = userMpin + "4";
                        break;
                    case R.id.button5:
                        userMpin = userMpin + "5";
                        break;
                    case R.id.button6:
                        userMpin = userMpin + "6";
                        break;
                    case R.id.button7:
                        userMpin = userMpin + "7";
                        break;
                    case R.id.button8:
                        userMpin = userMpin + "8";
                        break;
                    case R.id.button9:
                        userMpin = userMpin + "9";
                        break;
                    case R.id.button0:
                        userMpin = userMpin + "0";
                        break;
                }
                mpinImageUpdate();
            }
        }
    }

    private class SubmitMPIN extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(SetMpin.this);
            prgDialog.setMessage(getResources().getString(R.string.MSG_PLEASE_WAIT));
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resp = mpinSubmission();
            return resp;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("") || s == null) {
                prgDialog.dismiss();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.MSG_UNABLE_TO_REACH_SERVER) , Toast.LENGTH_LONG).show();
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
                            Intent in = new Intent(SetMpin.this,Login.class);
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

    private String mpinSubmission() {
        String resp="";
        try{

            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSSETPIN");
            json.put("entityId", "QRYS");

            jsonInner = new JSONObject();

            jsonInner.put("entityId", "QRYS");
            jsonInner.put("cbsType", cbsType);
            jsonInner.put("DEVICE_BUILD", "1.0");
            jsonInner.put("DeviceId", DeviceId);
            jsonInner.put("TYPE_OF_DEVICE", "ANDROID");
            jsonInner.put("MobileNo", MobileNo);
           // jsonInner.put("customerId","3574926");
            jsonInner.put("MPIN",confirmUserMpin);
//            jsonInner.put("RRN","");
            json.put("map", jsonInner);

            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("resp "+resp);
            appPref.getInstance().setString("confirmUserMpin_shared", confirmUserMpin);

        }catch(Exception e) {}

        return resp;
    }
}



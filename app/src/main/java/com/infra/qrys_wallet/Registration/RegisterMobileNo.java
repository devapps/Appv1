package com.infra.qrys_wallet.Registration;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.infra.qrys_wallet.CustomerAlreadyReg;
import com.infra.qrys_wallet.Login;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegisterMobileNo extends AppCompatActivity {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_BUNDLE = "pdus";
    private BroadcastReceiver receiver;
    Spinner spCountrycode;
    EditText eMobileNo;
    Button bNext;
    JSONObject json, jsonInner;
    String[] Data = new String[]{"India(+91)"};
    String mobileNo, opStatus, result, deviceId, respParams, CountryName, CountryPhoneNumber;
    ProgressDialog prgDialog;
    MWRequest mwRequest;
    JSONObject mainObj = null;
    // ArrayList<HashMap<String, String>> formList;
    ArrayList<String> country;
    JSONArray CountrycodeJsonArray = null;
    ArrayList<HashMap<String, String>> CountryList;
    ArrayList<String> addData;
    SharedPreference appPref;
    private String TAG = "RegisterMobileNo";

    @Override
    protected void onStart() {
        super.onStart();
        // new CountrynamesWithCodes().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_mobile_no);
        appPref.getInstance().Initialize(getApplicationContext());
        spCountrycode = (Spinner) findViewById(R.id.spcountrycodes);
        eMobileNo = (EditText) findViewById(R.id.edtmobileno);
        bNext = (Button) findViewById(R.id.btngonext);
        CountryList = new ArrayList<HashMap<String, String>>();
        addData = new ArrayList<String>();
       // new FetchCountryCode().execute();
        ArrayAdapter<String> ad = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_display, Data);
        spCountrycode.setAdapter(ad);
        eMobileNo.addTextChangedListener(new TextWatcher() {
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
      /*  *//*BroadCast Receiver code*//*
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        Log.d("Receiver 0", "Call");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //do something based on the intent's action
                Toast.makeText(context, "ON RECEIVE BROADCAST", Toast.LENGTH_LONG).show();
                Log.d("Test 0","Customer Already Reg");
                if (intent.getAction().equals(ACTION)) {
                    Bundle intentExtras = intent.getExtras();
                    if (intentExtras != null) {
                        Log.d("Test 1", "Customer Already Reg");
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
                      *//*  //this will update the UI with message
                        CustomerAlreadyReg inst = CustomerAlreadyReg.instance();
                        inst.updateList(smsMessageStr);*//*
                    }
                }
            }
        };
        registerReceiver(receiver, filter);*/
    }

    public void enableSubmitIfReady() {
        if (eMobileNo.getText().toString().length() == 10) {
            mobileNo = eMobileNo.getText().toString();
            appPref.getInstance().setString("user_mobile", mobileNo);
            Log.d(TAG, "Mobile No " + mobileNo);
            bNext.setVisibility(View.VISIBLE);
            bNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Without service call Testing */
                 /*   Intent intent = new Intent(RegisterMobileNo.this, com.infra.qrys.Registration.TypeOfUser.class);
                    startActivity(intent);
                    finish();*/
                    /*With Service Call Testing*/

                    new RegisterMobileNoService().execute();

                }
            });
            Log.d("btn ", " shown");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_register_mobile_no, menu);
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

    private String findCountryCode() {
        URL url = null;
        String resp = "";
        Log.d(TAG, " Test 1.0 ");
        try {
            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSCOUNTRYCODES");
            json.put("entityId", "QRYS");
            json.put("cbsType", "QRYS");
            jsonInner = new JSONObject();
            jsonInner.put("entityId", "QRYS");
            json.put("map", jsonInner);
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    /*-------------------*/
    private class RegisterMobileNoService extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, " Test 1 ");
            String resp = insertMobileNo();
            return resp;
        }        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(RegisterMobileNo.this);
            // Set Progress Dialog Text
            Log.d(TAG, "Test 0");
            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        private String insertMobileNo() {
            URL url = null;
            String resp = "";
            Log.d(TAG, " Test 1.0 ");
            try {
                json = new JSONObject();
                json.put("ACTION", "");
                json.put("subActionId", "QRYSMOBINSERT");
                json.put("entityId", "QRYS");
                json.put("cbsType", "QRYS");
                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("MobileNo", mobileNo);
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
/*"responseParameter":{"Result":"Customer already register.","resstatus":"03","opstatus":"03"}}*/
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("Result");
                    if (opStatus.equals("00")) {
                        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                       // Intent intent = new Intent(RegisterMobileNo.this, TypeOfUser.class);
                        Intent intent = new Intent(RegisterMobileNo.this, IndividualUserInfo.class);


                        startActivity(intent);
                        finish();
                    }else if(opStatus.equals("03"))
                    {
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RegisterMobileNo.this, CustomerAlreadyReg.class);
                        startActivity(intent);
                        finish();
                    }else {
                        prgDialog.dismiss();
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterMobileNo.this, Login.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                    prgDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private class FetchCountryCode extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String resp = findCountryCode();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(RegisterMobileNo.this);
            // Set Progress Dialog Text
            Log.d(TAG, "Test 0");
            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, " Test 2 " + s);
            if (s.equals("") || s == null) {
                prgDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                //  System.out.println("inside countrycode response ");
                JSONObject mainObj = null;
                try {
                    mainObj = new JSONObject(s);
                    JSONObject responseParams = mainObj.getJSONObject("set");
                    System.out.println("thisforcountrycode " + responseParams);
                    CountrycodeJsonArray = responseParams.getJSONArray("records");
                    for (int i = 0; i < CountrycodeJsonArray.length(); i++) {
                        JSONObject CountryCodeObject = CountrycodeJsonArray.getJSONObject(i);
                        CountryName = CountryCodeObject.getString("COUNTRYNAME");
                        CountryPhoneNumber = CountryCodeObject.getString("phone_number");
                        System.out.println("CountryNameforsinglelist " + CountryName);
                        HashMap<String, String> CountryDetails = new HashMap<String, String>();
                        CountryDetails.put("COUNTRYNAME", CountryName);
                        CountryDetails.put("phone_number", CountryPhoneNumber);
                        System.out.println("CountryDetails " + CountryDetails);
                        Iterator it = CountryDetails.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            Map.Entry pair2 = (Map.Entry) it.next();
                            System.out.println("HASHMAP " + pair.getKey() + " = " + pair.getValue());
                            addData.add(pair.getValue().toString() + " (" + pair2.getValue().toString() + ")");
                        }
                        System.out.println("CountryListinner " + CountryList);
                    }
                } catch (Exception e) {
                }
                ArrayAdapter<String> ad = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item_display, addData);
                spCountrycode.setAdapter(ad);
                prgDialog.dismiss();
            }
        }
    }
}


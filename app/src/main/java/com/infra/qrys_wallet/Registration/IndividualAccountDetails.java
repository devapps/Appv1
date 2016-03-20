package com.infra.qrys_wallet.Registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.OTP;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.MaskedWatcher;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sandeep.devhare on 15-10-2015.
 */
public class IndividualAccountDetails extends Activity {

    EditText MMID,AccountNo,IFSC;
    Button GO;
    JSONObject json, jsonInner;
    com.infra.qrys_wallet.Utils.MWRequest mwRequest;
    String mobileNo, opStatus, result, deviceId,shortBankName,cbsType, respParams,selectedBankName;
    String mmid,ifsc,accountNo;
    ProgressDialog prgDialog;
    SharedPreference appPref;
    private String blockCharacterSet = "~#^|$%&*!@ ()-+=?/{}[]<>,.;:'";
ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_user_account_details);
        appPref.getInstance().Initialize(getApplicationContext());
        MMID=(EditText)findViewById(R.id.mmid);
        AccountNo=(EditText)findViewById(R.id.editAccountNo);
        IFSC=(EditText)findViewById(R.id.editIFSCCode);
        GO=(Button)findViewById(R.id.userAccountButton);
        TextView tvpName = (TextView) findViewById(R.id.textUserName);
        TextView tvBankName = (TextView) findViewById(R.id.tvbankname);
        TextView tvNbinSet = (TextView) findViewById(R.id.textBankCode);
        backButton = (ImageButton)findViewById(R.id.imageButton);

       // tvNbinSet.setText(com.infra.qrys.Utils.DataHolderClass.getInstance().getnBinNo());
        selectedBankName = appPref.getInstance().getString("selectedBankName");
        tvBankName.setText(selectedBankName);
        tvpName.setText(appPref.getInstance().getString("fName") + " " + appPref.getInstance().getString("lName"));

        AccountNo.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(20)});
        IFSC.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(11)});
        MMID.addTextChangedListener(new MaskedWatcher("#######"));
        AccountNo.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // bNext.setEnabled(false);
                GO.setVisibility(View.GONE);
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

        IFSC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // bNext.setEnabled(false);
                GO.setVisibility(View.GONE);
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
        MMID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // bNext.setEnabled(false);
                GO.setVisibility(View.GONE);
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(IndividualAccountDetails.this,IndividualBankList.class);
                startActivity(in);
                finish();
            }
        });


    }
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
    /*submit call*/
    public void enableSubmitIfReady() {
        try {
  /*get value from fields*/
            mmid = MMID.getText().toString();
            ifsc = IFSC.getText().toString();
            accountNo = AccountNo.getText().toString();

            appPref.getInstance().setString("SharedMMID", mmid);
            appPref.getInstance().setString("SharedIFSC", ifsc);
            appPref.getInstance().setString("SharedAccountNo", accountNo);

            if ((IFSC.getText().toString().equals("")) || (AccountNo.getText().toString().equals(""))
                    ) {
                GO.setVisibility(View.GONE);
            } else {
                GO.setVisibility(View.VISIBLE);
                GO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new submitUserAccountInfo().execute();
                    }
                });
            }
            Log.d("btn ", " shown");
            /*Call service to send All Data*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class submitUserAccountInfo extends AsyncTask<String,Void,String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(IndividualAccountDetails.this);
            // Set Progress Dialog Text

            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
            prgDialog.show();
        }


        @Override
        protected String doInBackground(String... strings) {
            String resp = insertUserAccountInfo();
            return resp;
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
                    respParams = mainObj.getString("responseParameter");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("resstatus");
                    Log.d("result", result);

                        if (opStatus.equals("101")) {
                            prgDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Error: ", Toast.LENGTH_SHORT).show();
                            return;
                        } else if(opStatus.equals("00")){
                            Intent in = new Intent(IndividualAccountDetails.this, OTP.class);
                            startActivity(in);
                            finish();

                        }else
                        {
                            prgDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    prgDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String insertUserAccountInfo() {

        String resp="";

        mobileNo = appPref.getInstance().getString("user_mobile");
        shortBankName = appPref.getInstance().getString("selectedBankDescription");
        cbsType = appPref.getInstance().getString("cBsType");
        System.out.println("Details "+mobileNo+" "+shortBankName+" "+cbsType);
        String UserMMID=MMID.getText().toString();
        String UserAccountNo=AccountNo.getText().toString();
        String UserIFSC=IFSC.getText().toString();




        try{

            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSACCOUTMOBVER");
            json.put("entityId", "QRYS");

            jsonInner = new JSONObject();

            jsonInner.put("entityId", "QRYS");
            jsonInner.put("cbsType", cbsType);
            jsonInner.put("DEVICE_BUILD", "1");
            jsonInner.put("TYPE_OF_DEVICE", "ANDROID");
            jsonInner.put("QrysSelBank", shortBankName);
            jsonInner.put("MobileNo", mobileNo);
            jsonInner.put("ifsc_code", UserIFSC);
            jsonInner.put("accountno", UserAccountNo);

            json.put("map", jsonInner);

            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("resp "+resp);

        }catch(Exception e)
        {

        }

        return resp;
        // return null;
    }
}

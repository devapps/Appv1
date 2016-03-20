package com.infra.qrys_wallet.Registration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.SetMpin;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by sandeep.devhare on 14-10-2015.
 */
public class IndividualUserInfo extends AppCompatActivity {
    EditText eFname, eLname, eEmailid;
    Button bNext;

    String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String userID, FirstName, LastName, EmailID;
    String fName, lName;
    JSONObject json, jsonInner;
    MWRequest mwRequest;
    String mobileNo, opStatus, result,respParams;
    ProgressDialog prgDialog;
    SharedPreference appPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_user_info);
        appPref.getInstance().Initialize(getApplicationContext());
        mobileNo = appPref.getInstance().getString("user_mobile");
        eFname = (EditText) findViewById(R.id.edtfirstname);
        eLname = (EditText) findViewById(R.id.edtlastname);
      //  eUserid = (EditText) findViewById(R.id.edtuserid);
        eEmailid = (EditText) findViewById(R.id.edtuseremailid);
        bNext = (Button) findViewById(R.id.btngonext);
        /*Applying textwatcher for all input fields*/
        /*First Name*/
        eFname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // bNext.setEnabled(false);
                bNext.setVisibility(View.GONE);
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

        eLname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // bNext.setEnabled(false);
                bNext.setVisibility(View.GONE);
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
  /*      eUserid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // bNext.setEnabled(false);
                bNext.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableSubmitIfReady();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                //enableSubmitIfReady();
            }
        });*/

    }
    /*submit call*/
    public void enableSubmitIfReady() {
        try {
  /*get value from fields*/



            if ((eFname.getText().toString().equals("")) || (eLname.getText().toString().equals("") )
                    ) {
                bNext.setVisibility(View.GONE);
            } else {
                bNext.setVisibility(View.VISIBLE);
                bNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirstName = eFname.getText().toString();
                        LastName = eLname.getText().toString();

                        EmailID = eEmailid.getText().toString().trim();


                        appPref.getInstance().setString("UserID", userID);
                        appPref.getInstance().setString("fName",FirstName);
                        appPref.getInstance().setString("lName",LastName);
                        appPref.getInstance().setString("emailID", EmailID);
                        Log.d("btn ", " clicked"+EmailID);
                        if(EmailID.matches(emailPattern)&& eEmailid.getText().toString().length()>0)
                        {
                            new submitUserInfo().execute();
                        }else
                        {
                            Toast.makeText(getApplicationContext(), "Not Valid Email Id", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }

            /*Call service to send All Data*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class submitUserInfo extends AsyncTask<String,Void,String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(IndividualUserInfo.this);
            // Set Progress Dialog Text

            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resp = insertUserInfo();
            return resp;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            prgDialog.dismiss();
            if (s.equals("") || s == null) {

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

                    if (opStatus.equals("00")) {
                        prgDialog.dismiss();
                        System.out.println("Going to");
                        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                       // Intent intent = new Intent(IndividualUserInfo.this, IndividualBankList.class);
                        Intent intent = new Intent(IndividualUserInfo.this, SetMpin.class);
                        startActivity(intent);
                        finish();
                    } else {
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

    private String insertUserInfo() {

        String resp="";
        try{

            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSUPDATEPRSDET");
            json.put("entityId", "QRYS");

            jsonInner = new JSONObject();

            jsonInner.put("entityId", "QRYS");
            jsonInner.put("cbsType", "QRYS");
            jsonInner.put("TYPE_OF_DEVICE", "ANDROID");
            jsonInner.put("MobileNo", mobileNo);
            jsonInner.put("UserID","1");
            jsonInner.put("FirstName",FirstName);
            jsonInner.put("LastName",LastName);
            jsonInner.put("email_id", EmailID);
            json.put("map", jsonInner);

            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("resp "+resp);

        }catch(Exception e)
        {
        }
        return resp;
    }
    /*Validation Person name*/
    public void Is_Valid_Person_Name(EditText edt) throws NumberFormatException {
        if (edt.getText().toString().length() <= 0) {
            edt.setError("Accept Alphabets Only.");
            fName = null;
            lName = null;
        } else if (!edt.getText().toString().matches("[a-zA-Z ]+")) {
            edt.setError("Accept Alphabets Only.");
            fName = null;
            lName = null;
        } else {
            fName = edt.getText().toString();
            lName = edt.getText().toString();
        }
    }

}
package com.infra.qrys_wallet.Registration;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.Adapters.Individual_BankListAdapter;
import com.infra.qrys_wallet.Models.BankListModel;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sandeep.devhare on 15-10-2015.
 */
public class IndividualBankList extends Activity {
    Individual_BankListAdapter adapter;
    EditText inputSearch;
    TextView personName;
    JSONObject json, jsonInner;
    String opStatus, result, deviceId, respParams, userMobileNo, data, bdescription, bName, bType,cBsType,selectedbanktype, selectedBankDescription,selectedBankName;
    int Nbin;
    public ProgressDialog prgDialog;
    MWRequest mwRequest;
    ArrayList<BankListModel> items;
    // List view
    private ListView lv;
    private String TAG = "IndividualBankList";
    SharedPreference appPref;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_listofbanks);
        appPref.getInstance().Initialize(getApplicationContext());
//        BankList listOfBanks = new BankList();
//        listOfBanks.execute();
        lv = (ListView) findViewById(R.id.list_view);
        items = new ArrayList<BankListModel>();
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        personName = (TextView) findViewById(R.id.tvpersonname);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        Log.d(TAG, "Test 0");
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        personName.setText(appPref.getInstance().getString("fName") + " " + appPref.getInstance().getString("lName"));
        // Adding items to listview
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                IndividualBankList.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new BankList().execute();
    }

    private String insertServiceParameter() {
        URL url = null;
        String resp = "";
        Log.d(TAG, " Test 1.0 ");
        try {
            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSBANKLIST");
            json.put("entityId", "QRYS");
            jsonInner = new JSONObject();
            jsonInner.put("entityId", "QRYS");
            jsonInner.put("DEVICE_BUILD", "1");
            jsonInner.put("cbsType", "QRYS");
            jsonInner.put("TYPE_OF_DEVICE", "ANDROID");
            jsonInner.put("MobileNo", userMobileNo);
            json.put("map", jsonInner);
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    private void Dialog() {
        Button bproceed;
        final Dialog dialog = new Dialog(IndividualBankList.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        TextView tvSelectedBank = (TextView) dialog.findViewById(R.id.tvbankselected);
        tvSelectedBank.setText("You have selected a " + selectedbanktype);
        dialog.findViewById(R.id.btncancel).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        dialog.findViewById(R.id.otpok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent i = new Intent(IndividualBankList.this, IndividualAccountDetails.class);
                startActivity(i);
                finish();
            }
        });
        dialog.show();
    }

    private class BankList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, " Test 1 ");
            String resp = insertServiceParameter();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            deviceId = appPref.getInstance().getString("Device_Id");
            userMobileNo=appPref.getInstance().getString("user_mobile");

            Log.d("DeviceId " + deviceId, "Mobile No " + userMobileNo);

            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            prgDialog.dismiss();
            Log.d(TAG, " Test 2 " + s);
            if (s.equals("") || s == null) {
                prgDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                prgDialog.dismiss();
                Log.d(TAG, " Test 2.0 ");
                JSONObject mainObj = null;
                try {

                    mainObj = new JSONObject(s);
                    respParams = mainObj.getString("set");
                    Log.d(TAG, " respParams " + respParams);
                    JSONObject respObj = new JSONObject(respParams);
                    String firstNode = respObj.getString("setname");
                    String secondNode = respObj.getString("records");
                    Log.d(TAG, " setName " + firstNode);
                    Log.d(TAG, " records " + secondNode);
                    String resp = mainObj.getString("responseParameter");
                    JSONObject respObjforstatus = new JSONObject(resp);
                    opStatus = respObjforstatus.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObjforstatus.getString("resstatus");
                    Log.d("result", result);
                    String totallist = respObjforstatus.getString("totalListCount");
                    Log.d("totallist", totallist);
                    int l = Integer.parseInt(totallist);
                    Log.d("length", " " + l);


                    /*Read All Bank Names and */
                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray = respObj.optJSONArray("records");
                    System.out.println(" jsonArray.length() "+ jsonArray.length());
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        bdescription = jsonObject.getString("DESCRIPTION");
                        bType = jsonObject.getString("partnerBank");
                        bName = jsonObject.getString("bankName");
                        cBsType = jsonObject.getString("cbsType");
                     //  Nbin = Integer.parseInt(jsonObject.optString("nbin").toString());



                        data = "Node" + i + " : \n BName= " + bName + " \n BType= " + bType +"\n ShortName "+bdescription+"\n cbsType"+cBsType;
                        Log.d("Data", " " + data);
                        items.add(new BankListModel(bName, bType,bdescription,cBsType));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                prgDialog.dismiss();
                    if (opStatus.equals("00")) {

                        Log.d(TAG, " Test 2.1 ");
                        Log.d(TAG, "Add data in list view");
                        Log.d(TAG, "then show dialog on list item click");
                        adapter = new Individual_BankListAdapter(IndividualBankList.this, items);
                        lv.setAdapter(adapter);
                        prgDialog.dismiss();
                        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                TextView txt1 = (TextView) parent.getChildAt(position).findViewById(R.id.tvbankname);
                                selectedBankName = txt1.getText().toString();
                                appPref.getInstance().setString("selectedBankName",selectedBankName);

                                TextView txt = (TextView) parent.getChildAt(position).findViewById(R.id.tvbanktype);
                                selectedbanktype = txt.getText().toString();
//
                                TextView txt3 = (TextView) parent.getChildAt(position).findViewById(R.id.tvbankdescription);
                                selectedBankDescription = txt3.getText().toString();
                                Log.v("Short Name", "" + selectedBankDescription);
                                appPref.getInstance().setString("selectedBankDescription",selectedBankDescription);

                                TextView txt4 = (TextView) parent.getChildAt(position).findViewById(R.id.cbsType);
                                cBsType = txt4.getText().toString();
                                Log.v("cBsType", "" + cBsType);
                                appPref.getInstance().setString("cBsType",cBsType);

                                Log.v("value ", "result is " + selectedbanktype+" "+selectedBankName+" "+selectedBankDescription);
                                Dialog();
                            }
                        });
                    } else {
                        prgDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
                        return;
                    }
                prgDialog.dismiss();
            }

            prgDialog.dismiss();
        }
    }
}


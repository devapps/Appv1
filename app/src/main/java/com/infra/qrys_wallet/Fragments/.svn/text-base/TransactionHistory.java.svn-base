package com.infra.qrys_wallet.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.infra.qrys_wallet.Adapters.TransactionHistory_Adapter;
import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * Created by sandeep.devhare on 15-09-2015.
 */
public class TransactionHistory extends Fragment {
    public static String DATE = "DATE";
    public static String MONTH = "MONTH";
    public static String YEAR = "YEAR";
    public static String MSG = "MSG";
    public static String REMARK = "REMARK";
    public static String RS = "RS";
    public static String STATUS = "STATUS";
    public static String TAG = "Txn_History";
    public ProgressDialog prgDialog;
    ListView lv;
    JSONObject json, jsonInner;
    String opStatus, result, respParams,  data, txnAmount, txnStatus, txnRemark, txnMsg, txnDate, bName, bType, cBsType;
    HashMap<String, String> map = null;
    String getMonth;
    List<HashMap<String, String>> transactionDataCollection;
    MWRequest mwRequest;
    SharedPreference appPref;
    String userMobileNo,userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId,fNAME, lNAME, custFullName,bankName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Transaction History");
        DashBoardWallet.fragmentID=2;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
       // ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View rootView = inflater.inflate(R.layout.transaction_history, container, false);

        appPref.getInstance().Initialize(getActivity());
        userAccountNo = appPref.getInstance().getString("SharedAccountNo");
        shortBankName = appPref.getInstance().getString("selectedBankDescription");
        cbsType = appPref.getInstance().getString("cBsType");
        mmidCode = appPref.getInstance().getString("SharedMMID");
        ifscCode = appPref.getInstance().getString("SharedIFSC");
        DeviceId = appPref.getInstance().getString("Device_Id");
        userMobileNo = appPref.getInstance().getString("user_mobile");
        fNAME = appPref.getInstance().getString("fName");
        lNAME = appPref.getInstance().getString("lName");
        custFullName = appPref.getInstance().getString("CustFull_Name");
        bankName =   appPref.getInstance().getString("selectedBankName");


        System.out.println("SharedPreference Details In TransactionHistory Fragment \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode+" "+fNAME+" "+lNAME+" \n "+custFullName+" "+bankName);


        lv = (ListView) rootView.findViewById(R.id.list_transaction_history);
        prgDialog = new ProgressDialog(getActivity());
        // Set Progress Dialog Text
        Log.d(TAG, "Test 0");
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        LoadTransactionHistory loadTxnHistory = new LoadTransactionHistory();
        loadTxnHistory.execute();
        transactionDataCollection =
                new ArrayList<HashMap<String, String>>();



       /* Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println("HASHMAP " + pair.getKey() + " = " + pair.getValue());
        }*/
        return rootView;
    }

    /*---------------*/
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

//@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_transaction_history, menu);
        return true;
    }

    /*Load Txn History*/
// Load data on background
    class LoadTransactionHistory extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, " Test 1 ");
            String resp = insertServiceParameter();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            userMobileNo = appPref.getInstance().getString("user_mobile");
            Log.d("Mobile No ", " " + userMobileNo);
            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            prgDialog.dismiss();
            Log.d(TAG, "Responce" + s);
            if (s.equals("") || s == null) {
                prgDialog.dismiss();
                Toast.makeText(getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
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
                    System.out.println(" jsonArray.length() " + jsonArray.length());
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        map = new HashMap<String, String>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        txnAmount = jsonObject.getString("txn_amount");

                        txnRemark = jsonObject.getString("remarks");
                        txnMsg = jsonObject.getString("impsMessage");
                        txnDate = jsonObject.getString("dateofTxn");
                        txnStatus = jsonObject.getString("Status");

                        /*Row*/
                        /*Date connversion*/
                        SimpleDateFormat inputDF = new SimpleDateFormat("yyyy-MM-dd");

                        Date date1 = null;
                        try {

                            date1 = inputDF.parse(txnDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(date1);

                        int month = cal.get(Calendar.MONTH)+1;
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        int year = cal.get(Calendar.YEAR);

                        System.out.println(month + " - " + day + " - " + year);
                        getMonth = getMonthForInt(month);
                        System.out.println("month is " +getMonth);


                        map.put(DATE, String.valueOf(day));
                        map.put(MONTH, getMonth);
                        map.put(YEAR, String.valueOf(year));
                        map.put(MSG, txnMsg);
                        map.put(REMARK, txnRemark);
                        map.put(STATUS, txnStatus);



                        /*--*/
                        map.put(RS, txnAmount);
                        transactionDataCollection.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TransactionHistory_Adapter bindTra_hist_Data = new TransactionHistory_Adapter(getActivity(), transactionDataCollection);
                lv.setAdapter(bindTra_hist_Data);
                lv.setFastScrollEnabled(true);
            }
        }
        String getMonthForInt(int num) {
                String month = "wrong";
                DateFormatSymbols dfs = new DateFormatSymbols();
                String[] months = dfs.getMonths();
                if (num >= 0 && num <= 11 ) {
                    month = months[num];
                }
                return month;
            }
        private String insertServiceParameter() {
            URL url = null;
            String resp = "";
            Log.d(TAG, " Test 1.0 ");
            try {
                json = new JSONObject();
                json.put("entityId", "QRYS");
                json.put("actionId", "ENQUIRY");
                json.put("subActionId", "QRYSTRANSACTIONSEARCH");
                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("DEVICE_BUILD", "1");
                jsonInner.put("cbsType", cbsType);
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
    }
}
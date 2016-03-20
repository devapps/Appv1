package com.infra.qrys_wallet.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infra.qrys_wallet.Adapters.mWallet_Deals_List_Adapter;
import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class mWalletOffers extends Fragment {

    public static String DealDescription = "DESCRIPTION";
    public static String DealName = "DEALNAME";
    public static String DealAmount = "txn_amount";
    public static String DealImage = "imagePath";
    public static String TAG = "Deals";
    public static String DealDescStatus = "DEALDESCRIPTION";
    public static String DealCouponCode = "COUPONCODE";
    public static String DealStatus = "DEALSTATUS";
    public static String DealDays = "DEALDAYS";
    public static String DealID = "DEALID";

    public ProgressDialog prgDialog;
    ListView lv;
    JSONObject json, jsonInner;
    String opStatus, result, respParams,cBsType;
    String DEALDESCRIPTION, COUPONCODE, DEALSTATUS, DEALDAYS, DEALID, DESCRIPTION, DEALNAME, imagePath, txn_amount;
    HashMap<String, String> map = null;
    List<HashMap<String, String>> dealDataCollection;
    MWRequest mwRequest;
    SharedPreference appPref;
    FragmentManager fragmentManager;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo,fNAME, lNAME, custFullName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashBoardWallet.fragmentID = 14;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Offers");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
       // ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View rootView = inflater.inflate(R.layout.mwallet_offers, container, false);

        appPref.getInstance().Initialize(this.getActivity().getApplicationContext());
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

        System.out.println("SharedPreference Details In DASHBOARD \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode+" "+fNAME+" "+lNAME+" "+custFullName);
        lv = (ListView) rootView.findViewById(R.id.list_offers);
        prgDialog = new ProgressDialog(getActivity());
        // Set Progress Dialog Text
        Log.d(TAG, "Test 0");
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        LoadDeals loadDeal = new LoadDeals();
        loadDeal.execute();
        dealDataCollection =
                new ArrayList<HashMap<String, String>>();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                System.out.println(dealDataCollection.get(position).get(DealName));
                Bundle b = new Bundle();
                b.putString(DealDescription, dealDataCollection.get(position).get(DealDescription));
                b.putString(DealName, dealDataCollection.get(position).get(DealName));
                b.putString(DealAmount, dealDataCollection.get(position).get(DealAmount));
                b.putString(DealImage, dealDataCollection.get(position).get(DealImage));
                b.putString(DealDescStatus, dealDataCollection.get(position).get(DealDescStatus));
                b.putString(DealCouponCode, dealDataCollection.get(position).get(DealCouponCode));
                b.putString(DealStatus, dealDataCollection.get(position).get(DealStatus));
                b.putString(DealDays, dealDataCollection.get(position).get(DealDays));
                b.putString(DealID, dealDataCollection.get(position).get(DealID));

                fragmentManager = getFragmentManager();
                FragmentTransaction ft2 = fragmentManager.beginTransaction();
/*
                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }
*/
                mWalletOffersDetail offerDetail = new mWalletOffersDetail();
                ft2.replace(R.id.frame_container, offerDetail);
               // ft2.addToBackStack("optional tag");
                offerDetail.setArguments(b);
                ft2.commit();
            }
        });
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
    class LoadDeals extends AsyncTask<String, Void, String> {
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
            userAccountNo = appPref.getInstance().getString("user_mobile");
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
                    String resp = mainObj.getString("responseParameter");
                    JSONObject respObjforstatus = new JSONObject(resp);
                    opStatus = respObjforstatus.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObjforstatus.getString("resstatus");
                    Log.d("result", result);
                    if(result.equals("01") || opStatus.equals("01")){
                        Toast.makeText(getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                        return;
                    }
                    respParams = mainObj.getString("listofDataset");
                    Log.d(TAG, " respParams " + respParams);
                    JSONArray temp = new JSONArray(respParams);
                    if(temp.length() <= 0){
                        Toast.makeText(getActivity(), "No Deals found.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    JSONObject respObj = new JSONObject(temp.getJSONObject(0).toString());
                    String firstNode = respObj.getString("setname");
                    String secondNode = respObj.getString("records");
                    Log.d(TAG, " setName " + firstNode);
                    Log.d(TAG, " records " + secondNode);

                    String totallist = respObjforstatus.getString("totalListCount");
                    Log.d("totallist", totallist);
                    int l = Integer.parseInt(totallist);
                    Log.d("length", " " + l);


                    /*Read All Bank Names and */
                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray = respObj.optJSONArray("records");
                    Log.d("jsonArray.length()", " jsonArray.length() " + jsonArray.length());
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        map = new HashMap<String, String>();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        DESCRIPTION = jsonObject.getString("DESCRIPTION");
                        DEALNAME = jsonObject.getString("DEALNAME");
                        txn_amount = jsonObject.getString("txn_amount");
                        imagePath = jsonObject.getString("imagePath");
                        DEALDESCRIPTION = jsonObject.getString("DEALDESCRIPTION");
                        COUPONCODE = jsonObject.getString("COUPONCODE");
                        DEALSTATUS  = jsonObject.getString("DEALSTATUS");
                        DEALDAYS = jsonObject.getString("DEALDAYS");
                        DEALID = jsonObject.getString("DEALID");

                        Log.d("DEALNAME",DEALNAME);

                        map.put(DealDescription, DESCRIPTION);
                        map.put(DealName, DEALNAME);
                        map.put(DealAmount, txn_amount);
                        map.put(DealImage, imagePath);
                        map.put(DealDescStatus, DEALDESCRIPTION);
                        map.put(DealCouponCode, COUPONCODE);
                        map.put(DealStatus, DEALSTATUS);
                        map.put(DealDays, DEALDAYS);
                        map.put(DealID, DEALID);

                        dealDataCollection.add(map);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("dealDataPassing",dealDataCollection.toString());

                mWallet_Deals_List_Adapter bindTra_hist_Data = new mWallet_Deals_List_Adapter(getActivity(), dealDataCollection);
                lv.setAdapter(bindTra_hist_Data);
                lv.setFastScrollEnabled(true);
            }
        }

        private String insertServiceParameter() {
            URL url = null;
            String resp = "";
            Log.d(TAG, " Test 1.0 ");
            try {
                json = new JSONObject();
                json.put("entityId", "QRYS");
                json.put("actionId", " ");
                json.put("subActionId", "QRYSOFFERSDEALS");
                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("DEVICE_BUILD", "1");
                jsonInner.put("cbsType",cbsType);
                jsonInner.put("accountno", userAccountNo);//userAccountNo);
                jsonInner.put("DEALOFFER", "D");
                jsonInner.put("TYPE_OF_DEVICE", "ANDROID");
                jsonInner.put("city", "Mumbai");
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
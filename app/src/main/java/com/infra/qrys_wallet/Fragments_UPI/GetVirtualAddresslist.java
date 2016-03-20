package com.infra.qrys_wallet.Fragments_UPI;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infra.qrys_wallet.Adapters.UPI_VirtualAddressListAdapter;
import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.Fragments.SetPaymentAddressFragment;
import com.infra.qrys_wallet.Models.VirtualAddressList;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GetVirtualAddresslist extends Fragment implements View.OnClickListener {

    String resp;
    JSONObject json, jsonInner, jsonMWInner;
    MWRequest mwRequest;
    SharedPreference appPref;
    String userMobileNo, userMpin, Status, MiddlewareResponse, AddressList_Middleware, c, DeviceId, MobileNo, cbsType, ResultMW, AddresslistMW;
    JSONArray addresslist = null;
    ArrayList<VirtualAddressList> Addressitems;
    ListView VirtualAddressList;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    Context context;
    UPI_VirtualAddressListAdapter adapter;

    public GetVirtualAddresslist() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Virtual Payment list");
        System.out.println("inside getVirtualAddress");
        DashBoardWallet.fragmentID = 16;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        userMobileNo = appPref.getInstance().getString("user_mobile");
        userMpin = appPref.getInstance().getString("confirmUserMpin_shared");
        DeviceId = appPref.getInstance().getString("Device_Id");
        MobileNo = appPref.getInstance().getString("user_mobile");
        cbsType = appPref.getInstance().getString("cBsType");
        Addressitems = new ArrayList<VirtualAddressList>();
        // System.out.println("userMobileNo shared"+ userMobileNo + "userMpin "+ userMpin);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_get_virtual_addresslist, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        VirtualAddressList = (ListView) rootView.findViewById(R.id.VirtualAddressList);
        new getVirtualAddressList().execute();
        fab.setOnClickListener(this);


        return rootView;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fab:
                SetPaymentAddressFragment PayAdd = new SetPaymentAddressFragment();
                fragmentManager = getFragmentManager();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, PayAdd);
                ft.commit();
        }

    }

    private class getVirtualAddressList extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {


            resp = getVirtualList();
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("after calling async ", "afte asynctask5");

            System.out.println("Middleware data response " + s);
            if (s.equals("") || s == null || s.startsWith("Error")) {
                Toast.makeText(GetVirtualAddresslist.this.getActivity(), "No Data", Toast.LENGTH_LONG).show();
                return;
            } else {
                try {
                    JSONObject mainObj = new JSONObject(s);
                    // Status=mainObj.getString("opstatus");
                    // MiddlewareResponse=mainObj.getString("msg");
                    AddressList_Middleware = mainObj.getString("responseParameterMW");

                    System.out.println("get AddressList_Middleware " + AddressList_Middleware);

                    JSONObject innerObject = new JSONObject(AddressList_Middleware);

                    ResultMW = innerObject.getString("Result");
                    System.out.println("get ResultMW " + ResultMW);


                    JSONObject addresslistObject = new JSONObject(ResultMW);
                    AddresslistMW = addresslistObject.getString("responseParameter");
                    System.out.println("get AddresslistMW " + AddresslistMW);


                    JSONObject addresslistfinal = new JSONObject(AddresslistMW);

                    addresslist = addresslistfinal.getJSONArray("addresslist");
                    System.out.println("get addresslist " + addresslist.length());
                    for (int i = 0; i < addresslist.length(); i++) {
                        c = (String) addresslist.get(i);
                        System.out.println("get jsonAddress " + c);
                        Addressitems.add(new VirtualAddressList(c));
                    }

                    System.out.println("outside Addressitems " + Addressitems);


                    // String DataStringfilter=mainObj.toString().replace("\"", "\\\"");


                } catch (Exception e) {
                    String err = (e.getMessage() == null) ? "calling Dialog failed" : e.getMessage();

                }
                System.out.println("Addressitems " + Addressitems);

              try{
                  adapter = new UPI_VirtualAddressListAdapter(GetVirtualAddresslist.this.getActivity(), Addressitems);
                  VirtualAddressList.setAdapter(adapter);
              }catch(NullPointerException np){
                  np.printStackTrace();
              }

                VirtualAddressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    }
                });

            }
        }
    }

    public JSONObject JsonData() {
        json = new JSONObject();
        try {
            json.put("action", "PaymentAddress");
            json.put("subAction", "GetPaymentAddressList");
            json.put("entityID", "infrapsp");
            jsonInner = new JSONObject();
            jsonInner.put("entityID", "infrapsp");
            jsonInner.put("mobileNo", MobileNo);
            jsonInner.put("mpin", "1234");
            json.put("inputParam", jsonInner);
           /* mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getVirtualList() {

        String resp = "";
        JSONObject mydata = JsonData();
        String DataString = mydata.toString().replace("\"", "\\\"");
        System.out.println("mydata into string" + mydata.toString().replace("\"", "\\\""));
        try {
            System.out.println("getVirtualList method");
            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "UPIREQUESTHANDLER");
            json.put("entityId", "QRYS");
            json.put("cbsType", cbsType);
            jsonInner = new JSONObject();
            jsonInner.put("DeviceId", DeviceId);
            jsonInner.put("entityId", "QRYS");
            jsonInner.put("MobileNo", MobileNo);
            jsonInner.put("MPIN", "1234");
            jsonInner.put("upi_JSON_Data", DataString);
            json.put("map", jsonInner);
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;


    }
}

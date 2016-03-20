package com.infra.qrys_wallet.Fragments_UPI;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.Constants;
import com.infra.qrys_wallet.Utils.MWRequest_json;

import org.json.JSONObject;

import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class UPI_OTP extends Fragment {

    String resp,Status,MiddlewareResponse,AddressList_Middleware,responseupi_otp;
    JSONObject json, jsonInner;
    MWRequest_json mwRequest;
    FragmentManager fragmentManager;
    private FragmentTransaction ft;
    public UPI_OTP() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_upi__ot, container, false);

        EditText otp=(EditText)rootView.findViewById(R.id.upi_otp);
        Button otp_button=(Button)rootView.findViewById(R.id.nextButton);

        new sendOTP().execute();


        otp_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UPI_SelectedBankAccount upi_selectedBankAccount=new UPI_SelectedBankAccount();
                fragmentManager=getFragmentManager();
                ft=fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container,upi_selectedBankAccount);
                ft.commit();

            }
        });

        return rootView;
    }


    private class sendOTP extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
             resp=getUPI_OTP();
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("") || s == null||s.startsWith("Error")) {
                Toast.makeText(UPI_OTP.this.getActivity(), "No Data", Toast.LENGTH_LONG).show();
                return;
            }
            else{

                try{

                    Log.d("after calling async ", "afte asynctask5");
                    JSONObject mainObj = new JSONObject(s);
                    Status=mainObj.getString("status");
                    MiddlewareResponse=mainObj.getString("msg");
                    if(Status.equals("true")){


                        AddressList_Middleware=mainObj.getString("responseParameter");

                        System.out.println("get AddressList_Middleware "+AddressList_Middleware);

                        JSONObject innerObj = new JSONObject(AddressList_Middleware);


                        responseupi_otp=innerObj.getString("otp");
                        System.out.println("get otp "+responseupi_otp);
                    }


                }catch(Exception e){

                }
            }
        }
    }

    private String getUPI_OTP() {
        URL url = null;
        String resp = "";
        Constants.changeURL = true;

        try {
            System.out.println("getUPI_OTP method");
            json = new JSONObject();
            json.put("action", "ManageTPINService");
            json.put("subAction", "GenerateOTPFromCBS");
            //json.put("subAction", "GetPSPList");
            json.put("entityID", "ZABC");
            jsonInner = new JSONObject();
            jsonInner.put("entityID", Constants.entityID_UPI);
            //  jsonInner.put("phoneNo", "9867008241");
            jsonInner.put("mobileNo", "9167161861");

            json.put("inputParam", jsonInner);
            mwRequest = new MWRequest_json();
            resp = mwRequest.serviceReq(json);
            System.out.println("response from UPI for virtual list "+resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
}

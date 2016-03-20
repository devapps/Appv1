package com.infra.qrys_wallet.Fragments_UPI;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.Constants;
import com.infra.qrys_wallet.Utils.MWRequest_json;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONObject;

import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmTPIN extends Fragment {

    String resp,DebitCard_Shared,CVV_Shared,ExpiryDate_Shared,PaymentAddress_Shared,appPrefAccountNo,MobileNo_Shared,AccountType_shared,IFSC_Shared;
    JSONObject json, jsonInner;
    MWRequest_json mwRequest;
    SharedPreference appPref;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    public ConfirmTPIN() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_confirm_tpin, container, false);

        EditText SetTpin=(EditText)rootView.findViewById(R.id.Confirmtpin);
        Button ConfirmTPINButton=(Button)rootView.findViewById(R.id.ConfirmTpinbutton);


        DebitCard_Shared=  appPref.getInstance().getString("DebitCardStr_upi");
        ExpiryDate_Shared= appPref.getInstance().getString("ExpiryDateStr_upi");
        CVV_Shared=  appPref.getInstance().getString("CVVStr_upi");
        appPrefAccountNo = appPref.getInstance().getString("SelectedAccountNo_upi");
        MobileNo_Shared =appPref.getInstance().getString("user_mobile");

        AccountType_shared=  appPref.getInstance().getString("SelectedAccountType_upi");
        IFSC_Shared =appPref.getInstance().getString("SelectedIFSC_upi");

       PaymentAddress_Shared= appPref.getInstance().getString("PaymentAddressStr_upi");
System.out.println("DebitCard_Shared from app"+DebitCard_Shared+"ExpiryDate_Shared "+ExpiryDate_Shared+" CVV_Shared "+CVV_Shared);



        ConfirmTPINButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PassTPIN().execute();


            }
        });
        return rootView;
    }


    private class PassTPIN extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {

            resp=getTPIN();
            return resp;
        }
    }

    private String getTPIN() {

        URL url = null;
        String resp = "";
        Constants.changeURL = true;

        try {
            System.out.println("getTPIN method");
            json = new JSONObject();
            json.put("action", "PaymentAddress");
            json.put("subAction", "AddPaymentAddress");
            //json.put("subAction", "GetPSPList");
            json.put("entityID", Constants.entityID_UPI);
            jsonInner = new JSONObject();
            jsonInner.put("entityID", Constants.entityID_UPI);
            jsonInner.put("custName", "Hemal Kotecha");
            jsonInner.put("mobileNo", MobileNo_Shared);
            jsonInner.put("accountOTP", "123456");
            jsonInner.put("paymentAddress", PaymentAddress_Shared);
            jsonInner.put("ifsc", IFSC_Shared);
            jsonInner.put("accountNo", appPrefAccountNo);
            jsonInner.put("addressType", "Custom");
            jsonInner.put("accountType", AccountType_shared);
            jsonInner.put("mpin", "123456");
            jsonInner.put("rupayCardNo", DebitCard_Shared);
            jsonInner.put("expDate", ExpiryDate_Shared);
            jsonInner.put("cvv", CVV_Shared);


            json.put("inputParam", jsonInner);
            mwRequest = new MWRequest_json();
            resp = mwRequest.serviceReq(json);
            System.out.println("response from UPI for getTPIN "+resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;

    }
}

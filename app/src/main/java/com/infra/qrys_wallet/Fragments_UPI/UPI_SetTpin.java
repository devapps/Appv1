package com.infra.qrys_wallet.Fragments_UPI;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.Constants;
import com.infra.qrys_wallet.Utils.MWRequest_json;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONObject;

import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class UPI_SetTpin extends Fragment implements View.OnClickListener{

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    EditText DebitCard,ExpiryDate,CVV;
    TextView AccountNoTextview;
    Button Cancel,Next;
    String resp,AccountType_shared,AccountNo_Shared,MobileNo_Shared,Status,DebitCardStr,ExpiryDateStr,CVVStr;
    JSONObject json, jsonInner;
    MWRequest_json mwRequest;
    SharedPreference appPref;
    SharedPreferences mMyPreferences;
    View rootView;
    public UPI_SetTpin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView= inflater.inflate(R.layout.fragment_upi__set_tpin, container, false);


        AccountType_shared=  appPref.getInstance().getString("SelectedAccountType_upi");
        AccountNo_Shared = appPref.getInstance().getString("SelectedAccountNo_upi");

        System.out.println("AccountNo_Shared " + AccountNo_Shared);

        MobileNo_Shared =appPref.getInstance().getString("user_mobile");

        AccountNoTextview=(TextView)rootView.findViewById(R.id.accountID);
        AccountNoTextview.setText(AccountNo_Shared);


        Next=(Button)rootView.findViewById(R.id.nextID);
        Cancel=(Button)rootView.findViewById(R.id.cancelID);





        Next.setOnClickListener(this);
        Cancel.setOnClickListener(this);


        return rootView;
    }

    public void clear_pref(){

        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = mMyPreferences.edit();
        editor.remove("SelectedAccountNo_upi");
        editor.commit();
    }
    @Override
    public void onClick(View v) {

        switch(v.getId()){


            case R.id.nextID :
                DebitCard=(EditText)rootView.findViewById(R.id.debitcardedittext);
                DebitCardStr=DebitCard.getText().toString();
                System.out.println("DebitCardStr "+DebitCardStr);
                ExpiryDate=(EditText)rootView.findViewById(R.id.expirydate);
                ExpiryDateStr=ExpiryDate.getText().toString();
                System.out.println("ExpiryDateStr "+ExpiryDateStr);
                CVV=(EditText)rootView.findViewById(R.id.cvv);
                CVVStr=CVV.getText().toString();
                System.out.println("CVVStr "+CVVStr);


                appPref.getInstance().setString("DebitCardStr_upi", DebitCardStr);
                appPref.getInstance().setString("ExpiryDateStr_upi", ExpiryDateStr);
                appPref.getInstance().setString("CVVStr_upi",CVVStr);
                new TpinService().execute();
        }

    }

    private class TpinService extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {

            resp=getTpinService();
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("") || s == null||s.startsWith("Error")) {
                Toast.makeText(UPI_SetTpin.this.getActivity(), "", Toast.LENGTH_LONG).show();
                return;
            }
            else{

                try{

                    JSONObject mainObj = new JSONObject(s);
                    Status=mainObj.getString("status");

                    if(Status.equals("true")){

                        SetTpinFragment setTpinFragment=new SetTpinFragment();
                        fragmentManager=getFragmentManager();
                        ft=fragmentManager.beginTransaction();
                        ft.replace(R.id.frame_container,setTpinFragment);
                        ft.commit();

                    }


                }catch(Exception e){

                }
            }
        }
    }

    private String getTpinService() {

        System.out.println("getUPILIST method");
        URL url = null;
        String resp = "";
        Constants.changeURL = true;

        try {
            json = new JSONObject();
            json.put("action", "ManageTPINService");
            json.put("subAction", "GenerateOTPFromCBS");
            //json.put("subAction", "GetPSPList");
          //  json.put("entityID", "mahb");
            json.put("entityID", Constants.entityID_UPI);
            jsonInner = new JSONObject();
            jsonInner.put("entityID", Constants.entityID_UPI);
            jsonInner.put("mobileNo", MobileNo_Shared);
            jsonInner.put("accountNo", AccountNo_Shared);
            jsonInner.put("rupayCardNo", DebitCard);
            jsonInner.put("expDate", ExpiryDate);
            jsonInner.put("cvv", CVV);
            json.put("inputParam", jsonInner);
            mwRequest = new MWRequest_json();
            resp = mwRequest.serviceReq(json);
            System.out.println("response from UPI for setTPIN"+resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
}

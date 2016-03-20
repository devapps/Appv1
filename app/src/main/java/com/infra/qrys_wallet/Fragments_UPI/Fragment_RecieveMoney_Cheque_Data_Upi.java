package com.infra.qrys_wallet.Fragments_UPI;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.MWUPIRequest_json;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_RecieveMoney_Cheque_Data_Upi extends Fragment {


    public Fragment_RecieveMoney_Cheque_Data_Upi() {
        // Required empty public constructor
    }

    View rootview;
    String chequeNum,resp, amount, issueDate, chequeNumberResponse,status,benName;
    JSONObject json, jsonInner;
    MWUPIRequest_json mwRequest;
    MWRequest mwjsonRequest;FragmentManager fragmentManager;
    FragmentTransaction ft;
    SharedPreference appPref;
    EditText editTextAmount, editTextIssueDate, editTextChequeNumber,editTextBenName;
    public static boolean notificationAlert= false;
    Button btnSendNotification;
    int flag = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_fragment__recieve_money__cheque__data__upi, container, false);

        chequeNum= appPref.getInstance().getString("chequeNum");
        /*Bundle bundle = this.getArguments();
        chequeNum =bundle.getString("chequeNum");
        Log.d("chequeNum", chequeNum);*/
        editTextAmount = (EditText)rootview.findViewById(R.id.editTextAmount);
        editTextBenName = (EditText)rootview.findViewById(R.id.editTextBenName);
        editTextChequeNumber = (EditText)rootview.findViewById(R.id.editTextChequeNumber);
        editTextIssueDate = (EditText)rootview.findViewById(R.id.editTextIssuedDate);
        btnSendNotification = (Button)rootview.findViewById(R.id.buttonSendNotification);

        editTextChequeNumber.setText(chequeNum);

        new getchequeData().execute();
        btnSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag == 1)
                    new MyTask().execute();
            }
        });


        return rootview;
    }


    private class getchequeData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String resp = receiveMoneyCheque();
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("") || s == null || s.startsWith("Error")) {
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_LONG).show();
                return;
            } else {

                try {
                    JSONObject mainObj = new JSONObject(s);
                    amount = mainObj.getString("amount");
                    issueDate = mainObj.getString("issuedDate");
                    chequeNumberResponse = mainObj.getString("chequeNo");
                    status = mainObj.getString("status");
                    benName = mainObj.getString("benificaryName");

                    editTextIssueDate.setText(issueDate);
                    editTextBenName.setText(benName);
                    editTextAmount.setText(amount);


                    if(status.equals("SUCCESS") || "SUCCESS" == status )
                    {

                        /*NOTIFICATION SERVICE CALL***********************************************************/

                        flag = 1;

                    }
                    else
                    {
                        Toast errmsg = Toast.makeText(getActivity(),"Something went Wrong..", Toast.LENGTH_LONG);
                        errmsg.show();
                    }
                } catch (JSONException e) {
                    Log.d("error","***********************************************************");
                    e.printStackTrace();
                    Log.d("error", "**********************************************************");
                }

            }
        }

        private String receiveMoneyCheque() {
            URL url = null;
            String resp = "";


            try {
                json = new JSONObject();
                json.put("action", "ChequeData");
                json.put("subAction", "getChequeData");
                json.put("entityID", Constants.entityID_UPI);

                jsonInner = new JSONObject();
                jsonInner.put("chequeNo", chequeNum);

                json.put("inputParam", jsonInner);

                mwRequest = new MWUPIRequest_json();
                resp = mwRequest.serviceReq(json);

            } catch (JSONException e) {
                Log.d("error", "***********************************************************");
                e.printStackTrace();
                Log.d("error", "**********************************************************");
            }
            return resp;
        }
    }

    private class MyTask extends AsyncTask<String, String , String >{
        @Override
        protected String doInBackground(String... params) {

            String resp = NotificationToServer();
            return resp;


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            Log.d("valueeee","1");
            alert.setMessage("A Reminder has been sent to the Payee successfully...");

            Log.d("valueeee", "2");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Wallet_UPI wallet_upi = new Wallet_UPI();
                    fragmentManager = getFragmentManager();
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container, wallet_upi);
                    ft.commit();
                }
            });

            final AlertDialog alertDialog = alert.create();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.show();
                }
            }, 3000);

        }
    }

    private String NotificationToServer() {

        String resp="";



        json = new JSONObject();
        try {
            notificationAlert=true;
            json.put("message",benName + " has deposited the cheque no: " + chequeNumberResponse + " of amount " + amount);

            mwjsonRequest = new MWRequest();
            resp = mwjsonRequest.middleWareReq(json);

            System.out.println("notification response "+ resp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resp;
    }
}

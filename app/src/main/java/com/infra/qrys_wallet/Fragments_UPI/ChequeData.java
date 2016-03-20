package com.infra.qrys_wallet.Fragments_UPI;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.Constants;
import com.infra.qrys_wallet.Utils.MWUPIRequest_json;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Calendar;

/**
 * Created by vaibhav.ghanekar on 17-03-2016.
 */
public class ChequeData extends Fragment {

    View rootView;
    Button btnProceed;
    JSONObject json, jsonInner;
    EditText editTextBenName, editTextAmount, editTextIssuedDate, editTextMobileNumber;
    MWUPIRequest_json mwRequest;
    String mobileNumber, resp, serviceResponse, chequeNumber;
    SharedPreference appPref;
    Calendar myCalendar = Calendar.getInstance();
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    int mYear;
    int mMonth;
    int mDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.chequedata, container, false);

        btnProceed = (Button) rootView.findViewById(R.id.buttonProceed);
        editTextBenName = (EditText) rootView.findViewById(R.id.editTextBenName);
        editTextAmount = (EditText) rootView.findViewById(R.id.editTextAmount);
        editTextIssuedDate = (EditText) rootView.findViewById(R.id.editTextIssuedDate);
        editTextMobileNumber = (EditText) rootView.findViewById(R.id.editTextMobileNumber);


        mYear = myCalendar.get(Calendar.YEAR);
        mMonth = myCalendar.get(Calendar.MONTH);
        mDay = myCalendar.get(Calendar.DAY_OF_MONTH);

        //Edited by Vaibhav...
        Bundle bundle = this.getArguments();
        chequeNumber =bundle.getString("chequeNumber");
        Log.d("chequeNumber",chequeNumber);


        editTextIssuedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                editTextIssuedDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new getchequeData().execute();

            }
        });
        return rootView;
    }

    private class getchequeData extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            resp = issueCheque();
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
                    serviceResponse = mainObj.getString("status");
                    Log.d("serviceResponse",serviceResponse);

                    if(serviceResponse.equals("SUCCESS") || "SUCCESS" == serviceResponse )
                    {
                        Log.d("insideif","if");
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        Log.d("valueeee","1");
                        alert.setMessage("Saved Successfully");

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

                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
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

        private String issueCheque() {
            URL url = null;
            String resp = "";


            try {
                json = new JSONObject();
                json.put("action", "ChequeData");
                json.put("subAction", "setChequeData");
                json.put("entityID", Constants.entityID_UPI);

                jsonInner = new JSONObject();
                jsonInner.put("entityID", Constants.entityID_UPI);
                jsonInner.put("chequeNo", chequeNumber);
                jsonInner.put("issuedDate", editTextIssuedDate.getText().toString());
                jsonInner.put("amount", editTextAmount.getText().toString());
                jsonInner.put("benificaryName", editTextBenName.getText().toString());
                jsonInner.put("mobileNo", editTextMobileNumber.getText().toString());
                jsonInner.put("entityID", Constants.entityID_UPI);

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

}

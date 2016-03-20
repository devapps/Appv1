package com.infra.qrys_wallet.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by sandeep.devhare on 29-10-2015.
 */
public class ChangeMPIN extends Fragment implements View.OnClickListener {

    String currentMPIN = "";
    String userMpin = "";
    String confirmUserMpin = "";
    String serviceType = "VERIFYMPIN";
    boolean isNewPINMPINEntered = false;
    boolean isCurrentPINEntered = false;

    ImageView mpinImg1, mpinImg2, mpinImg3, mpinImg4;
    Button button1, button2, button3, button4, button5, button6, button7, button8, button9, button0;
    LinearLayout LL;
    ImageView ClearBtnLayout;

    TextView setMPINMsg,userFullName;

    ProgressDialog prgDialog;
    JSONObject json, jsonInner;
    MWRequest mwRequest;
    String opStatus, result, respParams;
    SharedPreference appPref;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo,fNAME, lNAME, custFullName,bankName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Change MPIN");
        DashBoardWallet.fragmentID=3;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View rootView = inflater.inflate(R.layout.change_mpin, container, false);

        userMpin = "";
        confirmUserMpin = "";
        isNewPINMPINEntered = false;
        isCurrentPINEntered = false;
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
        System.out.println("SharedPreference Details In ChangeMpin Fragment \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode+" "+fNAME+" "+lNAME+" \n "+custFullName+" "+bankName);


        mpinImg1 = (ImageView) rootView.findViewById(R.id.imageMpin1);
        mpinImg2 = (ImageView) rootView.findViewById(R.id.imageMpin2);
        mpinImg3 = (ImageView) rootView.findViewById(R.id.imageMpin3);
        mpinImg4 = (ImageView) rootView.findViewById(R.id.imageMpin4);

        setMPINMsg = (TextView) rootView.findViewById(R.id.textSetMpin);
        setMPINMsg.setText(getResources().getString(R.string.LBL_CURRENT_MPIN));
        button1 = (Button) rootView.findViewById(R.id.button1);
        button2 = (Button) rootView.findViewById(R.id.button2);
        button3 = (Button) rootView.findViewById(R.id.button3);
        button4 = (Button) rootView.findViewById(R.id.button4);
        button5 = (Button) rootView.findViewById(R.id.button5);
        button6 = (Button) rootView.findViewById(R.id.button6);
        button7 = (Button) rootView.findViewById(R.id.button7);
        button8 = (Button) rootView.findViewById(R.id.button8);
        button9 = (Button) rootView.findViewById(R.id.button9);
        button0 = (Button) rootView.findViewById(R.id.button0);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button0.setOnClickListener(this);


        LL = (LinearLayout) rootView.findViewById(R.id.mpinSubmit);
        LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isCurrentPINEntered){
                    if (isNewPINMPINEntered) {
                        if (confirmUserMpin.length() < 4) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.MSG_CONFIRM_MPIN),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            if (confirmUserMpin.equals(userMpin)){
                                new verifyAndUpdateMPIN().execute();
                                //Service call
                            } else {
                                confirmUserMpin = "";
                                setMPINMsg.setText(getResources().getString(R.string.LBL_CONFIRM_MPIN));
                                mpinImageUpdate();
                                Toast.makeText(getActivity(), "Please re-enter valid MPIN.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        if (userMpin.length() < 4) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.MSG_ENTER_MPIN),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            isNewPINMPINEntered = true;
                            confirmUserMpin = "";
                            setMPINMsg.setText(getResources().getString(R.string.LBL_CONFIRM_MPIN));
                            mpinImageUpdate();
                        }
                    }
                } else {
                    if (currentMPIN.length() < 4) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.MSG_CURRENT_MPIN),
                                Toast.LENGTH_LONG).show();
                    } else {
                        new verifyAndUpdateMPIN().execute();
                    }
                }
            }
        });

        ClearBtnLayout = (ImageView) rootView.findViewById(R.id.clearMpinBtn);
        ClearBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( isCurrentPINEntered ) {
                    if ( isNewPINMPINEntered ) {
                        clearConfirmMPIN();
                    } else {
                        clearMPIN();
                    }
                } else {
                    clearCurrentMPIN();
                }
            }
        });
        setRetainInstance(true);
        return rootView;
    }

    void updateMPINText(String enteredText){
        if ( isCurrentPINEntered ) {
            if (isNewPINMPINEntered) {
                if (confirmUserMpin.length() < 4) {
                    confirmUserMpin = confirmUserMpin + enteredText;
                }
            } else {
                if (userMpin.length() < 4) {
                    userMpin = userMpin + enteredText;
                }
            }
        } else {
            if (currentMPIN.length() < 4) {
                currentMPIN = currentMPIN + enteredText;
            }
        }
        mpinImageUpdate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                updateMPINText("1");
                break;
            case R.id.button2:
                updateMPINText("2");
                break;
            case R.id.button3:
                updateMPINText("3");
                break;
            case R.id.button4:
                updateMPINText("4");
                break;
            case R.id.button5:
                updateMPINText("5");
                break;
            case R.id.button6:
                updateMPINText("6");
                break;
            case R.id.button7:
                updateMPINText("7");
                break;
            case R.id.button8:
                updateMPINText("8");
                break;
            case R.id.button9:
                updateMPINText("9");
                break;
            case R.id.button0:
                updateMPINText("0");
                break;
        }
    }

    void mpinImageUpdate() {
        int mpinLength;
        if ( isCurrentPINEntered ){
            if ( isNewPINMPINEntered ) {
                mpinLength = confirmUserMpin.length();
            } else {
                mpinLength = userMpin.length();
            }
        } else {
            mpinLength = currentMPIN.length();
        }
        switch ( mpinLength ) {
            case 0:
                mpinImg1.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg2.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg3.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg4.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                break;
            case 1:
                mpinImg1.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg2.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg3.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg4.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                break;
            case 2:
                mpinImg1.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg2.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg3.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                mpinImg4.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                break;
            case 3:
                mpinImg1.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg2.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg3.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg4.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_000);
                break;
            case 4:
                mpinImg1.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg2.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg3.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                mpinImg4.setImageResource(R.drawable.abc_btn_check_to_on_mtrl_015);
                break;
        }
    }

    void clearCurrentMPIN() {
        currentMPIN = "";
        setMPINMsg.setText(getResources().getString(R.string.LBL_CURRENT_MPIN));
        mpinImageUpdate();
    }

    void clearMPIN() {
        userMpin = "";
        setMPINMsg.setText(getResources().getString(R.string.LBL_SET_MPIN));
        mpinImageUpdate();
    }

    void clearConfirmMPIN() {
        confirmUserMpin = "";
        setMPINMsg.setText(getResources().getString(R.string.LBL_CONFIRM_MPIN));
        mpinImageUpdate();
    }

    private class verifyAndUpdateMPIN extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(getActivity());
            prgDialog.setMessage(getResources().getString(R.string.MSG_PLEASE_WAIT));
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String resp = verifyAndUpdateMPINService();
            return resp;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("") || s == null) {
                prgDialog.dismiss();
                Toast.makeText(getActivity(), getResources().getString(R.string.MSG_UNABLE_TO_REACH_SERVER) , Toast.LENGTH_LONG).show();
                return;
            } else {
                JSONObject mainObj = null;
                try {
                    mainObj = new JSONObject(s);
                    respParams = mainObj.getString("responseParameter");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("resstatus");
                    Log.d("result", result);
                    if (result != null) {
                        if (opStatus.equals("00")) {
                            if( serviceType == "VERIFYMPIN" ){
                                isCurrentPINEntered = true;
                                userMpin = "";
                                serviceType = "UPDATEMPIN";
                                setMPINMsg.setText(getResources().getString(R.string.LBL_SET_MPIN));
                                mpinImageUpdate();
                            } else {
                                Toast.makeText(getActivity(), "MPIN updated successfully!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            serviceType = "VERIFYMPIN";
                            currentMPIN = "";
                            isCurrentPINEntered = false;
                            setMPINMsg.setText(getResources().getString(R.string.LBL_CURRENT_MPIN));
                            mpinImageUpdate();
                            prgDialog.dismiss();
                            Toast.makeText(getActivity(), "Invalid MPIN", Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }
                    prgDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String verifyAndUpdateMPINService() {
        String resp="";
        try{

            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSSETPIN");
            json.put("entityId", "QRYS");

            jsonInner = new JSONObject();

            jsonInner.put("entityId", "QRYS");
            jsonInner.put("cbsType",cbsType);
            jsonInner.put("DEVICE_BUILD", "1.0");
            jsonInner.put("DeviceId", DeviceId);
            jsonInner.put("service_Type", serviceType);
            jsonInner.put("NEW_MPIN", confirmUserMpin);
            jsonInner.put("MPIN", currentMPIN);
            jsonInner.put("MobileNo", userMobileNo);
            jsonInner.put("TYPE_OF_DEVICE", "ANDROID");
           // jsonInner.put("customerId","3574926");
//            jsonInner.put("RRN","");
            json.put("map", jsonInner);

            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("resp "+resp);

        }catch(Exception e) {}

        return resp;
    }


}

package com.infra.qrys_wallet.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.ScanQRMain;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by sandeep.devhare on 15-09-2015.
 */
public class SendMoneyFragment extends Fragment implements View.OnClickListener {
    static final String TAG = "SENDMONEY";
    public static String CURRENT_DATE;
    TextView details, subHeader, availablebalcance;
    EditText productName, beneficiaryName, remark, ammount;
    String userAccountDetails, strproductName, strbeneficiaryName, strRemark, strammount, remetresMobileNO,beneficiaryMobileNo,userIFSCCode;
    Button cnfrm, rescanqr;
    String qrdetails, header;
    String[] temp;
    String delimiter = "~";
    ImageView imgback;
    ProgressDialog prgDialog;
    JSONObject json, jsonInner;
    String requestUrl, strLine, opStatus, result, customerId, reqPname, reqMono;
    MWRequest mwRequest;
    SharedPreference appPref;
    private Context myContext;
    private FragmentManager fragmentManager;
    private WeakReference<ScanQRTransaction> asyncTaskWeakRef;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId,fNAME, lNAME, custFullName,bankName;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashBoardWallet.fragmentID = 9;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View rootView = inflater.inflate(R.layout.sendmoneyfragment, container, false);
        appPref.getInstance().Initialize(getActivity());
        userAccountNo = appPref.getInstance().getString("SharedAccountNo");
        shortBankName = appPref.getInstance().getString("selectedBankDescription");
        cbsType = appPref.getInstance().getString("cBsType");
        mmidCode = appPref.getInstance().getString("SharedMMID");
        ifscCode = appPref.getInstance().getString("SharedIFSC");
        DeviceId = appPref.getInstance().getString("Device_Id");
        remetresMobileNO = appPref.getInstance().getString("user_mobile");
        fNAME = appPref.getInstance().getString("fName");
        lNAME = appPref.getInstance().getString("lName");
        custFullName = appPref.getInstance().getString("CustFull_Name");
        bankName =   appPref.getInstance().getString("selectedBankName");


        System.out.println("SharedPreference Details In SendMoney Fragment \n" + remetresMobileNO + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode+" "+fNAME+" "+lNAME+" \n "+custFullName+" "+bankName);



        String strtext = getArguments().getString("Details");
        //here I Am getting result from Scan QR
        Log.e("SendMoney 3 :", "Fragment Calls " + strtext);
        cnfrm = (Button) rootView.findViewById(R.id.confirm);
        rescanqr = (Button) rootView.findViewById(R.id.rescan);
        imgback = (ImageView) rootView.findViewById(R.id.imgback);

        productName = (EditText) rootView.findViewById(R.id.edtproduct);
        beneficiaryName = (EditText) rootView.findViewById(R.id.edtbeneficiaryname);
        remark = (EditText) rootView.findViewById(R.id.edtremark);
        ammount = (EditText) rootView.findViewById(R.id.edtamount);

        subHeader = (TextView) rootView.findViewById(R.id.tvqrscandetails);

        imgback.setOnClickListener(this);
        cnfrm.setOnClickListener(this);
        rescanqr.setOnClickListener(this);

        if (savedInstanceState == null) {
            String Result = getArguments().getString("Details");
            if (Result == null) {
                qrdetails = null;
            } else {

                /*ZvhVG3G2vy8C9gmADknGQs7knRKADNNTq/4KQWLSiwdmzPtJIju30fOaimDJWoTHHvNZsiuXFzIR0m0P1nWHETDMRbIus1P6aNffwm3NmGYocO9ywszvXAseT4zrvCt10jqitQOgwvc72MKUBW2bR9GdH58+YZnMWnvzDr1L3qQ=
                ~SAI
                ~Cookies
                ~1
                */
                qrdetails = getArguments().getString("Details");
                Log.e("SendMoney 4 :", "Fragment Calls " + qrdetails);
                int len1 = qrdetails.trim().split("~").length;
                System.out.println("Length is" + len1);
                int len2 = qrdetails.trim().split("~").length;
                System.out.println("" + len2);

                /*Apply encrpt/decrypt logic here*/


                if (qrdetails.trim().split("~").length == 4) {
                    Log.e("Test 0 :", " " + len2);
                    temp = qrdetails.split(delimiter);
                    Log.e("Test 1 :", " " + temp);
                    for (int i = 0; i < temp.length; i++)
                        System.out.println("elements are " + temp[i]);
                    /*328010200006972~UTIB0000328~SAI~9920246993~product~ammount*/
                    /*, , , , */
                    strammount = temp[3];
                    strproductName = temp[2];
                    strbeneficiaryName = temp[1];
                    userAccountDetails =temp[0];


                    System.out.println(" \n");
                    System.out.println(" ---------------------------------------\n");
                    System.out.println(" strammount " + strammount + "\n");
                    System.out.println(" strbeneficiaryName " + strbeneficiaryName + "\n");
                    System.out.println(" strproductName " + strproductName + "\n");
                    System.out.println(" ---------------------------------------");

                    productName.setText(strproductName);
                    beneficiaryName.setText(strbeneficiaryName);
                    ammount.setText(strammount);

                } else {
                    showDialog(SendMoneyFragment.this.getActivity(), "No Correct Code Found", "Scan QR code Again?", "Yes", "No").show();
                }
                Log.d(TAG, "here I got the QR code contents from previous class " + qrdetails);
            }
        } else {
            qrdetails = (String) savedInstanceState.getSerializable("Details");
            //  details.setText(qrdetails);
        }
        //--
        // on configuration changes (screen rotation) we want fragment member variables to preserved
        setRetainInstance(true);
        return rootView;
    }
/**/
/*public static  String decrypt(String text,String stSecretKey)throws Exception
{
    byte[] key = null ;
    byte [] iv = null;
    BASE64Decoder decoder = null;
    byte [] results = null;
    try
    {
        key = stSecretKey.getBytes("UTF-8");
        iv = "0000000000000000".getBytes();
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE,keySpec,ivSpec);
        decoder = new BASE64Decoder();
        results = cipher.doFinal(decoder.decodeBuffer(text));
    }
    catch(Exception e){
        e.printStackTrace();
        logger.info(text + "#####" + e.toString());
    }
    return new String(results,"UTF-8");
}*/


    //alert dialog for downloadDialog
    private AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    ScanQRMain qrScan = new ScanQRMain();
                    fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container, qrScan);
                    ft.commit();
                    // act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:

                    strammount = ammount.getText().toString();
                    strRemark = remark.getText().toString();
                if(strRemark.length()==0)
                {
                    Log.d("Please Enter remark ","");
                }
                else{
                    ScanQRTransaction asyncTask = new ScanQRTransaction(SendMoneyFragment.this);
                    asyncTaskWeakRef = new WeakReference<ScanQRTransaction>(asyncTask);
                    asyncTask.execute();
                }




                break;
            case R.id.rescan:
                ScanQRMain qrScan = new ScanQRMain();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft1 = fragmentManager.beginTransaction();
                ft1.replace(R.id.frame_container, qrScan);
               // ft1.addToBackStack("optional tag");
                ft1.commit();
                break;
            case R.id.imgback:
                Wallet callWallet = new Wallet();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.frame_container, callWallet);
                //ft.addToBackStack("fragBack");
                ft.commit();
                break;
        }
    }

    private boolean isAsyncTaskPendingOrRunning() {
        return this.asyncTaskWeakRef != null &&
                this.asyncTaskWeakRef.get() != null &&
                !this.asyncTaskWeakRef.get().getStatus().equals(AsyncTask.Status.FINISHED);
    }

    public class ScanQRTransaction extends AsyncTask<String, Void, String> {
        private WeakReference<SendMoneyFragment> fragmentWeakRef;

        private ScanQRTransaction(SendMoneyFragment fragment) {
            this.fragmentWeakRef = new WeakReference<SendMoneyFragment>(fragment);
        }

        @Override
        protected String doInBackground(String... params) {
            String resp = doTransaction();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(SendMoneyFragment.this.getActivity());
            // Set Progress Dialog Text
            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("resp postexecute =", s);
            if (s.equals("") || s == null) {
                prgDialog.dismiss();
                Toast.makeText(SendMoneyFragment.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                try {
                    Log.d("after calling async ", "afte asynctask5");
                    //sendJsondAta(s);
                    JSONObject mainObj = new JSONObject(s);
                    String respParams = mainObj.getString("responseParameter");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("Result");
                    Log.d("result", result);
                    Dialog();
                    //Toast.makeText(SendMoneyFragment.this.getActivity(), " " + result, Toast.LENGTH_LONG).show();
                    prgDialog.dismiss();
                } catch (Exception e) {
                    Log.d("adapter", e.getMessage());
                    prgDialog.dismiss();
                }
            }
        }

        private String doTransaction() {
            URL url = null;
            String resp = "";

            try {
                json = new JSONObject();
                json.put("ACTION", "");
                json.put("subActionId", "QRYSIMPSP2AFUNDTRANSFER");
                json.put("entityId", "QRYS");
                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("cbsType", cbsType);

                jsonInner.put("transferType","Q");
                jsonInner.put("MobileNo", remetresMobileNO);
                jsonInner.put("DeviceId", DeviceId);
                jsonInner.put("txn_amount", strammount);
                jsonInner.put("remarks", strRemark);
                jsonInner.put("benefName", strbeneficiaryName);
                jsonInner.put("Product",strproductName);
                jsonInner.put("QR_scan_Val",userAccountDetails);


               // jsonInner.put("beneficiaryMobileNo", strmobileno);
                json.put("map", jsonInner);
                System.out.println("json----------->" + json.toString());
                mwRequest = new MWRequest();
                resp = mwRequest.middleWareReq(json);
                System.out.println("response b4 return " + resp);
            }/* catch (MalformedURLException e) {
                e.printStackTrace();
            }*/ catch (JSONException e) {
                e.printStackTrace();
            }
            return resp;
        }
    }
    private void Dialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_paymentdone);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        TextView tvSelectedBank = (TextView) dialog.findViewById(R.id.tvbankselected);
        tvSelectedBank.setText(" " + result);
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
                productName.setText("");
                beneficiaryName.setText("");
                remark.setText("");
                ammount.setText("");
                Wallet homeFragment = new Wallet();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, homeFragment);
               // ft.addToBackStack("optional tag");
                ft.commit();

            }
        });
        dialog.show();
    }
}






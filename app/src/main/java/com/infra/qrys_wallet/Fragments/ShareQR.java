package com.infra.qrys_wallet.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShareQR extends Fragment implements View.OnClickListener {
    ImageView shareQR, shareimg;
    Button regenrate;
    EditText EdittextAmount;
    Bitmap bitmap;
    // ImageView back;
    ProgressDialog pDialog;
    ProgressDialog prgDialog;
    JSONObject json, jsonInner;
    String requestUrl, strLine, opStatus, result, customerId, reqPname, reqMono, imgPath;
    MWRequest mwRequest;
    SharedPreference appPref;
    String userAccountNo, Amount, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, fNAME, lNAME, custFullName, bankName, remetresMobileNO;
    private WeakReference<LoadQRCode> asyncTaskWeakRef;

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DashBoardWallet.fragmentID = 11;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Share QR");
        View rootView = inflater.inflate(R.layout.share_qr, container,
                false);
        appPref.getInstance().Initialize(ShareQR.this.getActivity());
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
        bankName = appPref.getInstance().getString("selectedBankName");
        System.out.println("SharedPreference Details In SendMoney Fragment " + remetresMobileNO + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode + " " + fNAME + " " + lNAME + " \n " + custFullName + " " + bankName);
        shareQR = (ImageView) rootView.findViewById(R.id.img_qr);
        regenrate = (Button) rootView.findViewById(R.id.txt_regenerateQR);
        EdittextAmount = (EditText) rootView.findViewById(R.id.edtamount);
        regenrate.setOnClickListener(this);
        LoadQRCode asyncTask = new LoadQRCode(ShareQR.this);
        asyncTaskWeakRef = new WeakReference<LoadQRCode>(asyncTask);
        asyncTask.execute();
        return rootView;
    }

    private boolean isAsyncTaskPendingOrRunning() {
        return this.asyncTaskWeakRef != null &&
                this.asyncTaskWeakRef.get() != null &&
                !this.asyncTaskWeakRef.get().getStatus().equals(AsyncTask.Status.FINISHED);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_regenerateQR:
                Amount = EdittextAmount.getText().toString();
                new GenerateQr().execute();
            default:
                return;
        }
    }

    private String QrGeneration() {
        URL url = null;
        String resp = "";
        try {
            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSGENQRIMAGE");
            json.put("entityId", "QRYS");
            jsonInner = new JSONObject();
            jsonInner.put("entityId", "QRYS");
            jsonInner.put("cbsType", cbsType);
            jsonInner.put("MobileNo", remetresMobileNO);
            jsonInner.put("txn_amount", Amount);
            jsonInner.put("accountno", userAccountNo);
            json.put("map", jsonInner);
            System.out.println("json----------->" + json.toString());
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("response b4 return for generateQr " + resp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public class LoadQRCode extends AsyncTask<String, Void, String> {
        private WeakReference<ShareQR> fragmentWeakRef;

        private LoadQRCode(ShareQR shareqr) {
            this.fragmentWeakRef = new WeakReference<ShareQR>(shareqr);
        }

        @Override
        protected String doInBackground(String... params) {
            String resp = doTransaction();
            return resp;
        }

        private String doTransaction() {
            URL url = null;
            String resp = "";
            try {
                json = new JSONObject();
                json.put("ACTION", "");
                json.put("subActionId", "QRYSSHAREIMAGE");
                json.put("entityId", "QRYS");
                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("cbsType", cbsType);
                jsonInner.put("MobileNo", remetresMobileNO);
                jsonInner.put("DeviceId", DeviceId);
                jsonInner.put("accountno", userAccountNo);
                json.put("map", jsonInner);
                System.out.println("json----------->" + json.toString());
                mwRequest = new MWRequest();
                resp = mwRequest.middleWareReq(json);
                System.out.println("response b4 return " + resp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resp;
        }        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(ShareQR.this.getActivity());
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
                Toast.makeText(ShareQR.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                try {
                    Log.d("after calling async ", "afte asynctask5");
                    JSONObject mainObj = new JSONObject(s);
                    String respParams = mainObj.getString("responseParameter");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("resstatus");
                    Log.d("resstatus", result);
                    imgPath = respObj.getString("imagePath");
                    Log.d("ImgPath ", imgPath);
                    bitmap = getBitmapFromURL(imgPath);
                    if (bitmap != null) {
                        shareQR.setImageBitmap(bitmap);
                        shareimg.setEnabled(true);
                        pDialog.dismiss();
                    } else {
                        pDialog.dismiss();
                        Toast.makeText(ShareQR.this.getActivity(), "Image Does Not exist", Toast.LENGTH_SHORT).show();
                    }
                    prgDialog.dismiss();
                } catch (Exception e) {
                    Log.d("adapter", e.getMessage());
                    prgDialog.dismiss();
                }
            }
        }


    }

    /*Regenerate QR code Service*/
    private class GenerateQr extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String resp = QrGeneration();
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("resp postexecute =", s);
            if (s.equals("") || s == null) {
                prgDialog.dismiss();
                Toast.makeText(ShareQR.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                try {
                    Log.d("after calling async ", "afte asynctask5");
                    JSONObject mainObj = new JSONObject(s);
                    String respParams = mainObj.getString("responseParameter");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("resstatus");
                    Log.d("resstatus", result);
                    imgPath = respObj.getString("imagePath");
                    Log.d("ImgPath ", imgPath);
                    bitmap = getBitmapFromURL(imgPath);
                    if (bitmap != null) {
                        shareQR.setImageBitmap(bitmap);
                        shareimg.setEnabled(true);
                        pDialog.dismiss();
                    } else {
                        pDialog.dismiss();
                        Toast.makeText(ShareQR.this.getActivity(), "Image Does Not exist", Toast.LENGTH_SHORT).show();
                    }
                    prgDialog.dismiss();
                } catch (Exception e) {
                    Log.d("adapter", e.getMessage());
                    prgDialog.dismiss();
                }
            }
        }
    }
}

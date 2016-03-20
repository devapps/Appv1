package com.infra.qrys_wallet.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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

/**
 * Created by sandeep.devhare on 16-11-2015.
 */
public class mWalletOffersDetail extends Fragment implements View.OnClickListener {

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

    String DEALDESCRIPTION, COUPONCODE, DEALSTATUS, DEALDAYS, DEALID, DESCRIPTION, DEALNAME, imagePath, txn_amount;
    String cbsType, DeviceId, remetresMobileNO;

    ImageView imgDealImage;
    TextView textDealTitle, textDealDesc, textDealPrice;
    Button btnPay;

    ProgressDialog prgDialog;
    JSONObject json, jsonInner;
    String opStatus, result;
    MWRequest mwRequest;
    SharedPreference appPref;
    private FragmentManager fragmentManager;

    private WeakReference<makePaymentForOffers> asyncTaskWeakRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null && args.containsKey(DealID)) {
            DEALDESCRIPTION = args.getString(DealDescription);
            DEALNAME = args.getString(DealName);
            txn_amount = args.getString(DealAmount);
            imagePath = args.getString(DealImage);
            DESCRIPTION = args.getString(DealDescStatus);
            COUPONCODE = args.getString(DealCouponCode);
            DEALSTATUS = args.getString(DealStatus);
            DEALDAYS = args.getString(DealDays);
            DEALID = args.getString(DealID);


        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashBoardWallet.fragmentID = 15;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Offers");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View rootView = inflater.inflate(R.layout.mwallet_offers_detail, container, false);
        imgDealImage = (ImageView) rootView.findViewById(R.id.imgDealImage);
        textDealDesc = (TextView) rootView.findViewById(R.id.textDealDescription);
        textDealPrice = (TextView) rootView.findViewById(R.id.textDealPrice);
        textDealTitle = (TextView) rootView.findViewById(R.id.textDealTitle);
        btnPay = (Button) rootView.findViewById(R.id.btnPay);

        textDealDesc.setText(DEALDESCRIPTION);
        textDealPrice.setText(txn_amount);
        textDealTitle.setText(DEALNAME);

        Bitmap temp = getBitmapFromURL(imagePath);
        if (temp == null) {
            imgDealImage.setImageResource(R.drawable.dealplaceholder);
        } else {
            imgDealImage.setImageBitmap(temp);
        }

        if (DEALSTATUS.equals("N")) {
            btnPay.setText(COUPONCODE);
        } else {
            btnPay.setText("Pay");
            btnPay.setOnClickListener(this);
        }

        cbsType = appPref.getInstance().getString("cBsType");
        DeviceId = appPref.getInstance().getString("Device_Id");
        remetresMobileNO = appPref.getInstance().getString("user_mobile");

        return rootView;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPay:
                makePaymentForOffers asyncTask = new makePaymentForOffers(mWalletOffersDetail.this);
                asyncTaskWeakRef = new WeakReference<makePaymentForOffers>(asyncTask);
                asyncTask.execute();
                break;
            default:
                return;
        }
    }

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

    public class makePaymentForOffers extends AsyncTask<String, Void, String> {
        private WeakReference<mWalletOffersDetail> fragmentWeakRef;

        private makePaymentForOffers(mWalletOffersDetail fragment) {
            this.fragmentWeakRef = new WeakReference<mWalletOffersDetail>(fragment);
        }

        @Override
        protected String doInBackground(String... params) {
            String resp = doTransaction();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(mWalletOffersDetail.this.getActivity());
            prgDialog.setMessage("Please wait...");
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("resp postexecute =", s);
            if (s.equals("") || s == null) {
                prgDialog.dismiss();
                Toast.makeText(mWalletOffersDetail.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                try {
                    //sendJsondAta(s);
                    JSONObject mainObj = new JSONObject(s);
                    String respParams = mainObj.getString("responseParameter");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("Result");
                    Log.d("result", result);
                    Dialog();
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
                json.put("subActionId", "MERCHANTFUNDTRANSFER");
                json.put("entityId", "QRYS");
                json.put("customerId", "");
                json.put("sessionId", "");
                json.put("cbsType", cbsType);
                json.put("deviceId", "");

                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("cbsType", cbsType);
                jsonInner.put("MobileNo", remetresMobileNO);
                jsonInner.put("DeviceId", DeviceId);
                jsonInner.put("txn_amount", txn_amount);
                jsonInner.put("DEALID", DEALID);

                json.put("map", jsonInner);

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
                Wallet homeFragment = new Wallet();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, homeFragment);
                ft.addToBackStack("optional tag");
                ft.commit();

            }
        });
        dialog.show();
    }


}

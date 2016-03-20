package com.infra.qrys_wallet.Fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
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
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by sandeep.devhare on 16-11-2015.
 */
public class ContactsSendMoney extends Fragment implements View.OnClickListener{
    ImageView imgContactImg;
    EditText remark, amount;
    TextView userName, txtuserMobileNo;
    String Name, beneficiaryMobNo,stramount,strremark;
    public static Bitmap b, output;
    Button cancel, send;
    FragmentManager fragmentManager;
    private FragmentTransaction ft;
    ProgressDialog prgDialog;
    private WeakReference<SendMoneyTransaction> asyncTaskWeakRef;
    String requestUrl,opStatus, result,resultStatus, customerId, reqPname, reqMono;
    MWRequest mwRequest;
    JSONObject json, jsonInner;
    SharedPreference appPref;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo,fNAME, lNAME, custFullName,bankName;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null && args.containsKey("Name") && args.containsKey("phoneNo") ) {
            Name = args.getString("Name");
            beneficiaryMobNo = args.getString("phoneNo");

            System.out.println("Name " + Name + " " + " beneficiaryMobNo. " + beneficiaryMobNo);

        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View rootView = inflater.inflate(R.layout.contacts_send_money, container, false);
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
        System.out.println("SharedPreference Details In Contacts Fragment \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode+" "+fNAME+" "+lNAME+" \n "+custFullName+" "+bankName);

        imgContactImg = (ImageView) rootView.findViewById(R.id.imgUser);
        amount = (EditText) rootView.findViewById(R.id.textAmount);
        remark = (EditText) rootView.findViewById(R.id.edtRemark);
        userName = (TextView) rootView.findViewById(R.id.textUserName);
        txtuserMobileNo = (TextView) rootView.findViewById(R.id.textUserMobileNo);
        cancel = (Button) rootView.findViewById(R.id.buttonCancel);
        send = (Button) rootView.findViewById(R.id.buttonSend);
        cancel.setOnClickListener(this);
        send.setOnClickListener(this);

        userName.setText(Name);
        txtuserMobileNo.setText(beneficiaryMobNo);
        if (b != null) {
            b = getCircularBitmap(b);
            imgContactImg.setImageBitmap(b);
        } else {
            imgContactImg.setImageResource(R.drawable.profile_pic);
        }
        return rootView;

    }


    public static Bitmap getCircularBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() > bitmap.getHeight()) {
            // output.recycle();
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            // output.recycle();
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        float r = 0;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
        } else {
            r = bitmap.getWidth() / 2;
        }
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.buttonSend:

                stramount = amount.getText().toString();
                strremark = remark.getText().toString();
                SendMoneyTransaction asyncTask = new SendMoneyTransaction(ContactsSendMoney.this);
                asyncTaskWeakRef = new WeakReference<SendMoneyTransaction>(asyncTask);
                asyncTask.execute();

                break;
            case R.id.buttonCancel:
                Contacts phoneBook = new Contacts();
                fragmentManager = getFragmentManager();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, phoneBook);
                ft.commit();
                break;
            default:
                return;


        }

    }

    private boolean isAsyncTaskPendingOrRunning() {
        return this.asyncTaskWeakRef != null &&
                this.asyncTaskWeakRef.get() != null &&
                !this.asyncTaskWeakRef.get().getStatus().equals(AsyncTask.Status.FINISHED);
    }

    public class SendMoneyTransaction extends AsyncTask<String, Void, String> {
        private WeakReference<ContactsSendMoney> fragmentWeakRef;

        private SendMoneyTransaction(ContactsSendMoney fragment) {
            this.fragmentWeakRef = new WeakReference<ContactsSendMoney>(fragment);
        }
        @Override
        protected String doInBackground(String... params) {
            String resp = doTransaction();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(ContactsSendMoney.this.getActivity());
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
                Toast.makeText(ContactsSendMoney.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                try {
                    JSONObject mainObj = new JSONObject(s);
                    String respParams = mainObj.getString("responseParameter");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    resultStatus = respObj.getString("resstatus");
                    Log.d("result Status ", resultStatus);
                    result = respObj.getString("Result");
                    Log.d("result", result);
                    Toast.makeText(ContactsSendMoney.this.getActivity(), " " + result, Toast.LENGTH_LONG).show();
                    /* Send Money Reciept Fragment call*/

                    Bundle b = new Bundle();
                  //  b.putString("beneficiaryName", Name);
                    if(resultStatus.equals("00"))
                    {
                        b.putString("Status","Payment Successful");
                    }else
                    {
                        b.putString("Status","Payment Failed");
                    }
                    b.putString("amount",stramount);
                   // b.putString("txnID", "1234567890");
                    b.putString("statusMsg",result);

                    FragmentManager fm2 = getFragmentManager();
                    FragmentTransaction ft2 = fm2.beginTransaction();
                    ContactsSendMoneyReceipt sendMoneyReceipt = new ContactsSendMoneyReceipt();
                    ft2.replace(R.id.frame_container, sendMoneyReceipt);
                    ft2.addToBackStack("optional tag");
                    sendMoneyReceipt.setArguments(b);
                    ft2.commit();

                   // Dialog();

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
                json.put("subActionId", "QRYSSENDMONEY");
                json.put("entityId", "QRYS");
                jsonInner = new JSONObject();
                //jsonInner.put("DeviceId", DeviceId);
                jsonInner.put("accountno",userAccountNo);
                jsonInner.put("transferType","C");
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("remarks", strremark);
                jsonInner.put("beneficiaryMobileNo", beneficiaryMobNo);
                jsonInner.put("cbsType", cbsType);
                jsonInner.put("txn_amount",stramount);
                jsonInner.put("DEALOFFER","D");
               // jsonInner.put("QrysSelBank",bankName);
                jsonInner.put("MobileNo",userMobileNo);
                jsonInner.put("city","Mumbai");
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
               // productName.setText("");
               // beneficiaryName.setText("");
                remark.setText("");
                amount.setText("");
                Intent i = new Intent(getActivity(), DashBoardWallet.class);
                startActivity(i);

            }
        });
        dialog.show();
    }
    }

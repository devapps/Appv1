package com.infra.qrys_wallet;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.Adapters.NotificationAdapter;
import com.infra.qrys_wallet.Fragments.ContactsSendMoneyReceipt;
import com.infra.qrys_wallet.Models.PendingNotification;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

public class Notification extends Fragment implements View.OnClickListener {
    private static String TAG = "Notification";
    ImageView back;
    ArrayList<PendingNotification> selectNotifications;
    ListView listView;
    NotificationAdapter adapter;
    Bitmap bit_thumb = null;
    SwipeRefreshLayout swipeView;
    String msgs;
    ProgressDialog prgDialog;
    JSONObject json, jsonInner;
    String requestUrl, strLine,resultStatus, opStatus, result, customerId, reqPname, reqMono,msg,respParams,reqstID,reMark,bankNames,beniMobileNos,txnAmmount,custName;
    MWRequest mwRequest;
    SharedPreference appPref;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId,fNAME, lNAME, custFullName,bankName,remetresMobileNO;
    private WeakReference<SendMoneyTransaction> asyncTaskWeakRef;
    FragmentManager fragmentManager;
    private FragmentTransaction ft;
    public void Notification() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Notification");
        DashBoardWallet.fragmentID = 13;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View rootView = inflater.inflate(R.layout.notification, container, false);
        LoadNotification loadNotification = new LoadNotification();
        loadNotification.execute();
        appPref.getInstance().Initialize(Notification.this.getActivity());
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


        selectNotifications = new ArrayList<PendingNotification>();
        adapter = new NotificationAdapter(selectNotifications, getActivity());
        swipeView = (SwipeRefreshLayout)rootView. findViewById(R.id.swipe);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);

                LoadNotification loadNotification = new LoadNotification();
                loadNotification.execute();

            }
        });
        listView = (ListView) rootView. findViewById(R.id.notificationlist);
      //  back = (ImageView)rootView.  findViewById(R.id.imgback);
      //  back.setOnClickListener(this);
    return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.imgback:
                Intent callDashBoard = new Intent(Notification.this.getActivity(), DashBoardWallet.class);
                startActivity(callDashBoard);
                //overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
               // finish();
                break;*/
            default:
                return;
        }
    }

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /*Service call for Load Notification Msgs*/
    public class LoadNotification extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String resp = doLoadNotifications();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(Notification.this.getActivity());
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
                Toast.makeText(Notification.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                try {

                    JSONObject mainObj = new JSONObject(s);
                    respParams = mainObj.getString("set");
                    Log.d(TAG, " respParams " + respParams);
                    JSONObject respObj = new JSONObject(respParams);
                    String firstNode = respObj.getString("setname");
                    String secondNode = respObj.getString("records");
                    Log.d(TAG, " setName " + firstNode);
                    Log.d(TAG, " records " + secondNode);

                    String resp = mainObj.getString("responseParameter");
                    JSONObject respObjforstatus = new JSONObject(resp);
                    opStatus = respObjforstatus.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObjforstatus.getString("resstatus");
                    Log.d("result", result);

                    String totallist = respObjforstatus.getString("totalListCount");
                    Log.d("totallist", totallist);
                    int l = Integer.parseInt(totallist);
                    Log.d("length", " " + l);


                    /*Read All Bank Names and */
                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray = respObj.optJSONArray("records");
                    System.out.println(" jsonArray.length() " + jsonArray.length());
                    //Iterate the jsonArray and print the info of JSONObjects
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        reqstID = jsonObject.getString("requestId");
                        reMark = jsonObject.getString("remarks");
                        bankNames = jsonObject.getString("bankName");
                        beniMobileNos = jsonObject.getString("beneficiaryMobileNo");
                        txnAmmount = jsonObject.getString("txn_amount");
                        custName = jsonObject.getString("customerName");
                        //bit_thumb = jsonObject.getString("");
                        msg = jsonObject.getString("DESCRIPTION");
                        System.out.println("Retrive Details "+reqstID+" "+reMark+" "+bankNames+" "+beniMobileNos+" "+txnAmmount+" "+msg+" "+custName);
                        PendingNotification selectUser = new PendingNotification();
                        //selectUser.setThumb(bit_thumb);
                        selectUser.setName(msg);
                        selectNotifications.clear();
                        //   listView.getAdapter().notifyAll();
                        adapter.notifyDataSetChanged();
                        selectNotifications.add(selectUser);
                    }


                    if (opStatus != null) {
                        if (opStatus.equals("00")) {

                            if (selectNotifications != null) {

                                listView.setAdapter(adapter);
                                // Select item on listclick
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        Log.e("search", "here---------------- listener");
                                        PendingNotification data = selectNotifications.get(i);
                                        Log.e("Click Result ", "Name " + data.getName() + "  No." + data.getThumb());
                                        Dialog();
                                       // Toast.makeText(Notification.this, "Message will be sent to " + data.getName(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                listView.setFastScrollEnabled(true);
                                swipeView.setRefreshing(false);

                            } else {
                                Toast.makeText(Notification.this.getActivity(), "No pending requests.", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            prgDialog.dismiss();
                            Toast.makeText(Notification.this.getActivity(), "User is Not Registered.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    prgDialog.dismiss();
                } catch (Exception e) {
                    Log.d("adapter", e.getMessage());
                    prgDialog.dismiss();
                }
            }
        }

        private String doLoadNotifications() {
            URL url = null;
            String resp = "";
            try {
                json = new JSONObject();
                json.put("ACTION", "");
                json.put("subActionId", "QRYSPENDREQ");
                json.put("entityId", "QRYS");
                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("cbsType",cbsType );
                jsonInner.put("MobileNo",remetresMobileNO);
                jsonInner.put("DeviceId",DeviceId);

                json.put("map", jsonInner);
                System.out.println("json----------->" + json.toString());
                mwRequest = new MWRequest();
                resp = mwRequest.middleWareReq(json);
            }/* catch (MalformedURLException e) {
                e.printStackTrace();
            }*/ catch (JSONException e) {
                e.printStackTrace();
            }
            return resp;
        }
    }
    private void Dialog() {
        Button bproceed;
        final Dialog dialog = new Dialog(Notification.this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_notification);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        TextView tvSelectedBank = (TextView) dialog.findViewById(R.id.tvbankselected);
        tvSelectedBank.setText("You really want to send ₹" +txnAmmount +" to "+custName);
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
                /*service call for money send*/
                SendMoneyTransaction asyncTask = new SendMoneyTransaction(Notification.this);
                asyncTaskWeakRef = new WeakReference<SendMoneyTransaction>(asyncTask);
                asyncTask.execute();

            }
        });
        dialog.show();
    }
    private boolean isAsyncTaskPendingOrRunning() {
        return this.asyncTaskWeakRef != null &&
                this.asyncTaskWeakRef.get() != null &&
                !this.asyncTaskWeakRef.get().getStatus().equals(AsyncTask.Status.FINISHED);
    }

    public class SendMoneyTransaction extends AsyncTask<String, Void, String> {
        private WeakReference<Notification> notifWeakpref;

        private SendMoneyTransaction(Notification not) {
            this.notifWeakpref = new WeakReference<Notification>(not);
        }



        @Override
        protected String doInBackground(String... params) {
            String resp = doTransaction();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(Notification.this.getActivity());
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
                Toast.makeText(Notification.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(Notification.this.getActivity(), " " + result, Toast.LENGTH_LONG).show();
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
                    b.putString("amount",txnAmmount);
                    // b.putString("txnID", "1234567890");
                    b.putString("statusMsg", result);

                   fragmentManager = getFragmentManager();
                    FragmentTransaction ft2 = fragmentManager.beginTransaction();
                    ContactsSendMoneyReceipt sendMoneyReceipt = new ContactsSendMoneyReceipt();
                    ft2.replace(R.id.frame_container, sendMoneyReceipt);
                    ft2.addToBackStack("optional tag");
                    sendMoneyReceipt.setArguments(b);
                    ft2.commit();


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
                jsonInner.put("accountno",userAccountNo);
                jsonInner.put("transferType","P");
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("remarks", reMark);
                jsonInner.put("beneficiaryMobileNo", beniMobileNos);
                jsonInner.put("cbsType", cbsType);
                jsonInner.put("txn_amount",txnAmmount);
                jsonInner.put("requestId",reqstID);
                jsonInner.put("DEALOFFER","D");
                // jsonInner.put("QrysSelBank",bankName);
                jsonInner.put("MobileNo",remetresMobileNO);
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
}

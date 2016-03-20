package com.infra.qrys_wallet.Fragments;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.ScanQRMain;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
/**
 * Created by sandeep.devhare on 20-10-2015.
 */
public class DashBoardFragment extends Fragment implements View.OnClickListener {
    ImageView shareQR, scanQR, scanNFC,showMessage, requestMoney,sendContacts,receContacts;
    int height;
    LinearLayout topLayout, bottomLayout;
    RelativeLayout topParent, bottomParent;
    TextView BalanceEnquiry;
    boolean topVisible,bottomVisible,isTop;

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    int xValueTop,xValueBottom;

    ProgressDialog prgDialog;
    String AvailableBalance;
    JSONObject json, jsonInner;
    MWRequest mwRequest;
    SharedPreference appPref;

    JSONObject mainObj;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo,fNAME, lNAME, custFullName,bankName;
    ObjectAnimator TopAnimLeft,TopAnimRight,BottomAnimLeft,BottomAnimRight;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    Animation slideLeft,slideRight;

    float amountToBeMove = 0.95f;

    public DashBoardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View rootView = inflater.inflate(R.layout.dashboard, container, false);

        appPref.getInstance().Initialize(DashBoardFragment.this.getActivity().getApplicationContext());
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

        System.out.println("SharedPreference Details In DashBoard Fragment \n");
        System.out.println(userMobileNo + " " + DeviceId + " " + shortBankName + " \n");
        System.out.println(cbsType + " " + userAccountNo + " " + mmidCode + "\n ");
        System.out.println(ifscCode+" "+fNAME+" "+lNAME+" \n ");
        System.out.println(custFullName+" "+bankName);

        //-----------------
        shareQR = (ImageView) rootView.findViewById(R.id.imgReceShareQR);
        scanQR = (ImageView) rootView.findViewById(R.id.imgSendScanQR);
        scanNFC = (ImageView) rootView.findViewById(R.id.imgSendNFC);
        sendContacts = (ImageView) rootView.findViewById(R.id.imgSendContact);
        receContacts = (ImageView) rootView.findViewById(R.id.imgReceContact);

        BalanceEnquiry=(TextView)rootView.findViewById(R.id.BalanceEnquiry);

        BalanceEnquiry.setText("Linked with "+bankName+" Account >");
        BalanceEnquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("clicked balance enquiry");
                new Balanceenquiry().execute();
            }
        });

        //showMessage = (ImageView) rootView.findViewById(R.id.imgmessage);
        //requestMoney = (ImageView) rootView.findViewById(R.id.imgreqstmoney);
        //requestMoney.setOnClickListener(this);
        //  showMessage.setOnClickListener(this);
        shareQR.setOnClickListener(this);
        scanQR.setOnClickListener(this);
        scanNFC.setOnClickListener(this);
        sendContacts.setOnClickListener(this);
        receContacts.setOnClickListener(this);

        topVisible = false;
        bottomVisible = false;
        isTop = false;

        DisplayMetrics displayMetrics = rootView.getResources().getDisplayMetrics();
        float mDisplayWidth = displayMetrics.widthPixels;
        mDisplayWidth = (float) (mDisplayWidth * 0.88);

        topLayout = (LinearLayout) rootView.findViewById(R.id.topSwipeLayout);
        bottomLayout = (LinearLayout) rootView.findViewById(R.id.bottomSwipeLayout);

        topParent = (RelativeLayout) rootView.findViewById(R.id.topParent);
        bottomParent = (RelativeLayout) rootView.findViewById(R.id.bottomParent);

        TopAnimLeft = ObjectAnimator.ofFloat(topLayout, "translationX", 0f, mDisplayWidth);
        TopAnimLeft.setDuration(500);

        TopAnimRight = ObjectAnimator.ofFloat(topLayout, "translationX", mDisplayWidth, 0f);
        TopAnimRight.setDuration(500);

        BottomAnimLeft = ObjectAnimator.ofFloat(bottomLayout, "translationX", 0f, -mDisplayWidth);
        BottomAnimLeft.setDuration(500);

        BottomAnimRight = ObjectAnimator.ofFloat(bottomLayout, "translationX", -mDisplayWidth, 0f);
        BottomAnimRight.setDuration(500);

        xValueTop = container.getWidth() - topLayout.getWidth();
        xValueBottom = container.getWidth() - bottomLayout.getWidth();

        gestureDetector = new GestureDetector(new MyGestureDetector());
        topParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                isTop = true;
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        bottomParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                isTop = false;
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        topLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                isTop = true;
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        bottomLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                isTop = false;
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        setRetainInstance(true);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgSendScanQR:
                /*this is just for testing the UI of SendMoney Screen, plz see below commented code for actual*/
                FragmentManager fm2 = getFragmentManager();
                FragmentTransaction ft2 = fm2.beginTransaction();
                ScanQRMain qrMainScan = new ScanQRMain();
                ft2.replace(R.id.frame_container, qrMainScan);
             //   ft2.addToBackStack("optional tag");
                ft2.commit();
                break;
            case R.id.imgSendNFC:

                NFCsplashScreen nfcTag = new NFCsplashScreen();
                fragmentManager = getFragmentManager();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, nfcTag);
                ft.commit();
                break;
            case R.id.imgSendContact:
                Contacts phoneBook = new Contacts();
                fragmentManager = getFragmentManager();
              //  getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, phoneBook);
             //   ft.addToBackStack("fragback");
                ft.commit();
                break;
            case R.id.imgReceContact:
                ContactsForRequestMoney phoneBookRecv = new ContactsForRequestMoney();
                fragmentManager = getFragmentManager();
             //   getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, phoneBookRecv);
              //  ft.addToBackStack("fragback");
                ft.commit();
                break;
            case R.id.imgReceShareQR:
                Intent shareQrCall = new Intent(getActivity(), ShareQR.class);
                getActivity().startActivity(shareQrCall);
                getActivity().finish();
                break;
        }
    }


    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    System.out.println("LEFT Swipe");
                    if(isTop){
                        if (topVisible){
                            hideTop();
//                            topLayout.startAnimation(slideRight);
                            topVisible = false;
                        }
                    } else {
                        if (bottomVisible){

                        } else {
                            showBottom();//bottomLayout.startAnimation(slideRight);
                            bottomVisible = true;
                            if (topVisible){
                                hideTop();//topLayout.startAnimation(slideLeft);
                                topVisible = false;
                            }
                        }
                    }

                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    System.out.println("Right Swipe");
                    if( isTop ){
                        if ( topVisible ){
                            //do nothing
                        } else {
                            showTop();
//                            topLayout.startAnimation(slideLeft);
                            topVisible = true;
                            if (bottomVisible){
                                hideBottom();
//                              bottomLayout.startAnimation(slideRight);
                                bottomVisible = false;
                            }
                        }
                    } else {
                        if (bottomVisible){
                            hideBottom();
//                            bottomLayout.startAnimation(slideRight);
                            bottomVisible = false;
                        }
                    }

                    System.out.println("IS TOP : "+isTop+"---TOP VISIBLE : "+topVisible+"---BOTTOM VISIBLE : "+bottomVisible);
//                    bottomLayout.startAnimation(slideRight);
//                    topLayout.startAnimation(slideRight);
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }
    }

    public void showTop(){
        TopAnimLeft.start();
    }

    public void hideTop(){
        TopAnimRight.start();
    }

    public void showBottom() {
        BottomAnimLeft.start();
    }

    public void hideBottom() {
        BottomAnimRight.start();
    }

    private class Balanceenquiry extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            String resp = CheckBalanceEnquiry();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(getActivity());
            // Set Progress Dialog Text
           // Log.d(TAG, "Test 0");
            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            System.out.println("Response balanve "+s);

                try {
                    mainObj = new JSONObject(s);
                    JSONObject  respParams = mainObj.getJSONObject("responseParameter");
                    AvailableBalance = respParams.getString("avail_bal");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            prgDialog.dismiss();
            Dialog();
        }
    }

    private void Dialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.balanceenquiry);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        TextView BalanceEnquiry = (TextView) dialog.findViewById(R.id.BalanceEnquiryAmount);
        BalanceEnquiry.setText(AvailableBalance);
        TextView tvSelectedBank = (TextView) dialog.findViewById(R.id.BalanceEnquiryBankName);
        tvSelectedBank.setText(bankName);
        dialog.show();
    }

    private String CheckBalanceEnquiry() {
        URL url = null;
        String resp = "";


        try{

            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "QRYSGETBAL");
            json.put("entityId", "QRYS");
            jsonInner = new JSONObject();
            jsonInner.put("entityId", "QRYS");
            jsonInner.put("cbsType", cbsType);


            jsonInner.put("MobileNo", userMobileNo);
            jsonInner.put("DeviceId", DeviceId);
            jsonInner.put("accountno", userAccountNo);
            jsonInner.put("QrysSelBank", shortBankName);



            // jsonInner.put("beneficiaryMobileNo", strmobileno);
            json.put("map", jsonInner);
            System.out.println("json----------->" + json.toString());
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("response b4 return for BalanceEnquiry" + resp);

        }catch(Exception e){


        }

        return resp;
    }
}

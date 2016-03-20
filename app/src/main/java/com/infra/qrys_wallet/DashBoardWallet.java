package com.infra.qrys_wallet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.infra.qrys_wallet.Adapters.DashboardScreen_Adapter;
import com.infra.qrys_wallet.Fragments.Profile;
import com.infra.qrys_wallet.Fragments.SetPaymentAddressFragment;
import com.infra.qrys_wallet.Fragments_UPI.ChequeServicesDashBoard;
import com.infra.qrys_wallet.Fragments_UPI.GetVirtualAddresslist;
import com.infra.qrys_wallet.Fragments_UPI.Insta_Pay;
import com.infra.qrys_wallet.Fragments_UPI.MandateForm_UPI;
import com.infra.qrys_wallet.Fragments_UPI.SendMoney_UPI;
import com.infra.qrys_wallet.Fragments_UPI.Wallet_UPI;
import com.infra.qrys_wallet.Utils.Constants;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.List;

/**
 * Created by sandeep.devhare on 16-11-2015.
 */
public class DashBoardWallet extends AppCompatActivity {
    public static int fragmentID;
    public RecyclerView.Adapter mAdapter;
   // String screenNames[] = {"Profile", "Home", "Transaction History", "Change MPIN", "Invite Friends", "Manage Accounts", "Logout"};
    String TITLES[] = {"Home","Send Money","Payment Address","Mandate Form", "Logout"};
    int ICONS[] = {R.drawable.ic_home, R.drawable.ic_history, R.drawable.ic_change_mpin, R.drawable.ic_invite_frnds,0};
    String fNAME, lNAME, profile_Pic, custFullName;
    Bitmap Profile_Pic;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;
    ActionBar mActionBar;
    boolean doubleBackToExitPressedOnce = false;
    //Service Variables
    MWRequest mwRequest;
    String opStatus, result, respParams;
    ProgressDialog prgDialog;
    SharedPreference appPref;
    JSONObject json, jsonInner;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo;
    private Toolbar toolbar;
    private Context mContext;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    private String TAG = "LOGOUT";
    String host;
    Uri deepLinkingIntent;
   // private TransactionRequest request = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_screen_wallet);




try{

     deepLinkingIntent= getIntent().getData();


    if(deepLinkingIntent!=null){

        String scheme = deepLinkingIntent.getScheme(); // "http"
        host = deepLinkingIntent.getHost(); // "twitter.com"

        List<String> pa = deepLinkingIntent.getQueryParameters("pa");
        String PA = pa.get(0); // "status"


        List<String> pn = deepLinkingIntent.getQueryParameters("pn");
        String PN = pn.get(0);

        List<String> mc = deepLinkingIntent.getQueryParameters("mc");
        String MC = mc.get(0);

        List<String> ti = deepLinkingIntent.getQueryParameters("ti");
        String TI = ti.get(0);

        List<String> tr = deepLinkingIntent.getQueryParameters("tr");
        String TR = tr.get(0);

        List<String> tn = deepLinkingIntent.getQueryParameters("tn");
        String TN = tn.get(0);

        List<String> am = deepLinkingIntent.getQueryParameters("am");
        String AM = am.get(0);

        List<String> cu = deepLinkingIntent.getQueryParameters("cu");
        String CU = cu.get(0);

        List<String> appid = deepLinkingIntent.getQueryParameters("appid");
        String APPID = appid.get(0);

        List<String> appname = deepLinkingIntent.getQueryParameters("appname");
        String APPNAME = appname.get(0);


        Bundle b = new Bundle();
        b.putString("APPNAME",APPNAME);
        b.putString("APPID", APPID);
        b.putString("CU",CU);
        b.putString("AM", AM);
        b.putString("TN",TN);
        b.putString("TR", TR);
        b.putString("TI",TI);
        b.putString("MC", MC);
        b.putString("PN",PN);
        b.putString("PA", PA);




    }

}catch(NullPointerException np){

    Log.d("No deep Linking","");

}










        appPref.getInstance().Initialize(getApplicationContext());
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
        System.out.println("SharedPreference Details In DASHBOARD \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode + " " + fNAME + " " + lNAME + " " + custFullName);
        profile_Pic = appPref.getInstance().getString("Profile_pic");
        if (profile_Pic != null) {
            Profile_Pic = decodeBase64(profile_Pic);
        } else {
            Profile_Pic = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.profile_pic);
        }
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        if (getSupportActionBar() != null) {
            mActionBar = getSupportActionBar();
            mActionBar.setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        if (custFullName != null) {
            mAdapter = new DashboardScreen_Adapter(TITLES, ICONS, custFullName, userMobileNo, Profile_Pic, this);
        } else {
            mAdapter = new DashboardScreen_Adapter(TITLES, ICONS, fNAME + " " + lNAME, userMobileNo, Profile_Pic, this);
        }
        mRecyclerView.setAdapter(mAdapter);
        final GestureDetector mGestureDetector = new GestureDetector(DashBoardWallet.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();
                    onTouchDrawer(recyclerView.getChildAdapterPosition(child));
                    Log.e("methode call", "Test 1");
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            }
        });
        //------------
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mDrawerToggle = new ActionBarDrawerToggle(DashBoardWallet.this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (Constants.checkImage) {
                    profile_Pic = appPref.getInstance().getString("Profile_pic");
                    if (profile_Pic != null) {
                        Profile_Pic = decodeBase64(profile_Pic);
                        ImageView img = (ImageView) findViewById(R.id.circleView);
                        img.setImageBitmap(Profile_Pic);
                    } else {
                        Profile_Pic = BitmapFactory.decodeResource(DashBoardWallet.this.getResources(),
                                R.drawable.profile_pic);
                        ImageView img = (ImageView) findViewById(R.id.circleView);
                        img.setImageBitmap(Profile_Pic);
                    }
                    Constants.checkImage = false;
                }
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // getSupportActionBar().setHomeButtonEnabled(true);
        }
        mDrawerToggle.syncState();
        if (savedInstanceState == null) {

               /*Wallet Fragment Class*/
           /* Wallet callWallet = new Wallet();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.frame_container, callWallet);
            // ft.addToBackStack("fragBack");
            ft.commit();
            setTitle("UPI Hackathon");
            Drawer.closeDrawers();*/

            try{
                if(deepLinkingIntent!=null) {

                    Insta_Pay insta_pay = new Insta_Pay();
                    fragmentManager = getSupportFragmentManager();
                    // getFragmentManager().popBackStack();
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container, insta_pay);
                    // ft.addToBackStack("fragback");
                    ft.commit();
                }
                else{
                    Wallet_UPI callWallet = new Wallet_UPI();
                    fragmentManager = getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.add(R.id.frame_container, callWallet);
                    // ft.addToBackStack("fragBack");
                    ft.commit();
                    setTitle("FInfinity");
                    Drawer.closeDrawers();
                }

            }catch(NullPointerException np){

                Log.d("no data","");
            }






        }
    }



    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void onTouchDrawer(int position) {
       // String screenNames[] = {"Profile", "Home", "Transaction History", "Change MPIN", "Invite Friends", "Manage Accounts", "Logout"};

        String screenNames[] =  {"Home","Send Money","Payment Address","Mandate Form", "Logout"};

        switch (position) {
            //case for Header USER PROFILE
            case 0:
                setTitle(screenNames[0]);
                Drawer.closeDrawers();
                Profile profileFragment = new Profile();
                fragmentManager = getSupportFragmentManager();
                // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
               /* for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }*/
                ft.replace(R.id.frame_container, profileFragment);
                //ft.addToBackStack("fragback");
                ft.commit();
                break;
            //case for HOME
            case 1:
                setTitle(screenNames[position]);
                Drawer.closeDrawers();
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
              /*  Wallet callWallet = new Wallet();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, callWallet);
                // ft.addToBackStack("fragBack");
                ft.commit();*/
                Wallet_UPI callWallet = new Wallet_UPI();
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, callWallet);
                // ft.addToBackStack("fragBack");
                ft.commit();
                Drawer.closeDrawers();
                break;
            case 2:
                System.out.println("case 5");
                setTitle(screenNames[position]);
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                Drawer.closeDrawers();
                SendMoney_UPI sendMoney_upi= new SendMoney_UPI();
                fragmentManager=getSupportFragmentManager();
                // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, sendMoney_upi);
                // ft.addToBackStack("fragback");
                ft.commit();
                Drawer.closeDrawers();
                break;


            case 3:
                System.out.println("case 6");
                setTitle(screenNames[position]);
                this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                Drawer.closeDrawers();
                GetVirtualAddresslist getVirtualAddresslist= new GetVirtualAddresslist();
                fragmentManager=getSupportFragmentManager();
               // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, getVirtualAddresslist);
               // ft.addToBackStack("fragback");
                ft.commit();
                Drawer.closeDrawers();
                break;
            case 4:
                System.out.println("case 7");
                setTitle(screenNames[position]);

                Drawer.closeDrawers();
                MandateForm_UPI mandateForm_upi= new MandateForm_UPI();
                fragmentManager=getSupportFragmentManager();
                // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, mandateForm_upi);
                // ft.addToBackStack("fragback");
                ft.commit();
                Drawer.closeDrawers();
                break;



            //case for Logout Accounts
            case 5:
                setTitle(screenNames[position]);
                Drawer.closeDrawers();
                new LogOut().execute();
                break;
            default:
                return;
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            //noinspection SimplifiableIfStatement
            if (mDrawerToggle.onOptionsItemSelected(item)) {
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (fragmentID) {
            /*Cases for Drawer item clicks*/
            case 0:
                showExitAlert();
                break;
            case 1:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 2:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 3:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 4:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 5:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            /*cases for Send Dialog Fragment*/
            case 6:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 7:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 8:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            /*case for sendMoney Fragment from send dialog after scan QR code*/
            case 9:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
              /*cases for Request Dialog Fragment*/
            case 10:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            /*case for share QR*/
            case 11:
                /*this one is for ShareQR Fragment*/
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            /*cases for Load Money Dialog calls*/
            case 12:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;

            /*cases for Notification*/

            case 13:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            /*cases for offer and deal */
            case 14:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 15:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 16:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 17:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 18:
                ft.replace(R.id.frame_container, new SetPaymentAddressFragment()).commit();
                break;
            case 19:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 20:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
            case 21:
                ft.replace(R.id.frame_container, new ChequeServicesDashBoard()).commit();
                break;
            case 22:
                ft.replace(R.id.frame_container, new Wallet_UPI()).commit();
                break;
        }
    }
    /*Show Exit Alert */
    public void showExitAlert() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        } else if (Drawer.isDrawerOpen(Gravity.LEFT)) {
            Drawer.closeDrawers();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    //LOGOUT SERVICE CALL
    private class LogOut extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, " Test 1 ");
            String resp = insertMpinNo();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(DashBoardWallet.this);
            // Set Progress Dialog Text
            Log.d(TAG, "Test 0");
            prgDialog.setMessage("Please wait...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, " Test 2 " + s);
            if (s.equals("") || s == null) {
                prgDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                Log.d(TAG, " Test 2.0 ");
                JSONObject mainObj = null;
                try {
                    mainObj = new JSONObject(s);
                    respParams = mainObj.getString("responseParameterMW");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("Result");
                    if (opStatus.equals("00")) {
                        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DashBoardWallet.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        prgDialog.dismiss();
                        onBackPressed();
                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    prgDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private String insertMpinNo() {
            URL url = null;
            String resp = "";
            Log.d(TAG, " Test 1.0 ");
            appPref.getInstance().Initialize(getApplicationContext());
            try {
                json = new JSONObject();
                json.put("ACTION", "");
                json.put("subActionId", "QRYSLogOut");
                json.put("entityId", "QRYS");
                json.put("cbsType", cbsType);
                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("MobileNo", userMobileNo);
                json.put("map", jsonInner);
                mwRequest = new MWRequest();
                resp = mwRequest.middleWareReq(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }
    }
}





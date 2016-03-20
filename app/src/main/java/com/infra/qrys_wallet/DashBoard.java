package com.infra.qrys_wallet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import com.infra.qrys_wallet.Fragments.ChangeMPIN;
import com.infra.qrys_wallet.Fragments.DashBoardFragment;
import com.infra.qrys_wallet.Fragments.InviteFriends;
import com.infra.qrys_wallet.Fragments.Profile;
import com.infra.qrys_wallet.Fragments.TransactionHistory;
import com.infra.qrys_wallet.Fragments_UPI.GetVirtualAddresslist;
import com.infra.qrys_wallet.Utils.Constants;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by sandeep.devhare on 16-11-2015.
 */
public class DashBoard extends AppCompatActivity {
    public RecyclerView.Adapter mAdapter;
    String screenNames[] = {"Profile", "Home", "Transaction History", "Change MPIN", "Invite Friends", "Manage Accounts",  "Logout"};
    String TITLES[] = {"Home", "Transaction History", "Change MPIN", "Invite Friends","Payment Address", "Manage Accounts",  "Logout"};




    int ICONS[] = {R.drawable.ic_home, R.drawable.ic_history, R.drawable.ic_change_mpin, R.drawable.ic_invite_frnds,  0, 0};
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_screen);
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

        System.out.println("SharedPreference Details In DASHBOARD \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode+" "+fNAME+" "+lNAME+" "+custFullName);
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
        final GestureDetector mGestureDetector = new GestureDetector(DashBoard.this, new GestureDetector.SimpleOnGestureListener() {
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
        mDrawerToggle = new ActionBarDrawerToggle(DashBoard.this, Drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(Constants.checkImage)
                {
                    profile_Pic = appPref.getInstance().getString("Profile_pic");
                    if (profile_Pic != null) {
                        Profile_Pic = decodeBase64(profile_Pic);
                        ImageView img = (ImageView)findViewById(R.id.circleView);
                        img.setImageBitmap(Profile_Pic);
                    } else {
                        Profile_Pic = BitmapFactory.decodeResource(DashBoard.this.getResources(),
                                R.drawable.profile_pic);
                        ImageView img = (ImageView)findViewById(R.id.circleView);
                        img.setImageBitmap(Profile_Pic);
                    }

                    Constants.checkImage=false;

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

             /*DashBoardHome Fragment Class*/
            DashBoardFragment homeFragment = new DashBoardFragment();
            fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.frame_container, homeFragment);
            ft.addToBackStack("fragBack");
            //homeFragment.setArguments(b);
            ft.commit();
            setTitle("Finfinity");
            Drawer.closeDrawers();
        }
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void onTouchDrawer(int position) {
        String screenNames[] = {"Profile", "Home", "Transaction History", "Change MPIN", "Invite Friends", "Payment Address","Manage Accounts","Logout"};
        switch (position) {
            //case for Header USER PROFILE
            case 0:
                setTitle(screenNames[0]);
                Drawer.closeDrawers();
                Profile profileFragment = new Profile();
                fragmentManager = getSupportFragmentManager();
                getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, profileFragment);
                ft.addToBackStack("fragback");
                ft.commit();
                break;
            //case for HOME
            case 1:
                setTitle(screenNames[position]);
                Drawer.closeDrawers();
                DashBoardFragment homeFragment = new DashBoardFragment();
                fragmentManager = getSupportFragmentManager();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, homeFragment);
                ft.commit();
                break;
            //case for Transaction History
            case 2:
                setTitle(screenNames[position]);
                Drawer.closeDrawers();
                TransactionHistory transactionHistory = new TransactionHistory();
                fragmentManager = getSupportFragmentManager();
                getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, transactionHistory);
                ft.addToBackStack("fragback");

                ft.commit();
                break;
            //case for Change MPIN
            case 3:
                setTitle(screenNames[position]);
                Drawer.closeDrawers();
                ChangeMPIN chngPin = new ChangeMPIN();
                fragmentManager = getSupportFragmentManager();
                getFragmentManager().popBackStack();

                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, chngPin);
                ft.addToBackStack("fragback");
                ft.commit();
                break;
            //case for Invite Friends
            case 4:
                setTitle(screenNames[position]);
                Drawer.closeDrawers();
                InviteFriends inviteFrnds = new InviteFriends();
                fragmentManager = getSupportFragmentManager();
                getFragmentManager().popBackStack();

                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, inviteFrnds);
                ft.addToBackStack("fragback");

                ft.commit();
                break;
            //case for Manage Account

            case 5:
                setTitle(screenNames[position]);
                Drawer.closeDrawers();
                GetVirtualAddresslist getVirtualAddresslist= new GetVirtualAddresslist();
                fragmentManager=getSupportFragmentManager();
                getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, getVirtualAddresslist);
                ft.addToBackStack("fragback");
                break;


            case 6:
                setTitle(screenNames[position]);
                Drawer.closeDrawers();
                break;
            /*//case for Reset Accounts
            case 6:
                setTitle(screenNames[position]);
                Drawer.closeDrawers();
                break;*/
            //case for Logout Accounts
            case 7:
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
        if (getSupportFragmentManager().findFragmentByTag("fragBack") != null) {

        }
        else {
            super.onBackPressed();
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_LONG).show();
            Fragment frag = getSupportFragmentManager().findFragmentByTag("fragBack");
            FragmentTransaction transac = getSupportFragmentManager().beginTransaction().remove(frag);
            transac.commit();
        }
    }
  /*  @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }*/
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
            prgDialog = new ProgressDialog(DashBoard.this);
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
                    respParams = mainObj.getString("responseParameter");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("Result");
                    if (opStatus.equals("00")) {
                        //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DashBoard.this, Login.class);
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





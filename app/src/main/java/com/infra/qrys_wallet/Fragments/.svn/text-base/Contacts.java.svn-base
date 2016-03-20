package com.infra.qrys_wallet.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.infra.qrys_wallet.Adapters.PhoneBook_SelectUser_Adapter;
import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
/**
 * Created by sandeep.devhare on 16-11-2015.
 */
public class Contacts extends Fragment {
    private FragmentTabHost mTabHost;
    SearchView searchView;
    PhoneBook_SelectUser_Adapter adapter;
    private FragmentManager fragmentManager;
    QRysUserSendMoneyPhoneBook qrysFrag;
    ProgressDialog prgDialog;
    private String TAG = "QRYsUserContact";
    JSONObject json, jsonInner;
    MWRequest mwRequest;
    String contactsFound, opStatus, result,respParams,idFound;
    public static ArrayList<String> mobileNos,getAllContacts,getAllIds;
    String requiredPhoneNumber="";
    private Menu optionsMenu;
    TabLayout tabLayout;

    int temp=2;
    final Bundle bundle = new Bundle();
    SharedPreference appPref;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo,fNAME, lNAME, custFullName,bankName;
    // Contact List

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Contacts");
        DashBoardWallet.fragmentID = 6;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
      //  setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.contacts, container, false);
        appPref.getInstance().Initialize(getActivity());
        Toolbar toolbar = (Toolbar)rootView.findViewById(R.id.tool_bar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Contacts");
    //    ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
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

        tabLayout = (TabLayout)rootView. findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Phonebook"));
        tabLayout.addTab(tabLayout.newTab().setText("QRys User"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.pager);

        final PagerAdapter adapter = new PagerAdapter
                (getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

       tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               viewPager.setCurrentItem(tab.getPosition());
               if (tab.getPosition() == 1) {

               } else {
                   // setRefreshActionButtonState(false);
               }
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {
           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {
           }
       });

        return rootView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        setHasOptionsMenu(true);
    }

   /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.optionsMenu = menu;

        menu.clear();
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_contacts, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }*/
   //@Override
   public boolean onCreateOptionsMenu(Menu menu){
       MenuInflater inflater = getActivity().getMenuInflater();
       inflater.inflate(R.menu.menu_contacts, menu);

       return true;
   }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {

            case R.id.action_sync:
                System.out.println("Sync called");
               setRefreshActionButtonState(true);

                break;
            case R.id.menu_item_search:
                System.out.println("Search called");
                SearchView sv = new SearchView(((DashBoardWallet) getActivity()).getSupportActionBar().getThemedContext());
                MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
                MenuItemCompat.setActionView(item, sv);
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        System.out.println("search query submit");
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        System.out.println("tap");
                        if(Phonebook.adapter!=null)
                        {
                            Phonebook.adapter.filter(newText);
                        }else
                        {
                            Toast.makeText(Contacts.this.getActivity(), "Contacts are not Available", Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                });
                break;

            default:
                return super.onOptionsItemSelected(item);
        }//end switch
        return true;
    }//end onOptionsItemSelected


    /*Sync*/
    public void setRefreshActionButtonState(final boolean refreshing) {
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.action_sync);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.synchcontacts);
                    SyncQRysUserContacts syncContacts = new SyncQRysUserContacts();
                    syncContacts.execute();

                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }
    /*-------------------*/
    private class SyncQRysUserContacts extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(TAG, " Test 1 ");
            String resp = passAvailableContacts();
            System.out.println("RESPONSE IS " + resp);
            return resp;
        }        @Override
                 protected void onPreExecute() {
            super.onPreExecute();
        }

        private String passAvailableContacts() {
            URL url = null;
            String resp = "";
            Log.d(TAG, " Test 1.0 ");

            mobileNos = Phonebook.contactNos;

            //Remove + " " [ ] from Contact No.
            String lastMobileNo = mobileNos.toString().replace("[", "").replace("]", "");

            Log.e("Constants Mobile No", " " + lastMobileNo);

            requiredPhoneNumber = lastMobileNo.replaceAll("[\\s\\-()]", "");

            System.out.println("requiredPhoneNumber " + requiredPhoneNumber);
            try {
                json = new JSONObject();
                json.put("ACTION", "");
                json.put("actionId", "ENQUIRY");
                json.put("subActionId", "QRYSCONTACTLIST");
                json.put("entityId", "QRYS");
                json.put("cbsType", cbsType);
                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("mobileNoList", requiredPhoneNumber);
                json.put("map", jsonInner);
                mwRequest = new MWRequest();
                resp = mwRequest.middleWareReq(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, " Test 2 " + s);
            if (s.equals("") || s == null) {
                // prgDialog.dismiss();
                Toast.makeText(Contacts.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                Log.d(TAG, " Test 2.0 ");
                JSONObject mainObj = null;
                try {
                    mainObj = new JSONObject(s);

                    mainObj = new JSONObject(s);
                    respParams = mainObj.getString("set");
                    Log.d(TAG, " respParams " + respParams);
                    JSONObject respObj = new JSONObject(respParams);
                    String firstNode = respObj.getString("setname");
                    String secondNode = respObj.getString("records");
                    Log.d(TAG, " setName " + firstNode);
                    Log.d(TAG, " records " + secondNode);



                    respParams = mainObj.getString("responseParameter");
                    JSONObject respObjforstatus = new JSONObject(respParams);
                    opStatus = respObjforstatus.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObjforstatus.getString("Result");
                    Log.d("Result", result);
                    String totallist = respObjforstatus.getString("totalListCount");
                    Log.d("totallist", totallist);
                    int l = Integer.parseInt(totallist);
                    Log.d("length", " " + l);
                    //Get the instance of JSONArray that contains JSONObjects
                    JSONArray jsonArray = respObj.optJSONArray("records");
                    System.out.println(" jsonArray.length() " + jsonArray.length());
                    //Iterate the jsonArray and print the info of JSONObjects
                    getAllContacts = new ArrayList<>();
                    getAllIds = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        contactsFound = jsonObject.getString("MobileNo");
                        idFound = jsonObject.getString("ContactIndex");

                        getAllContacts.add(contactsFound);
                        getAllIds.add(idFound);



                    }

                    if (opStatus.equals("00")) {
                        setRefreshActionButtonState(false);
                        temp=1;
                        Toast.makeText(Contacts.this.getActivity(), result, Toast.LENGTH_SHORT).show();

                        int p = tabLayout.getSelectedTabPosition();
                        Log.i("Position",""+p);
                        if(p==1)
                        {
                            System.out.println("Tab Pos " + p);


                            TabLayout.Tab tab = tabLayout.getTabAt(p);
                            tab.select();
                        }
                    } else {
                        // prgDialog.dismiss();
                        System.out.println("Contact Not Found " + contactsFound);
                        Toast.makeText(Contacts.this.getActivity(), result, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // prgDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;
        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Phonebook tab2 = new Phonebook();
                    return tab2;
                case 1:
                    qrysFrag= new QRysUserSendMoneyPhoneBook();
                    return qrysFrag; //added

                default:
                    return null;
            }
        }

        public void setData(){
            qrysFrag= new QRysUserSendMoneyPhoneBook(); //modified


        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}

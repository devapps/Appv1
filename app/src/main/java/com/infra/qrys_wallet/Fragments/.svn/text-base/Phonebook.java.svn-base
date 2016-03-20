package com.infra.qrys_wallet.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.Adapters.PhoneBook_SelectUser_Adapter;
import com.infra.qrys_wallet.Models.PhoneBook_SelectUser;
import com.infra.qrys_wallet.ProxyClasses.ContactInfo;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.Constants;
import com.infra.qrys_wallet.Utils.ContactHolderDesignPattern;
import com.infra.qrys_wallet.Utils.DataBase;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sandeep.devhare on 10-11-2015.
 */
public class Phonebook extends Fragment{
    public static ArrayList<String> contactNos;
    public static ArrayList<String> contactID;
    // ArrayList
   public static ArrayList<PhoneBook_SelectUser> selectUsers;
    List<PhoneBook_SelectUser> temp;
    // Contact List
    ListView listView;
    // Cursor to load contacts list
    Cursor phones, email;
    // Pop up
    ContentResolver resolver;
    android.support.v7.widget.SearchView searchView;
    public static PhoneBook_SelectUser_Adapter adapter;
    String phoneNumber, name, EmailAddr, image_thumb, id;
    Bitmap bit_thumb = null;
    String constructedNo;
    // SwipeRefreshLayout swipeView;
    SharedPreference appPref;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo, fNAME, lNAME, custFullName, bankName;
    String requestUrl, opStatus, result, resultStatus, customerId, reqPname, reqMono;
    MWRequest mwRequest;
    JSONObject json, jsonInner;
    ProgressDialog prgDialog;
    ContactInfo storeInDB = null;
    DataBase db;
    private WeakReference<SendMoneyTransaction> asyncTaskWeakRef;
    private Context ccontext;
    private String beneficiarypersonName, beneficiaryPhoneNo;
    private FragmentManager fragmentManager;

    public Phonebook() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setHasOptionsMenu(true);
        db = new DataBase(Phonebook.this.getActivity());
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
        bankName = appPref.getInstance().getString("selectedBankName");
        System.out.println("SharedPreference Details In QrysUserPhoneBook Fragment \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode + " " + fNAME + " " + lNAME + " \n " + custFullName + " " + bankName);
        selectUsers = new ArrayList<PhoneBook_SelectUser>();
        resolver = getActivity().getContentResolver();
        View rootView = inflater.inflate(R.layout.phonebook_display, container, false);
        //  swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
       /* swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadContact loadContact = new LoadContact();
                loadContact.execute();
            }
        });*/
        listView = (ListView) rootView.findViewById(R.id.contacts_list);
        //phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone._ID}, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC");
        contactNos = new ArrayList<String>();
        contactID = new ArrayList<String>();
        if (Constants.checkSQLiteInserted) {
            selectUsers.clear();
            Log.d("Flag is true", "Means data available in SQLite DB");

            /*GETTING CONTACT DETAILS FROM SINGLETON DESIGN PATTERN CLASS*/
            selectUsers = ContactHolderDesignPattern.getInstance().getSelectUsers();

            /*READING ALL STORED CONTACTS FROM SQLITE DATABASE*/
           /* List<ContactInfo> contacts = DataBase.getInstance(Phonebook.this.getActivity()).getAllContactsRecords();
            Log.d("All Contacts", "" + contacts);
            for (ContactInfo cn : contacts) {
                String log = "ID:" + cn.getContactssid() + "\n Name: " + cn.getName()
                        + "\n PhoneNos: " + cn.getPhone()
                        + "\n Image: " + cn.getThumb();
                // Writing Contacts to log
                Log.e("READING CONTACTS 4m DB", "\n " + log);
                Bitmap image = BitmapFactory.decodeResource(getResources(),
                        R.drawable.profile_pic);
                image = BitmapFactory.decodeByteArray(cn.getThumb(), 0, cn.getThumb().length);

                    *//*PASS CONTACT DETAILS TO MODEL CLASS TO VIEW IN LISTVIEW *//*
                PhoneBook_SelectUser selectUser = new PhoneBook_SelectUser();
                selectUser.setThumb(image);
                selectUser.setName(cn.getName());
                selectUser.setPhone(cn.getPhone());
                //   selectUser.setEmail(cn.getContactssid());
                // selectUser.setCheckedBox(false);
                selectUsers.add(selectUser);
            }*/
           // Toast.makeText(Phonebook.this.getActivity(), "Contacts load from SingleTone" + selectUsers.size(), Toast.LENGTH_SHORT).show();
            if (selectUsers != null) {
                adapter = new PhoneBook_SelectUser_Adapter(selectUsers, getActivity());
                listView.setAdapter(adapter);
                // Select item on listclick
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.e("search", "here---------------- listener");
                        PhoneBook_SelectUser data = selectUsers.get(i);
                        Log.e("Click Result ", "Name " + data.getName() + "  No." + data.getPhone() + " " + "Image" + data.getThumb() + " " + data.getPhone());
                        beneficiarypersonName = data.getName().toString();
                        beneficiaryPhoneNo = data.getPhone().toString();

                        /*Service call for send message*/
                        SendMoneyTransaction asyncTask = new SendMoneyTransaction(Phonebook.this);
                        asyncTaskWeakRef = new WeakReference<SendMoneyTransaction>(asyncTask);
                        asyncTask.execute();
                    }
                });
                listView.setFastScrollEnabled(true);
                //  swipeView.setRefreshing(false);
                //Constants.checkSQLiteInserted=false;
            } else {
                Toast.makeText(getActivity(), "No contacts in your contact list.", Toast.LENGTH_LONG).show();
            }
        } else {
            LoadContact loadContact = new LoadContact();
            loadContact.execute();
        }

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        phones.close();
        if (prgDialog != null)
            prgDialog.dismiss();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_contacts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }//end switch
    }//end onOptionsItemSelected

    /*Service call for Send Message */
    private boolean isAsyncTaskPendingOrRunning() {
        return this.asyncTaskWeakRef != null &&
                this.asyncTaskWeakRef.get() != null &&
                !this.asyncTaskWeakRef.get().getStatus().equals(AsyncTask.Status.FINISHED);
    }

    private void Dialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_invitebymsg);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        TextView tvSelectedBank = (TextView) dialog.findViewById(R.id.tvbankselected);
        tvSelectedBank.setText(" " + beneficiarypersonName);
        /*dialog.findViewById(R.id.btncancel).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });*/
        dialog.findViewById(R.id.otpok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Wallet callWallet = new Wallet();
                fragmentManager = getFragmentManager();
                getFragmentManager().popBackStack();
                android.support.v4.app.FragmentTransaction ft = fragmentManager.beginTransaction();
                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }
                ft.setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right);
                ft.replace(R.id.frame_container, callWallet);
                ft.addToBackStack("fragback");
                ft.commit();
            }
        });
        dialog.show();
    }


    // Load data on background
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone
            int i = 0;
            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                if (phones.getCount() == 0) {
                    Toast.makeText(getActivity(), "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }
                while (phones.moveToNext()) {
                    id = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
                    name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    //  EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                    image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));
                    contactID.add(id);
                    StringBuffer sb = new StringBuffer();
                    phoneNumber = phoneNumber.replaceAll("[\\s\\-()]", "");
                    if (phoneNumber.startsWith("+")) {
                        phoneNumber = phoneNumber.substring(1, phoneNumber.length());
                    }
                    constructedNo = sb.append(i + "##" + phoneNumber).toString();
                    i++;
                    System.out.println("constructed " + constructedNo);
                    contactNos.add(constructedNo);
                    try {
                        if (image_thumb != null) {
                            // Get the contact photo.
                            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                                    Long.parseLong(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))));
                            InputStream input =
                                    ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(), uri);
                            bit_thumb = BitmapFactory.decodeStream(input);
                            Log.e("Yes Image Thumb", "--------------");
                        } else {
                            bit_thumb = null;
                            Log.e("No Image Thumb", "--------------");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    phoneNumber.replaceAll("\\D", "");
                    name = name.replaceAll("&", "");
                    name.replace("|", "");
                    name = name.replace("|", "");


                    /*STORE RETRIVED CONTACTS DETAILS IN SQLITE DATABASE*/
                    Bitmap image = BitmapFactory.decodeResource(getResources(),
                            R.drawable.profile_pic);
                    // convert bitmap to byte
                    if (bit_thumb != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bit_thumb.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte imageInByte[] = stream.toByteArray();
                        // Inserting Contacts
                        Log.d("Inserting: ", "IF .." + name + " " + phoneNumber);
                        DataBase.getInstance(Phonebook.this.getActivity()).insertContacts(
                                new ContactInfo(Integer.parseInt(id), name, phoneNumber, imageInByte));
                        Constants.checkSQLiteInserted = true;
                    } else {
                        String text = "-NULL-";
                        try {
                            DataBase.getInstance(Phonebook.this.getActivity()).insertContacts(
                                    new ContactInfo(Integer.parseInt(id), name, phoneNumber, text.getBytes("UTF-8")));
                            Constants.checkSQLiteInserted = true;
                            Log.d("Inserting: ", "ELSE .." + name + " " + phoneNumber);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }




                    /*PASS CONTACT DETAILS TO MODEL CLASS TO VIEW IN LISTVIEW */
                    PhoneBook_SelectUser selectUser = new PhoneBook_SelectUser();
                    selectUser.setThumb(bit_thumb);
                    selectUser.setName(name);
                    selectUser.setPhone(phoneNumber);
                    //selectUser.setEmail(id);
                    // selectUser.setCheckedBox(false);
                    selectUsers.add(selectUser);
                    // Toast.makeText(Phonebook.this.getActivity(), "Contacts load from Device", Toast.LENGTH_SHORT).show();
                }
                  /*STORE ALL CONTACTS DETAILS IN SINGLETONE CLASS*/

                ContactHolderDesignPattern.getInstance().setSelectUsers(selectUsers);

            } else {
                Log.e("Cursor close 1", "----------------");
            }
            //phones.close();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(Phonebook.this.getActivity());
            // Set Progress Dialog Text
            prgDialog.setMessage("Loading Contacts...");
            // Set Cancelable as False
            prgDialog.setCancelable(false);
            prgDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (selectUsers != null) {
                adapter = new PhoneBook_SelectUser_Adapter(selectUsers, getActivity());
                listView.setAdapter(adapter);
                // Select item on listclick
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.e("search", "here---------------- listener");
                        PhoneBook_SelectUser data = selectUsers.get(i);
                        Log.e("Click Result ", "Name " + data.getName() + "  No." + data.getPhone() + " " + "Image" + data.getThumb() + " " + data.getPhone());
                        beneficiarypersonName = data.getName().toString();
                        beneficiaryPhoneNo = data.getPhone().toString();

                        /*Service call for send message*/
                        SendMoneyTransaction asyncTask = new SendMoneyTransaction(Phonebook.this);
                        asyncTaskWeakRef = new WeakReference<SendMoneyTransaction>(asyncTask);
                        asyncTask.execute();
                    }
                });
                listView.setFastScrollEnabled(true);
                //  swipeView.setRefreshing(false);
                prgDialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                prgDialog.dismiss();
            }
        }
    }

    public class SendMoneyTransaction extends AsyncTask<String, Void, String> {
        private WeakReference<Phonebook> fragmentWeakRef;

        private SendMoneyTransaction(Phonebook phBook) {
            this.fragmentWeakRef = new WeakReference<Phonebook>(phBook);
        }

        @Override
        protected String doInBackground(String... params) {
            String resp = doTransaction();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(Phonebook.this.getActivity());
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
                Toast.makeText(Phonebook.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
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
                json.put("subActionId", "QRYSSENDMONEY");
                json.put("entityId", "QRYS");
                jsonInner = new JSONObject();
                //jsonInner.put("DeviceId", DeviceId);
                //jsonInner.put("accountno",userAccountNo);
                jsonInner.put("transferType", "C");
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("beneficiaryMobileNo", beneficiaryPhoneNo);
                jsonInner.put("cbsType", cbsType);
                jsonInner.put("benefName", beneficiarypersonName);
                //   jsonInner.put("DEALOFFER","D");
                jsonInner.put("MobileNo", userMobileNo);
                //  jsonInner.put("city","Mumbai");
                json.put("map", jsonInner);
                System.out.println("json----------->" + json.toString());
                mwRequest = new MWRequest();
                resp = mwRequest.middleWareReq(json);
                System.out.println("response b4 return " + resp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return resp;
        }
    }
}

package com.infra.qrys_wallet.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infra.qrys_wallet.Adapters.PhoneBook_SelectUser_Adapter;
import com.infra.qrys_wallet.Models.PhoneBook_SelectUser;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * Created by sandeep.devhare on 16-11-2015.
 */
public class QRysUserPhoneBook extends Fragment {
    static List<String> mobileNos, getIds;
    ArrayList<String> getContacts;
    SearchView searchView;
    public static PhoneBook_SelectUser_Adapter adapter;
    ProgressDialog prgDialog;
    JSONObject json, jsonInner;
    MWRequest mwRequest;
    String contactsFound, opStatus, result, respParams;
    ArrayList<PhoneBook_SelectUser> selectUsers;
    // Cursor to load contacts list
    Cursor phones, email;
    // Pop up
    ContentResolver resolver;
    ListView listView;
    String phoneNumber, name, EmailAddr, image_thumb, id, idFound;
    Bitmap bit_thumb = null;
    String requiredPhoneNumber = "";
    private String TAG = "QRYsUserContact";
    private Menu optionsMenu;
    private Context ccontext;
    List<Set> test;
    ArrayList<String> con;
    ArrayList<String> idd;
    SharedPreference appPref;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo,fNAME, lNAME, custFullName,bankName;
    public QRysUserPhoneBook() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        con = new ArrayList<>();
        idd = new ArrayList<>();
        if (args != null && args.containsKey("id_User") && args.containsKey("contacts") && args.containsKey("ids")) {
            String userId = args.getString("id_User");

            con = args.getStringArrayList("contacts");
            idd = args.getStringArrayList("ids");

            System.out.println("UserId " + userId + " " + "caontacts  " + con + " " + "Ids " + idd);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setHasOptionsMenu(true);
        selectUsers = new ArrayList<PhoneBook_SelectUser>();
        resolver = getActivity().getContentResolver();
        getContacts = new ArrayList<>();

        getIds = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.phonebook_display, container, false);
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
        System.out.println("SharedPreference Details In QrysUserPhoneBook Fragment \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode+" "+fNAME+" "+lNAME+" \n "+custFullName+" "+bankName);

        listView = (ListView) rootView.findViewById(R.id.contacts_list);
        phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " ASC");
        SyncQRysUserContacts syncContacts = new SyncQRysUserContacts();
        syncContacts.execute();


        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        phones.close();
    }

@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.optionsMenu = menu;
        menu.clear();
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_contacts, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_sync:
                setRefreshActionButtonState(true);
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
                    // callAsynchronousTask();
                    SyncQRysUserContacts syncContacts = new SyncQRysUserContacts();
                    syncContacts.execute();
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    /* Load All Available contacts*/
    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone

            int matchedIndex = 0;
            int contactIndex = 0;

            if (phones != null) {
                Log.e("count", "" + phones.getCount());
                Log.e("Test 1", " doinbackground");
                if (phones.getCount() == 0) {
                    Toast.makeText(getActivity(), "No contacts in your contact list.", Toast.LENGTH_LONG).show();
                }
                while (phones.moveToNext()) {
                    id = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY));
                    phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    EmailAddr = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA2));
                    image_thumb = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI));

                    //Log.i("getID", getIds.toString());
                    //Log.i("getContact", getContacts.toString());
                    Log.i("getIds Size", getIds.size() + "--" + matchedIndex);
                    if (getIds != null && getContacts != null && getContacts.size() > 0 && getIds.size() > 0 && matchedIndex < getIds.size()) {

                        String lastMobileNo = phoneNumber.toString().replace("[", "").replace("]", "");
                        phoneNumber = lastMobileNo.replaceAll("[\\s\\-()]", "");

                        Log.i("getIds", getIds.get(matchedIndex) + "--" + contactIndex);
                        Log.i("getContacts", getContacts.get(matchedIndex) + "--" + phoneNumber);

                        if (getIds.get(matchedIndex).equals(""+contactIndex) && getContacts.get(matchedIndex).equals(phoneNumber)) {
                            System.out.println("Match found... Adding Contact");
                            try {
                                if (image_thumb != null) {
                                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                                            Long.parseLong(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))));
                                    InputStream input =
                                            ContactsContract.Contacts.openContactPhotoInputStream(getActivity().getContentResolver(), uri);
                                    bit_thumb = BitmapFactory.decodeStream(input);
                                } else {
                                    bit_thumb = null;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            phoneNumber.replaceAll("\\D", "");
                            name = name.replaceAll("&", "");
                            name.replace("|", "");
                            name = name.replace("|", "");
                            phoneNumber = phoneNumber.replaceAll("[\\s\\-()]", "");
                            if (phoneNumber.startsWith("+")) {
                                phoneNumber = phoneNumber.substring(1, phoneNumber.length());
                            }
                            PhoneBook_SelectUser selectUser = new PhoneBook_SelectUser();
                            selectUser.setThumb(bit_thumb);
                            selectUser.setName(name);
                            selectUser.setPhone(phoneNumber);
                            selectUser.setEmail(id);
                          //  selectUser.setCheckedBox(false);
                            selectUsers.add(selectUser);
                            matchedIndex++;
                        }
                    }
                    contactIndex++;
                }

            } else {
                Log.e("Cursor close 1", "----------------");
            }
            //phones.close();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("Test 1.3", "on Post");
            adapter = new PhoneBook_SelectUser_Adapter(selectUsers, getActivity());
            listView.setAdapter(adapter);
            Log.e("Test 1.4", "set Adapter");
            // Select item on listclick
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("search", "here---------------- listener");
                    PhoneBook_SelectUser data = selectUsers.get(i);
                    Log.e("Click Result ", "Name " + data.getName() + "  No." + data.getPhone() +" "+"Image"+data.getThumb());
                    Bundle b = new Bundle();
                    b.putString("Name",data.getName().toString());
                    b.putString("phoneNo", data.getPhone().toString());
                    b.putParcelable("Image",data.getThumb());
                    Log.e("Bundle"," "+b);
                    FragmentManager fm2 = getFragmentManager();
                    FragmentTransaction ft2 = fm2.beginTransaction();
                    ContactsRequestMoney qrMainScan = new ContactsRequestMoney();
                    ft2.replace(R.id.frame_container, qrMainScan);
                    ft2.addToBackStack("optional tag");
                    qrMainScan.setArguments(b);
                    ft2.commit();
                }
            });
            listView.setFastScrollEnabled(true);
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
        }

        @Override
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
                Toast.makeText(QRysUserPhoneBook.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                Log.d(TAG, " Test 2.0 ");
                JSONObject mainObj = null;
                try {
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

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        contactsFound = jsonObject.getString("MobileNo");
                        idFound = jsonObject.getString("ContactIndex");
                        getContacts.add(contactsFound);
                        getIds.add(idFound);
                    }
                    if (opStatus.equals("00")) {
                        Log.e("Test 0", " loadSync call");
                        LoadContact loadContact = new LoadContact();
                        loadContact.execute();
                      /*Store response element and pass it to QRYSUser Phone Book Class*/
                        setRefreshActionButtonState(false);
                        for (int i = 0; i < l; i++) {
                            //Remove + " " [ ] from Contact No.
                            String mob = getContacts.toString().replace("[", "").replace("]", "");
                            String ids = getIds.toString().replace("[", "").replace("]", "");
                            System.out.println("Contact Found " + mob);
                            System.out.println("ID Found " + ids);

                        }
                    } else {
                        // prgDialog.dismiss();
                        System.out.println("Contact Not Found " + contactsFound);
                        Toast.makeText(QRysUserPhoneBook.this.getActivity(), result, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // prgDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

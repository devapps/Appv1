package com.infra.qrys_wallet.Fragments_UPI;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.Adapters.UPI_SelectedBankAccountAdapter;
import com.infra.qrys_wallet.Fragments.UPI_BankListFragment;
import com.infra.qrys_wallet.Models.UPI_SelectedBankAccountsModel;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UPI_SelectedBankAccount extends Fragment {


    ListView BankAccountList;
    Button mapAccountButton;
    String resp,MiddlewareResponse,Status,BankList_Middleware,SelectedAccountType,
            SelectedAccountNo,SelectedIFSC,appPrefAccountNo,
            appPrefAccountType,appPrefIFSC,CredData_upi,CredKeyIndex_upi,
            CredCode_upi,CredName_upi,PayerAddress,MobileNo,cbsType,DeviceId,ResultMW,BanklistMW;
    JSONObject json, jsonInner;
    MWRequest mwRequest;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    public ProgressDialog prgDialog;
    ArrayList<UPI_SelectedBankAccountsModel> upiSelectedBankAccounts;
   // String[] rawData  = new String[]{"A","B","C"};
    JSONArray AccountListArray = null;
    SharedPreference appPref;
    UPI_SelectedBankAccountAdapter adapter;
    public UPI_SelectedBankAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Select Bank Accounts");
        View rootView= inflater.inflate(R.layout.fragment_upi__selected_bank_account, container, false);
        upiSelectedBankAccounts = new ArrayList<UPI_SelectedBankAccountsModel>();
        BankAccountList=(ListView)rootView.findViewById(R.id.listView);
        mapAccountButton=(Button)rootView.findViewById(R.id.mapAccountbutton);

        CredName_upi= appPref.getInstance().getString("cred_upi");
        CredCode_upi= appPref.getInstance().getString("keyCode_upi");
        CredKeyIndex_upi= appPref.getInstance().getString("keyindex_upi");
        CredData_upi= appPref.getInstance().getString("credData");

        PayerAddress= appPref.getInstance().getString("PaymentAddressStr_upi");

        System.out.println("CredName_upi middleware"+CredName_upi);
        System.out.println("CredKeyId_upi middleware"+CredCode_upi);
        System.out.println("CredKeyIndex_upi middleware"+CredKeyIndex_upi);
        System.out.println("CredData_upi middleware"+CredData_upi);


        MobileNo = appPref.getInstance().getString("user_mobile");
        DeviceId = appPref.getInstance().getString("Device_Id");
        cbsType = appPref.getInstance().getString("cBsType");


        prgDialog = new ProgressDialog(UPI_SelectedBankAccount.this.getActivity());
        // Set Progress Dialog Text

        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

        new getBankAccountList().execute();



        mapAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                appPrefAccountNo = appPref.getInstance().getString("SelectedAccountNo_upi");
                System.out.println("appPrefAccountNo "+appPrefAccountNo);
                if(appPrefAccountNo.equals("") || appPrefAccountNo.length()==0){

                    Toast.makeText(UPI_SelectedBankAccount.this.getActivity(),"Please select Account",Toast.LENGTH_SHORT).show();
                }
                else{
                    AccountMappedSetTPIN accountMappedSetTPIN=new AccountMappedSetTPIN();
                    fragmentManager =getFragmentManager();
                    ft=fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container, accountMappedSetTPIN);
                    ft.commit();



                }
            }
        });

        return rootView;
    }


    private class getBankAccountList extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            resp=getbankAccount();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            prgDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            System.out.println("String response onPost " + s);
            prgDialog.dismiss();
            if (s.equals("") || s == null||s.startsWith("Error")) {
                Toast.makeText(UPI_SelectedBankAccount.this.getActivity(), "", Toast.LENGTH_LONG).show();
                return;
            }

            else{
                prgDialog.dismiss();
                try{

                    JSONObject mainObj = new JSONObject(s);



                        BankList_Middleware=mainObj.getString("responseParameterMW");

                        System.out.println("get AddressList_Middleware for BankAccount "+BankList_Middleware);

                        JSONObject innerObject = new JSONObject(BankList_Middleware);

                        ResultMW = innerObject.getString("Result");
                        JSONObject addresslistObject = new JSONObject(ResultMW);
                        BanklistMW = addresslistObject.getString("responseParameter");
                        System.out.println("get AddresslistMW " + BanklistMW);


                        JSONObject banklistfinal = new JSONObject(BanklistMW);

                        AccountListArray=banklistfinal.getJSONArray("ListAccounts");
                        for(int i=0 ; i < AccountListArray.length() ; i++){

                            JSONObject accountObject = AccountListArray.getJSONObject(i);

                            String Name = accountObject.getString("NAME");
                            String MMID=accountObject.getString("MMID");
                            String ACCNUMBER=accountObject.getString("ACCNUMBER");
                            String IFSC=accountObject.getString("IFSC");
                           // String ACCTYPE=accountObject.getString("ACCTYPE");
                            String ACCTYPE="Saving";


                            System.out.println("UPI_selectedbankaccount Name "+Name+"MMID "+MMID +"ACCNUMBER "+ACCNUMBER+"IFSC "+IFSC+"ACCTYPE "+ACCTYPE);

                            upiSelectedBankAccounts.add(new UPI_SelectedBankAccountsModel(ACCTYPE,IFSC,ACCNUMBER));


                        }







                }catch(Exception e){


                }


               adapter=new UPI_SelectedBankAccountAdapter(UPI_SelectedBankAccount.this.getActivity(),upiSelectedBankAccounts);


                BankAccountList.setAdapter(adapter);
                BankAccountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        for (int j = 0; j < parent.getChildCount(); j++)
                            parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
                        view.setBackgroundColor(Color.LTGRAY);// to change the background color of list



                        TextView IFCS_Textview=(TextView)parent.getChildAt(position).findViewById(R.id.dynamicIFCS);
                        TextView AccountNo_Textview=(TextView)parent.getChildAt(position).findViewById(R.id.DynamicsavingAccount);
                        TextView AccountType_Textview=(TextView)parent.getChildAt(position).findViewById(R.id.textView14);

                        SelectedAccountType = AccountType_Textview.getText().toString();
                        appPref.getInstance().setString("SelectedAccountType_upi",SelectedAccountType);
                        SelectedAccountNo = AccountNo_Textview.getText().toString();
                        System.out.println("SelectedAccountNoii "+SelectedAccountNo);

                        appPref.getInstance().setString("SelectedAccountNo_upi",SelectedAccountNo);
                        SelectedIFSC = IFCS_Textview.getText().toString();
                        appPref.getInstance().setString("SelectedIFSC_upi",SelectedIFSC);

                    }
                });

            }
        }
    }


    public JSONObject Jsonselectedbankaccount() {
        json = new JSONObject();
        try {
            json = new JSONObject();
            json.put("action", "PSPService");
            json.put("subAction", "GetAccountList");
            //json.put("subAction", "GetPSPList");
            // json.put("entityID", "mahb");
            json.put("entityID","infrapsp");
            jsonInner = new JSONObject();
            jsonInner.put("entityID", "infrapsp");
            jsonInner.put("mobileNo", MobileNo);
            jsonInner.put("txnID", UPI_BankListFragment.TransactionId_upi);
            jsonInner.put("txnNote", "Custom");
            jsonInner.put("refID",UPI_BankListFragment.TransactionId_upi);
            jsonInner.put("refUrl", "http://www.bankofmaharashtra.in/");

            jsonInner.put("linkType", "MOBILE");
            jsonInner.put("linkValue", MobileNo);
            jsonInner.put("addressType", "ACCOUNT");
            jsonInner.put("payerAddr", PayerAddress);
            jsonInner.put("accNum","");
            jsonInner.put("ifsc", "MAPP");

            jsonInner.put("accType","");
            jsonInner.put("iin", "");
            jsonInner.put("uidNum", "");
            jsonInner.put("mmid", "");
            jsonInner.put("cardNum","");
            jsonInner.put("expDate", "");

            jsonInner.put("credType", CredName_upi);
            jsonInner.put("credSubType", CredName_upi);
            jsonInner.put("credCode", CredCode_upi);
            jsonInner.put("credKi", CredKeyIndex_upi);
            jsonInner.put("credData",CredData_upi);



            json.put("inputParam", jsonInner);
           /* mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getbankAccount() {

        String resp = "";
        JSONObject selectedbankaccountjson = Jsonselectedbankaccount();
        String DataString = selectedbankaccountjson.toString().replace("\"", "\\\"");
        System.out.println("DataString into string" + DataString.toString().replace("\"", "\\\""));

        try {
            System.out.println("getVirtualList method");
            json = new JSONObject();
            json.put("ACTION", "");
            json.put("subActionId", "UPIREQUESTHANDLER");
            json.put("entityId", "QRYS");
            json.put("cbsType", cbsType);
            jsonInner = new JSONObject();
            jsonInner.put("DeviceId", DeviceId);
            jsonInner.put("entityId", "QRYS");
            jsonInner.put("MobileNo", MobileNo);
            jsonInner.put("MPIN", "1234");
            jsonInner.put("upi_JSON_Data", DataString);
            json.put("map", jsonInner);
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("response from UPI getUPILIST "+resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;




    }
}

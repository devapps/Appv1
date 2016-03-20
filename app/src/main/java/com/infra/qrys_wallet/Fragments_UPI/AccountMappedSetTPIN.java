package com.infra.qrys_wallet.Fragments_UPI;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npci.upi.security.pinactivitycomponent.PinActivityComponent;

import java.util.HashMap;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountMappedSetTPIN extends Fragment implements View.OnClickListener {
    SharedPreference appPref;
    SharedPreferences mMyPreferences;
    FragmentManager fragmentManager;
    private FragmentTransaction ft;
    private String xmlPayloadString="";
    private String credAllowedString = "";
    private final String keyCode="NPCI";
    public static String TransactionId_upi;
    public static boolean checkCommonLib = false;
    View rootView;
    String resp,MobileNo,DeviceId,cbsType,PayerAddress,CredData_upi,CredKeyIndex_upi,
    CredCode_upi,CredName_upi,appPrefAccountNo,ResultMW,BanklistMW,BankList_Middleware,status;
    JSONObject json, jsonInner;
    MWRequest mwRequest;
    public ProgressDialog prgDialog;
    public AccountMappedSetTPIN() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("TPIN");

        rootView = inflater.inflate(R.layout.fragment_account_mapped_dialog, container, false);
        MobileNo = appPref.getInstance().getString("user_mobile");
        DeviceId = appPref.getInstance().getString("Device_Id");
        cbsType = appPref.getInstance().getString("cBsType");
        PayerAddress= appPref.getInstance().getString("PaymentAddressStr_upi");

        prgDialog = new ProgressDialog(AccountMappedSetTPIN.this.getActivity());
        // Set Progress Dialog Text

        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);


        TransactionId_upi=String.valueOf(new Random().nextInt(99999999));
        app_init(rootView);

        Button ok = (Button)rootView.findViewById(R.id.setTPIN);

        ok.setOnClickListener(this);

        return rootView;
    }





    public void app_init(View rootView){

        // Creating API Responses

        /* This will be obtained from the response of ListKeys API */
        xmlPayloadString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:RespListKeys xmlns:ns2=\"http://npci.org/upi/schema/\"><Head msgId=\"1GRDBknXpxwlntt6r3My\" orgId=\"NPCI\" ts=\"2016-03-11T00:07:51+05:30\" ver=\"1.0\"/><Resp reqMsgId=\"INFRAPSP2J7JFXYYER\" result=\"SUCCESS\"/><Txn id=\"INFRAPSP83W66W9K1U\" refId=\"testing\" refUrl=\"https://mahb.com\" ts=\"2016-03-11T00:00:02+05:30\" type=\"ListKeys\"/><keyList><key code=\"NPCI\" ki=\"20150822\" owner=\"NPCI\" type=\"PKI\"><keyValue xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4rIIEHkJ2TYgO/JUJQI/sxDgbDEAIuy9uTf4DItWeIMsG9AuilOj9R+dwAv8S6/9No/z0cwsw4UnsHQG1ALVIxFznLizMjaVJ7TJ+yTS9C9bYEFakRqH8b4jje7SC7rZ9/DtZGsaWaCaDTyuZ9dMHrgcmJjeklRKxl4YVmQJpzYLrK4zOpyY+lNPBqs+aiwJa53ZogcUGBhx/nIXfDDvVOtKzNb/08U7dZuXoiY0/McQ7xEiFcEtMpEJw5EB4o3RhE9j/IQOvc7l/BfD85+YQ5rJGk4HUb6GrQXHzfHvIOf53l1Yb0IX4v9q7HiAyOdggO+PVzXMSbrcFBrEjGZD7QIDAQAB</keyValue></key></keyList><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>x3+xFPIiKmFX4c+3Pid2IckJhxoBHWh49lxcVEqeFI0=</DigestValue></Reference></SignedInfo><SignatureValue>UAKQFFAYw26+F7YfPd+1uOG3zFO+OWa9nJs3evvR1HkrN8ga9DPc5Xe5Isq55YQojSrksTTqX9FEetEBd8dlR5D5uEt726o3HGsGiBdF6dGpJzziFRCjc9YoruFEA7V36StC3vMSJRZqt8CnpRPssn9hlNFIbc1NeZkWekUCM/jrihBsYSm8h52uy6BLWb7evsqb5BdAwwLwPhRy066BnM92skYjdXgunwr7NDpu8kIcQkK232Xas9Y1nYndp6J0Hma+8jdCo8BTv9gH95ZwtI8euToq7f8xILniHnQdt+8IkKEpGOC0qcOQrDUyB3wdKxt8ogCy72jRkfzQMIE7jw==</SignatureValue><KeyInfo><KeyValue><RSAKeyValue><Modulus>01DqzBsJTyMHT2S9MK5AIyFXNU646kwiOK3uymXIy9EW0nRKNKRkeIRTlGwX4wEnymGtGgX5B/Ij1elkLN4VJ9GplDV+wf0Lp2i2q4E6uRiWIzsqq42MCQgv8Fq/IPqjqPbeP9yh/8YPmBiMehBmhQd3qzl77C03k6d0yBIO5q/zXneTK9uFBNEL5yNpukrLGBcf3b9VHsjXpEaQrxGSMHCgNWpQgXpEcBr5OJ0/XxWbgMCZMlkYe1d6gswjuCRZ/xxJwEfbSO5AsnPtyqxSIjyhgEi9REtYnzaWwOBN4JCqt0pML0ja23lUwVJuNwkwNGKBXvkGoXUln8Sf7PIv7w==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue></KeyValue></KeyInfo></Signature></ns2:RespListKeys>";
       /* Log.i("xml_payload",xmlPayloadString);*/
        // xmlPayloadString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:RespListKeys xmlns:ns2=\"http://npci.org/upi/schema/\"><Head msgId=\"1GRDBknspOwrBRjZpXT3\" orgId=\"NPCI\" ts=\"2016-02-18T15:51:40+05:30\" ver=\"1.0\"/><Resp reqMsgId=\"MAHB14VWPQY4OI\" result=\"SUCCESS\"/><Txn id=\"MAHB2V5GR9EHHF\" note=\"payment\" refId=\"testing\" refUrl=\"https://mahb.com\" ts=\"2016-02-18T16:01:51+05:30\" type=\"ListKeys\"/><keyList><key code=\"NPCI\" ki=\"20150822\" owner=\"NPCI\" type=\"PKI\"><keyValue xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4rIIEHkJ2TYgO/JUJQI/sxDgbDEAIuy9uTf4DItWeIMsG9AuilOj9R+dwAv8S6/9No/z0cwsw4UnsHQG1ALVIxFznLizMjaVJ7TJ+yTS9C9bYEFakRqH8b4jje7SC7rZ9/DtZGsaWaCaDTyuZ9dMHrgcmJjeklRKxl4YVmQJpzYLrK4zOpyY+lNPBqs+aiwJa53ZogcUGBhx/nIXfDDvVOtKzNb/08U7dZuXoiY0/McQ7xEiFcEtMpEJw5EB4o3RhE9j/IQOvc7l/BfD85+YQ5rJGk4HUb6GrQXHzfHvIOf53l1Yb0IX4v9q7HiAyOdggO+PVzXMSbrcFBrEjGZD7QIDAQAB</keyValue></key></keyList><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>4wahq5ABCdy9IeqHiq6wTWIf+hqlha+VP3ieAYDk+eM=</DigestValue></Reference></SignedInfo><SignatureValue>tVgv11X9c3G+kofnn912X6PHps6hDM13BABtLQQAG76tu+nOhKnoHGAxInalEKRFif2ep01aDLEZyxl8liydmg0mSjAAb4B/EezoxgJbX3aq+8za/dbS0rqubejZrjwZu3ioMrpDwFCXIcftsSjCz+QIpdf8a9jmavzBaEOLj/Tpol0JUQzASiPxnLtZp6YzF2DLF5G2cCaDtUvLOkE5yixXxLRy5C/FmKBLdhwxcqc0V0xbr/epdweSP9YL2CVXKfyhJgCdPS0rHusc7MQF8o4KlzSGga+421AWSWkQ9epizV1GESdhutgmybfFaFZ3vnl6I3nuvt9vTWwm00FJnA==</SignatureValue><KeyInfo><KeyValue><RSAKeyValue><Modulus>01DqzBsJTyMHT2S9MK5AIyFXNU646kwiOK3uymXIy9EW0nRKNKRkeIRTlGwX4wEnymGtGgX5B/Ij1elkLN4VJ9GplDV+wf0Lp2i2q4E6uRiWIzsqq42MCQgv8Fq/IPqjqPbeP9yh/8YPmBiMehBmhQd3qzl77C03k6d0yBIO5q/zXneTK9uFBNEL5yNpukrLGBcf3b9VHsjXpEaQrxGSMHCgNWpQgXpEcBr5OJ0/XxWbgMCZMlkYe1d6gswjuCRZ/xxJwEfbSO5AsnPtyqxSIjyhgEi9REtYnzaWwOBN4JCqt0pML0ja23lUwVJuNwkwNGKBXvkGoXUln8Sf7PIv7w==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue></KeyValue></KeyInfo></Signature></ns2:RespListKeys>";

        /* This will be obtained from the response of ListAccountResponse API */
       /* credAllowedString = "{\n" +
                "\t\"CredAllowed\": [{\n" +
                "\t\t\"type\": \"TPIN\",\n" +
                "\t\t\"subtype\": \"TPIN\",\n" +
                "\t\t\"dType\": \"ALPH | NUM\",\n" +
                "\t\t\"dLength\": 6\n" +
                "\t}]\n" +
                "}";*/

        credAllowedString = "{\n" +
                "\t\"CredAllowed\": [{\n" +
                "\t\t\"type\": \"PIN\",\n" +
                "\t\t\"subtype\": \"MPIN\",\n" +
                "\t\t\"dType\": \"ALPH | NUM\",\n" +
                "\t\t\"dLength\": 6\n" +
                //"\t}, {\n" +
                //"\t\t\"type\": \"OTP\",\n" +
                //"\t\t\"subtype\": \"OTP\",\n" +
                //"\t\t\"dType\": \"NUM\",\n" +
                //"\t\t\"dLength\": 6\n" +
                "\t}]\n" +
                "}";


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Result Code:", String.valueOf(resultCode));
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            String errorMsgStr = data.getStringExtra("error");
            if (errorMsgStr != null && !errorMsgStr.isEmpty()) {
                Log.d("Error:", errorMsgStr);
                try {
                    JSONObject error = new JSONObject(errorMsgStr);
                    String errorCode = error.getString("errorCode");
                    String errorText = error.getString("errorText");
                    Toast.makeText(getActivity().getApplicationContext(), errorCode + ":" + errorText, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            HashMap<String, String> credListHashMap = (HashMap<String, String>)data.getSerializableExtra("credBlocks");
            for(String cred : credListHashMap.keySet()){ // This will return the list of field name e.g mpin,otp etc...
                try {
                    JSONObject credBlock=new JSONObject(credListHashMap.get(cred));
                    Log.i("enc_msg", credBlock.toString());
                    // adapter.add("Control name : "+cred);
                    // adapter.add("keyId : " + credBlock.getJSONObject("data").getString("code"));
                    // adapter.add("key index : " + credBlock.getJSONObject("data").getString("ki"));
                    // adapter.add("Encrypted Message from Common Library: " + credBlock.getJSONObject("data").getString("encryptedBase64String"));


                    System.out.println("Control name : " + cred);
                    System.out.println("keyId : " + credBlock.getJSONObject("data").getString("code"));
                    System.out.println("key index : " + credBlock.getJSONObject("data").getString("ki"));
                    System.out.println("Encrypted Message from Common Library: : " + credBlock.getJSONObject("data").getString("encryptedBase64String"));

                    appPref.getInstance().setString("cred_upi_tpin",cred);
                    appPref.getInstance().setString("keyCode_upi_tpin",credBlock.getJSONObject("data").getString("code"));
                    appPref.getInstance().setString("keyindex_upi_tpin",credBlock.getJSONObject("data").getString("ki"));
                    appPref.getInstance().setString("credData_tpin",credBlock.getJSONObject("data").getString("encryptedBase64String"));
                    new setTPIN().execute();

                    // UPI_CommonLibraryCall();

                   /* UPI_SelectedBankAccount upi_selectedBankAccount=new UPI_SelectedBankAccount();
                    fragmentManager=getFragmentManager();
                    ft=fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container,upi_selectedBankAccount);
                    ft.commit();*/


                    Log.i("enc_msg", credBlock.getJSONObject("data").getString("encryptedBase64String"));
                    //Log.i("enc_msg", credBlock.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.setTPIN:
                System.out.println("inside onClick ");

                Intent intent = new Intent(getActivity(), PinActivityComponent.class);
                // Create Keycode
                intent.putExtra("keyCode", keyCode);

                // Create xml payload
                if(xmlPayloadString.isEmpty()){
                    Toast.makeText(getActivity(),"XML List Key API is not loaded.",Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra("keyXmlPayload", xmlPayloadString);  // It will get the data from list keys API response

                // Create Controls
                if(credAllowedString.isEmpty()){
                    Toast.makeText(getActivity(),"Required Credentials could not be loaded.",Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra("controls", credAllowedString);

                // Create Configuration
                try {
                    JSONObject configuration = new JSONObject();
                    configuration.put("payerBankName", "Bank Of maharashtra");
                    configuration.put("backgroundColor","#FFFFFF");
                    Log.i("configuration", configuration.toString());
                    intent.putExtra("configuration", configuration.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Create Salt
                try {
                    JSONObject salt = new JSONObject();
                    salt.put("txnId", TransactionId_upi);
                    Log.i("txnId: ",TransactionId_upi );
                    salt.put("txnAmount", "100");
                    salt.put("deviceId", "74235ae00124fab8");
                    salt.put("appId", getActivity().getApplicationContext().getPackageName());
                    Log.i("salt", salt.toString());
                    intent.putExtra("salt", salt.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Create Pay Info
                JSONArray payInfoArray=new JSONArray();
                try {
                    JSONObject jsonPayeeName = new JSONObject();
                    jsonPayeeName.put("name", "payeeName");
                    jsonPayeeName.put("value",appPref.getInstance().getString("PaymentAddressStr_upi")+ "@infrapsp");
                    payInfoArray.put(jsonPayeeName);

                    JSONObject jsonNote = new JSONObject();
                    jsonNote.put("name", "note");
                    jsonNote.put("value", "Add Bank Account");
                    payInfoArray.put(jsonNote);

                    JSONObject jsonRefId = new JSONObject();
                    jsonRefId.put("name", "refId");
                    jsonRefId.put("value", TransactionId_upi);
                    payInfoArray.put(jsonRefId);

                    JSONObject jsonRefUrl = new JSONObject();
                    jsonRefUrl.put("name", "refUrl");
                    jsonRefUrl.put("value", "http://www.bankofmaharashtra.in/");
                    payInfoArray.put(jsonRefUrl);

                    /*JSONObject jsonAccount = new JSONObject();
                    jsonAccount.put("name", "account");
                    jsonAccount.put("value", "122XXX423");
                    payInfoArray.put(jsonAccount);*/

                    Log.i("payInfo", payInfoArray.toString());
                    intent.putExtra("payInfo", payInfoArray.toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Create Language Pref
                intent.putExtra("languagePref", "en_US");

                startActivityForResult(intent, 1);

        }
    }

    private class setTPIN extends AsyncTask<String,String,String>{




        @Override
        protected String doInBackground(String... params) {
            resp=getUPITPIN();
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
            prgDialog.dismiss();
            if (s.equals("") || s == null||s.startsWith("Error")) {
                Toast.makeText(AccountMappedSetTPIN.this.getActivity(), "No Data", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                prgDialog.dismiss();
                try{


                    JSONObject mainObj = new JSONObject(s);



                    BankList_Middleware=mainObj.getString("responseParameterMW");

                    System.out.println("get AddressList_Middleware "+BankList_Middleware);

                    JSONObject innerObject = new JSONObject(BankList_Middleware);


                    ResultMW = innerObject.getString("Result");
                    JSONObject addresslistObject = new JSONObject(ResultMW);
                    BanklistMW = addresslistObject.getString("responseParameter");

                    System.out.println("get AddresslistMW " + BanklistMW);

                    status=addresslistObject.getString("status");

                    System.out.println("get status " + status);

                    if(status.equals("true")){

                        SuccessfullTPIN successfullTPIN=new SuccessfullTPIN();
                        fragmentManager =getFragmentManager();
                        ft=fragmentManager.beginTransaction();
                        ft.replace(R.id.frame_container, successfullTPIN);
                        ft.commit();

                    }


                }catch(Exception e){

                }
            }
        }
    }

    public JSONObject JsonsetupiTPIN() {
        json = new JSONObject();
        CredName_upi= appPref.getInstance().getString("cred_upi_tpin");
        CredCode_upi= appPref.getInstance().getString("keyCode_upi_tpin");
        CredKeyIndex_upi= appPref.getInstance().getString("keyindex_upi_tpin");
        CredData_upi= appPref.getInstance().getString("credData_tpin");
        appPrefAccountNo = appPref.getInstance().getString("SelectedAccountNo_upi");
        System.out.println("cred_upi_tpin middleware"+CredName_upi);
        System.out.println("keyCode_upi_tpin middleware"+CredCode_upi);
        System.out.println("keyindex_upi_tpin middleware"+CredKeyIndex_upi);
        System.out.println("credData_tpin middleware"+CredData_upi);
        System.out.println("appPrefAccountNo middleware"+appPrefAccountNo);
        try {
            json = new JSONObject();
            json.put("action", "PaymentAddress");
            json.put("subAction", "AddPaymentAddress");
            //json.put("subAction", "GetPSPList");
            // json.put("entityID", "mahb");
            json.put("entityID","infrapsp");
            jsonInner = new JSONObject();
            jsonInner.put("entityID", "infrapsp");
            jsonInner.put("mobileNo", MobileNo);
            jsonInner.put("payerName", "hemal");

            jsonInner.put("txnID", TransactionId_upi);
            jsonInner.put("txnNote", "Custom");
            jsonInner.put("refID",TransactionId_upi);
            jsonInner.put("refUrl", "http://www.bankofmaharashtra.in/");


            jsonInner.put("addressType", "ACCOUNT");
            jsonInner.put("payerAddr", PayerAddress);
            jsonInner.put("accNum",appPrefAccountNo);
            jsonInner.put("ifsc", "MAPP");

            jsonInner.put("accType","Savings");



            jsonInner.put("cardNum","");
            jsonInner.put("expDate", "");
            jsonInner.put("credType", "PIN");
            jsonInner.put("credSubType", CredName_upi);
            jsonInner.put("credCode", CredCode_upi);
            jsonInner.put("credKi", CredKeyIndex_upi);
            jsonInner.put("credData",CredData_upi);
            jsonInner.put("newCredType", "PIN");
            jsonInner.put("newCredSubType", CredName_upi);
            jsonInner.put("newCredCode", CredCode_upi);
            jsonInner.put("newCredKi", CredKeyIndex_upi);
            jsonInner.put("newCredData",CredData_upi);



            json.put("inputParam", jsonInner);
           /* mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getUPITPIN() {
        String resp = "";
        JSONObject selectedbankaccountjson = JsonsetupiTPIN();
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

            System.out.println("request from UPI for json form " + json);
            mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);
            System.out.println("response from UPI for TPIN "+resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
}

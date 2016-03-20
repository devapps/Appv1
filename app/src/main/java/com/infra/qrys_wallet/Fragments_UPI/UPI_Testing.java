package com.infra.qrys_wallet.Fragments_UPI;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest_json;
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
public class UPI_Testing extends Fragment implements View.OnClickListener {

    View rootView;
    Button listAccount;
    JSONObject json, jsonInner,jsonDeviceInner;
    private String xmlPayloadString="";
    private String credAllowedString = "";
    private final String keyCode="NPCI";
    public static String TransactionId_upi;
    SharedPreference appPref;
    FragmentManager fragmentManager;
    MWRequest_json mwRequest;
    private FragmentTransaction ft;
    String resp,MobileNo,CredCodeinsta,CredNameinsta,CredKeyIndexinsta,CredDatainsta,appPrefAccountNo,appPrefIFSC,CredName_upi;
    public UPI_Testing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView= inflater.inflate(R.layout.fragment_upi__testing, container, false);
        TransactionId_upi=String.valueOf(new Random().nextInt(99999999));
        listAccount=(Button)rootView.findViewById(R.id.listaccount);
        MobileNo = appPref.getInstance().getString("user_mobile");
        listAccount.setOnClickListener(this);
        app_init(rootView);
        return rootView;
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.listaccount:

                Intent intent = new Intent(getActivity(), PinActivityComponent.class);
                // Create Keycode
                intent.putExtra("keyCode", keyCode);

                // Create xml payload
                if(xmlPayloadString.isEmpty()){
                    Toast.makeText(getActivity(), "XML List Key API is not loaded.", Toast.LENGTH_LONG).show();
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
                    jsonPayeeName.put("value","amit@infrapsp");
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

    public void app_init(View rootView){

        // Creating API Responses

        /* This will be obtained from the response of ListKeys API */
        xmlPayloadString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:RespListKeys xmlns:ns2=\"http://npci.org/upi/schema/\"><Head msgId=\"1GRDBknXpxwlntt6r3My\" orgId=\"NPCI\" ts=\"2016-03-11T00:07:51+05:30\" ver=\"1.0\"/><Resp reqMsgId=\"INFRAPSP2J7JFXYYER\" result=\"SUCCESS\"/><Txn id=\"INFRAPSP83W66W9K1U\" refId=\"testing\" refUrl=\"https://mahb.com\" ts=\"2016-03-11T00:00:02+05:30\" type=\"ListKeys\"/><keyList><key code=\"NPCI\" ki=\"20150822\" owner=\"NPCI\" type=\"PKI\"><keyValue xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4rIIEHkJ2TYgO/JUJQI/sxDgbDEAIuy9uTf4DItWeIMsG9AuilOj9R+dwAv8S6/9No/z0cwsw4UnsHQG1ALVIxFznLizMjaVJ7TJ+yTS9C9bYEFakRqH8b4jje7SC7rZ9/DtZGsaWaCaDTyuZ9dMHrgcmJjeklRKxl4YVmQJpzYLrK4zOpyY+lNPBqs+aiwJa53ZogcUGBhx/nIXfDDvVOtKzNb/08U7dZuXoiY0/McQ7xEiFcEtMpEJw5EB4o3RhE9j/IQOvc7l/BfD85+YQ5rJGk4HUb6GrQXHzfHvIOf53l1Yb0IX4v9q7HiAyOdggO+PVzXMSbrcFBrEjGZD7QIDAQAB</keyValue></key></keyList><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>x3+xFPIiKmFX4c+3Pid2IckJhxoBHWh49lxcVEqeFI0=</DigestValue></Reference></SignedInfo><SignatureValue>UAKQFFAYw26+F7YfPd+1uOG3zFO+OWa9nJs3evvR1HkrN8ga9DPc5Xe5Isq55YQojSrksTTqX9FEetEBd8dlR5D5uEt726o3HGsGiBdF6dGpJzziFRCjc9YoruFEA7V36StC3vMSJRZqt8CnpRPssn9hlNFIbc1NeZkWekUCM/jrihBsYSm8h52uy6BLWb7evsqb5BdAwwLwPhRy066BnM92skYjdXgunwr7NDpu8kIcQkK232Xas9Y1nYndp6J0Hma+8jdCo8BTv9gH95ZwtI8euToq7f8xILniHnQdt+8IkKEpGOC0qcOQrDUyB3wdKxt8ogCy72jRkfzQMIE7jw==</SignatureValue><KeyInfo><KeyValue><RSAKeyValue><Modulus>01DqzBsJTyMHT2S9MK5AIyFXNU646kwiOK3uymXIy9EW0nRKNKRkeIRTlGwX4wEnymGtGgX5B/Ij1elkLN4VJ9GplDV+wf0Lp2i2q4E6uRiWIzsqq42MCQgv8Fq/IPqjqPbeP9yh/8YPmBiMehBmhQd3qzl77C03k6d0yBIO5q/zXneTK9uFBNEL5yNpukrLGBcf3b9VHsjXpEaQrxGSMHCgNWpQgXpEcBr5OJ0/XxWbgMCZMlkYe1d6gswjuCRZ/xxJwEfbSO5AsnPtyqxSIjyhgEi9REtYnzaWwOBN4JCqt0pML0ja23lUwVJuNwkwNGKBXvkGoXUln8Sf7PIv7w==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue></KeyValue></KeyInfo></Signature></ns2:RespListKeys>";
       /* Log.i("xml_payload",xmlPayloadString);*/
        // xmlPayloadString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:RespListKeys xmlns:ns2=\"http://npci.org/upi/schema/\"><Head msgId=\"1GRDBknspOwrBRjZpXT3\" orgId=\"NPCI\" ts=\"2016-02-18T15:51:40+05:30\" ver=\"1.0\"/><Resp reqMsgId=\"MAHB14VWPQY4OI\" result=\"SUCCESS\"/><Txn id=\"MAHB2V5GR9EHHF\" note=\"payment\" refId=\"testing\" refUrl=\"https://mahb.com\" ts=\"2016-02-18T16:01:51+05:30\" type=\"ListKeys\"/><keyList><key code=\"NPCI\" ki=\"20150822\" owner=\"NPCI\" type=\"PKI\"><keyValue xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4rIIEHkJ2TYgO/JUJQI/sxDgbDEAIuy9uTf4DItWeIMsG9AuilOj9R+dwAv8S6/9No/z0cwsw4UnsHQG1ALVIxFznLizMjaVJ7TJ+yTS9C9bYEFakRqH8b4jje7SC7rZ9/DtZGsaWaCaDTyuZ9dMHrgcmJjeklRKxl4YVmQJpzYLrK4zOpyY+lNPBqs+aiwJa53ZogcUGBhx/nIXfDDvVOtKzNb/08U7dZuXoiY0/McQ7xEiFcEtMpEJw5EB4o3RhE9j/IQOvc7l/BfD85+YQ5rJGk4HUb6GrQXHzfHvIOf53l1Yb0IX4v9q7HiAyOdggO+PVzXMSbrcFBrEjGZD7QIDAQAB</keyValue></key></keyList><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>4wahq5ABCdy9IeqHiq6wTWIf+hqlha+VP3ieAYDk+eM=</DigestValue></Reference></SignedInfo><SignatureValue>tVgv11X9c3G+kofnn912X6PHps6hDM13BABtLQQAG76tu+nOhKnoHGAxInalEKRFif2ep01aDLEZyxl8liydmg0mSjAAb4B/EezoxgJbX3aq+8za/dbS0rqubejZrjwZu3ioMrpDwFCXIcftsSjCz+QIpdf8a9jmavzBaEOLj/Tpol0JUQzASiPxnLtZp6YzF2DLF5G2cCaDtUvLOkE5yixXxLRy5C/FmKBLdhwxcqc0V0xbr/epdweSP9YL2CVXKfyhJgCdPS0rHusc7MQF8o4KlzSGga+421AWSWkQ9epizV1GESdhutgmybfFaFZ3vnl6I3nuvt9vTWwm00FJnA==</SignatureValue><KeyInfo><KeyValue><RSAKeyValue><Modulus>01DqzBsJTyMHT2S9MK5AIyFXNU646kwiOK3uymXIy9EW0nRKNKRkeIRTlGwX4wEnymGtGgX5B/Ij1elkLN4VJ9GplDV+wf0Lp2i2q4E6uRiWIzsqq42MCQgv8Fq/IPqjqPbeP9yh/8YPmBiMehBmhQd3qzl77C03k6d0yBIO5q/zXneTK9uFBNEL5yNpukrLGBcf3b9VHsjXpEaQrxGSMHCgNWpQgXpEcBr5OJ0/XxWbgMCZMlkYe1d6gswjuCRZ/xxJwEfbSO5AsnPtyqxSIjyhgEi9REtYnzaWwOBN4JCqt0pML0ja23lUwVJuNwkwNGKBXvkGoXUln8Sf7PIv7w==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue></KeyValue></KeyInfo></Signature></ns2:RespListKeys>";

        /* This will be obtained from the response of ListAccountResponse API */
        credAllowedString = "{\n" +
                "\t\"CredAllowed\": [{\n" +
                "\t\t\"type\": \"OTP\",\n" +
                "\t\t\"subtype\": \"OTP\",\n" +
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
                    System.out.println("CredCode : " + credBlock.getJSONObject("data").getString("code"));
                    System.out.println("key index : " + credBlock.getJSONObject("data").getString("ki"));
                    System.out.println("Encrypted Message from Common Library: : " + credBlock.getJSONObject("data").getString("encryptedBase64String"));

                    appPref.getInstance().setString("cred_upi", cred);
                    appPref.getInstance().setString("keyCode_upi", credBlock.getJSONObject("data").getString("code"));
                    appPref.getInstance().setString("keyindex_upi", credBlock.getJSONObject("data").getString("ki"));
                    appPref.getInstance().setString("credData", credBlock.getJSONObject("data").getString("encryptedBase64String"));


                    new listAccountData().execute();


                    Log.i("enc_msg", credBlock.getJSONObject("data").getString("encryptedBase64String"));
                    //Log.i("enc_msg", credBlock.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private class listAccountData extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            resp=getbankAccount();
            return resp;
        }
    }

    private String getbankAccount() {

        json = new JSONObject();
        CredNameinsta= appPref.getInstance().getString("cred_upi_insta");
        CredCodeinsta= appPref.getInstance().getString("keyCode_upi_insta");
        CredKeyIndexinsta= appPref.getInstance().getString("keyindex_upi");
        CredDatainsta= appPref.getInstance().getString("credData");
        appPrefAccountNo = appPref.getInstance().getString("SelectedAccountNo_upi");
        appPrefIFSC= appPref.getInstance().getString("SelectedIFSC_upi");
        try {

            json.put("txnId",TransactionId_upi );
            json.put("payerAddress", "amit@mapp");
            json.put("payerName", "Amit K");
            json.put("linkType", "MOBILE");
            json.put("linkValue", MobileNo);
            json.put("accountAddressType", "ACCOUNT");


            JSONArray req = new JSONArray();
            jsonInner = new JSONObject();

            jsonInner.put("ifsc","MAPP0001865");
            jsonInner.put("acType","");
            jsonInner.put("acNum","");
            jsonInner.put("iin",null);
            jsonInner.put("uIdNum",null);
            jsonInner.put("mmId",null);
            jsonInner.put("mobNum",null);
            jsonInner.put("cardNum",null);

            req.put(jsonInner);
            json.put("detailsJson",req);

            json.put("credType", "PIN");
            json.put("credSubType", CredName_upi);
            json.put("credDataValue", CredDatainsta);
            json.put("credDataCode", "NPCI");
            json.put("credDataKi", CredKeyIndexinsta);
            mwRequest = new MWRequest_json();
            resp = mwRequest.serviceReq(json);
            System.out.println("response from UPI testing... " + resp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}

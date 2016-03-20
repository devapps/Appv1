package com.infra.qrys_wallet.Fragments_UPI;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.Models.VirtualAddressList;
import com.infra.qrys_wallet.NFCTagReadActivity_UPI;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.Constants;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.NdefMessageParser;
import com.infra.qrys_wallet.Utils.ParsedNdefRecord;
import com.infra.qrys_wallet.Utils.ScanQRMain;
import com.infra.qrys_wallet.Utils.SharedPreference;
import com.infra.qrys_wallet.Utils.TextRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npci.upi.security.pinactivitycomponent.PinActivityComponent;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class Insta_Pay extends Fragment {
    Button sendMoney,QRscanButton,NFCScanButton;
    Spinner PayerAddress;
    View rootView;
    SharedPreference appPref;
    String userMobileNo,userMpin,FrequenctStr,SpinnerValue_frequency,
            MobileNo,cbsType,DeviceId,Status,ResultMW, AddresslistMW,
            SpinnerValuePref,Datestr,ToViewstr,Amountstr,ipAddress,PayeeAdd,
            MiddlewareResponse,AddressList_Middleware,c,respdata,
            getSpinnerValue,MobileNo_Shared,CredDatainsta,CredKeyIndexinsta,appPrefAccountNo,appPrefIFSC,
    CredCodeinsta,CredNameinsta,PayerAddressStr;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    EditText PayerAmount,PayerRemark,Toview,Date,FrequencyEditText;
    String resp;
    private RelativeLayout mTagContent;
    private AlertDialog mDialog;
    public static String TransactionId_upi;
    private String xmlPayloadString="";
    private String credAllowedString = "";
    private final String keyCode="NPCI";
    JSONArray addresslist = null;
    ArrayList<VirtualAddressList> Addressitems;
    List<String> stockList = new ArrayList<String>();
    String[] AddressArr = new String[stockList.size()];
    String SpinnerValue;
    JSONObject json, jsonInner,jsonDeviceInner;
    MWRequest mwRequest;
    String strtext;
    public static boolean CheckQRScanPositionInsta = false;
    public static boolean CheckNFCScanPosition = false;
    String value,APPNAME,APPID,CU,AM,TN,TR,TI,MC,PN,PA;
    public Insta_Pay() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DashBoardWallet.fragmentID=20;
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Insta Pay");
          rootView= inflater.inflate(R.layout.fragment_insta__pay, container, false);
        app_init(rootView);
        TransactionId_upi=String.valueOf(new Random().nextInt(99999999));
        sendMoney=(Button)rootView.findViewById(R.id.sendMoneyButton);
        QRscanButton=(Button)rootView.findViewById(R.id.QRbutton);
        NFCScanButton=(Button)rootView.findViewById(R.id.NFCbutton);
        PayerAddress = (Spinner)rootView.findViewById(R.id.sendmoneyfromspinner);
        MobileNo = appPref.getInstance().getString("user_mobile");
        DeviceId = appPref.getInstance().getString("Device_Id");
        cbsType = appPref.getInstance().getString("cBsType");
        MobileNo_Shared =appPref.getInstance().getString("user_mobile");
        Toview=(EditText)rootView.findViewById(R.id.Toviewe);
        PayerAmount = (EditText) rootView.findViewById(R.id.Amountviewe);
        Amountstr = PayerAmount.getText().toString();
        PayerRemark = (EditText) rootView.findViewById(R.id.Remarkviewe);
        Toview = (EditText) rootView.findViewById(R.id.Toviewe);



        Bundle extras = getActivity().getIntent().getExtras();

        if (extras != null) {
            APPNAME = extras.getString("APPNAME");
            APPID = extras.getString("APPID");
            CU = extras.getString("CU");
            AM = extras.getString("AM");
            TN = extras.getString("TN");
            TR = extras.getString("TR");
            TI = extras.getString("TI");
            MC = extras.getString("MC");
            PN = extras.getString("PN");
            PA = extras.getString("PA");



        }
        Toview.setText(PA);
        PayerAmount.setText(AM);
        PayerRemark.setText(TN);


        new getPayerAddress().execute();
        if(ScanQRMain.CheckQRScanrevertPositionInstaPay==true){


            try{
                 strtext = getArguments().getString("DetailsUPI");
                System.out.println("strText "+strtext);
                Toview.setText(strtext);
            }catch(NullPointerException np){


                Toast.makeText(Insta_Pay.this.getActivity()," data"+ ScanQRMain.CheckQRScanrevertPositionInstaPay, Toast.LENGTH_LONG).show();
            }
            ScanQRMain.CheckQRScanrevertPositionInstaPay=false;



        }

        QRscanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckQRScanPositionInsta = true;
                   /*this is just for testing the UI of SendMoney Screen, plz see below commented code for actual*/
                ScanQRMain qrMainScan = new ScanQRMain();
                // getFragmentManager().popBackStack();
                fragmentManager = getFragmentManager();
                ft = fragmentManager.beginTransaction();
                /*for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }*/
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, qrMainScan);
                // ft.addToBackStack("fragback");
                ft.commit();
            }
        });
        NFCScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckNFCScanPosition=true;
              /*  NFCsplashScreen nfcTag = new NFCsplashScreen();
                // getFragmentManager().popBackStack();
                fragmentManager = getFragmentManager();
                ft = fragmentManager.beginTransaction();
                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, nfcTag);
                //ft.addToBackStack("fragback");
                ft.commit();*/


                Intent nfcCall = new Intent(getActivity(), NFCTagReadActivity_UPI.class);
                startActivity(nfcCall);

            }
        });
        resolveIntent(Insta_Pay.this.getActivity().getIntent());

        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("getPayers method " + SpinnerValue + "Toview " + Toview);




                getSpinnerValue = appPref.getInstance().getString("SpinnerValue_upi");


                PayerAmount = (EditText) rootView.findViewById(R.id.Amountviewe);
                Amountstr = PayerAmount.getText().toString();
                PayerRemark = (EditText) rootView.findViewById(R.id.Remarkviewe);
                Toview = (EditText) rootView.findViewById(R.id.Toviewe);
                ToViewstr = Toview.getText().toString();


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
        });

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

                    appPref.getInstance().setString("cred_upi_insta",cred);
                    appPref.getInstance().setString("keyCode_upi_insta",credBlock.getJSONObject("data").getString("code"));
                    appPref.getInstance().setString("keyindex_upi_insta",credBlock.getJSONObject("data").getString("ki"));
                    appPref.getInstance().setString("credData_insta", credBlock.getJSONObject("data").getString("encryptedBase64String"));
                   // new instaPayment().execute();
                    new instaPay().execute();
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
    public void onStart() {
        super.onStart();
        if(NFCTagReadActivity_UPI.CheckNFCScanrevertPosition==true){
            System.out.println("Inside CheckNFCScanrevertPosition true ");
            // String strtext = getArguments().getString("nfcdata");
            String strtext =   appPref.getInstance().getString("nfcdata");

            if(strtext.equals("null") || strtext.length()==0)
            {
                Toast.makeText(Insta_Pay.this.getActivity(), "No Correct Code Found", Toast.LENGTH_LONG).show();
            }
            else{
                System.out.println("strText "+strtext);
                Toview.setText(strtext);
                NFCTagReadActivity_UPI.CheckNFCScanrevertPosition=false;
            }

        }
    }

    /*   @Override
       public void onActivityResult(int requestCode, int resultCode, Intent data) {
           super.onActivityResult(requestCode, resultCode, data);
           if (requestCode == RESULTNFC){
               Log.e("OnActivityResult","called");
               String text1 = getArguments().getString("nfcResult");
               Log.e("Dataxxxxxx","is: "+text1);
               if (Activity.RESULT_OK == resultCode) {
                   Log.e("OnActivityResult","called");
                   String text = getArguments().getString("nfcResult");
                   Log.e("Dataxxxxxx","is: "+text);
                   *//*String strtext = getArguments().getString("edttext");
                Log.e("Dataxxxxxx","is: "+strtext);*//*
              *//*  Bundle extras = data.getExtras();
                String value;
                if (extras != null) {
                    value = extras.getString("nfcResult");
                    Log.e("Dataxxx","is: "+value);
                }*//*

            }
        }
    }*/
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        // String action = "android.nfc.action.TAG_DISCOVERED";

        System.out.println("inside resolveIntent"+action);

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            Log.d("rawMsgs", "rawMsgs" + rawMsgs);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                Log.d("msgs log", "in if");
                msgs = new NdefMessage[rawMsgs.length];
                //Log.d("msgs log", msgs.toString());
                //Toast.makeText(TagViewer.this, "msgs "+msgs, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                Log.d("msgs log", "in else");
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                Log.d("payload log", payload.toString());
                Toast.makeText(getActivity(), "payload" + payload.toString(), Toast.LENGTH_SHORT).show();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                Log.d("record log", record.toString());
                Toast.makeText(getActivity(), "records" + record.toString(), Toast.LENGTH_SHORT).show();
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            }
            // Setup the views
            buildTagViews(msgs);
        }
    }
    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        byte[] id = tag.getId();
        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");
        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                MifareClassic mifareTag = MifareClassic.get(tag);
                String type = "Unknown";
                switch (mifareTag.getType()) {
                    case MifareClassic.TYPE_CLASSIC:
                        type = "Classic";
                        break;
                    case MifareClassic.TYPE_PLUS:
                        type = "Plus";
                        break;
                    case MifareClassic.TYPE_PRO:
                        type = "Pro";
                        break;
                }
                sb.append("Mifare Classic type: ");
                sb.append(type);
                sb.append('\n');
                sb.append("Mifare size: ");
                sb.append(mifareTag.getSize() + " bytes");
                sb.append('\n');
                sb.append("Mifare sectors: ");
                sb.append(mifareTag.getSectorCount());
                sb.append('\n');
                sb.append("Mifare blocks: ");
                sb.append(mifareTag.getBlockCount());
            }
            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }
        return sb.toString();
    }



    private String getHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    private long getDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    private long getReversed(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }
    void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // LinearLayout content = mTagContent;
        RelativeLayout content = mTagContent;
        // Parse the first message in the list
        // Build views for all of the sub records
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        Log.d("size", "" + size);
        if (TextRecord.text != null) {
            if (TextRecord.text.trim().split("~").length > 0 && TextRecord.text.trim().split("~").length == 4) {
                // String[] respFieldDet = TextRecord.text.trim().split("~");
                System.out.println("TextRecord "+TextRecord.text);

                String[] respFieldDet = TextRecord.text.trim().split("~");

                System.out.println("respFieldDet "+respFieldDet);
                Log.e("Tag Read Result", " ");
            /*YovSEjn6PPxtN2Gvicowd5CDGU/IzGDaCB2nqN7gka9ZcQdSAbNWXncUbRqujLKDIWyDMtMbke5j
    EAJ8Lu68szkY5oQ+3UkyIJMxe1+1ui60fdL4dAylf9OLyDFm7uvOamZLtzpnqeoRXfZNTfykhkV2
    u4t+WccLdx9DNrHe9FWZngmn1QAXN4vibEslIuDyaxFlA1xbVfvYXAFjrB1sDNspiJm3vDQ1G9h1
    lSGgWSM=~Mahendra~Cookies~2*/
               /* userAccountDetails = respFieldDet[0];
                beneficiaryname = respFieldDet[1];
                edtbeneficiaryname.setText(respFieldDet[1]);
                productname = respFieldDet[2];
                edtproductname.setText(respFieldDet[2]);
                edtamount.setText(respFieldDet[3]);*/
            } else {
                Toast.makeText(getActivity(), "Please read again the Tag details", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(getActivity(), "Sorry your NFC TAG is Empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void showMessage(int title, int message) {
        mDialog.setTitle(title);
        mDialog.setMessage(getText(message));
        mDialog.show();
    }

    private NdefRecord newTextRecord(String text, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = text.getBytes(utfEncoding);
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }








    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        builder.create().show();
        return;
    }
    private class getPayerAddress extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... params) {

            resp=getPayers();
            return resp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("after calling async ", "afte asynctask5");

            System.out.println("Middleware data response " + s);
            if (s.equals("") || s == null || s.startsWith("Error")) {
                Toast.makeText(Insta_Pay.this.getActivity(), "No Data", Toast.LENGTH_LONG).show();
                return;
            } else {
                try {
                    JSONObject mainObj = new JSONObject(s);
                    // Status=mainObj.getString("opstatus");
                    // MiddlewareResponse=mainObj.getString("msg");
                    AddressList_Middleware = mainObj.getString("responseParameterMW");

                    System.out.println("get AddressList_Middleware " + AddressList_Middleware);

                    JSONObject innerObject = new JSONObject(AddressList_Middleware);

                    ResultMW = innerObject.getString("Result");
                    System.out.println("get ResultMW " + ResultMW);


                    JSONObject addresslistObject = new JSONObject(ResultMW);
                    AddresslistMW = addresslistObject.getString("responseParameter");
                    System.out.println("get AddresslistMW " + AddresslistMW);


                    JSONObject addresslistfinal = new JSONObject(AddresslistMW);

                    addresslist = addresslistfinal.getJSONArray("addresslist");
                    System.out.println("get addresslist " + addresslist.length());
                    for (int i = 0; i < addresslist.length(); i++) {
                        c = (String) addresslist.get(i);
                        System.out.println("get jsonAddress " + c);
                        stockList.add(c);
                    }

                    System.out.println("outside Addressitems " + Addressitems);


                    // String DataStringfilter=mainObj.toString().replace("\"", "\\\"");


                } catch (Exception e) {
                    String err = (e.getMessage() == null) ? "calling Dialog failed" : e.getMessage();

                }
                // adapter=new UPI_VirtualAddressListAdapter(SendMoney_UPI.this.getActivity(),Addressitems);
                AddressArr = stockList.toArray(AddressArr); // convert ArrayList to Array

                for(String es : AddressArr)

                    System.out.println("Address array " + es);



                try{
                    ArrayAdapter<String> ad = new ArrayAdapter<String>(Insta_Pay.this.getActivity(),android.R.layout.simple_spinner_item,AddressArr);
                    ad.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    PayerAddress.setAdapter(ad);
                }catch(NullPointerException np){
                    np.printStackTrace();
                }


                PayerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        int Item =  PayerAddress.getSelectedItemPosition();
                        SpinnerValue = PayerAddress.getSelectedItem().toString();
                        System.out.println("SpinnerValue selecteddd"+SpinnerValue+"Item Position "+Item);
                        appPref.getInstance().setString("SpinnerValue_upi", SpinnerValue);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });





                //   PayerAddress.setAdapter(adapter);
            }
        }
    }
    public JSONObject JsonsendMoneyData() {
        json = new JSONObject();
        try {
            json = new JSONObject();
            json.put("action", "PaymentAddress");
            json.put("subAction", "GetPaymentAddressList");
            //json.put("subAction", "GetPSPList");
            json.put("entityID", Constants.entityID_UPI);
            jsonInner = new JSONObject();
            jsonInner.put("entityID", Constants.entityID_UPI);
            jsonInner.put("mobileNo", MobileNo);
            // jsonInner.put("mobileNo", "9833214677");
            jsonInner.put("mpin", "1234");
            json.put("inputParam", jsonInner);
           /* mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }


    private String getPayers() {

        JSONObject banklist = JsonsendMoneyData();
        String DataString = banklist.toString().replace("\"", "\\\"");
        System.out.println("DataString into string" + DataString.toString().replace("\"", "\\\""));
        String resp = "";


        try {
            System.out.println("getPayers method");
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

    private class instaPay extends AsyncTask<String,String,String>{



        @Override
        protected String doInBackground(String... params) {

            System.out.println("inside instaPay doInbbackground ");
            resp=getinstaPay();
            return resp;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }



    public JSONObject JsonsetInstaPay() {
        json = new JSONObject();
        Amountstr = PayerAmount.getText().toString();
        PayeeAdd=Toview.getText().toString();
        PayerAddressStr= appPref.getInstance().getString("SpinnerValue_upi");
        CredNameinsta= appPref.getInstance().getString("cred_upi_insta");
        CredCodeinsta= appPref.getInstance().getString("keyCode_upi_insta");
        CredKeyIndexinsta= appPref.getInstance().getString("keyindex_upi_insta");
        CredDatainsta= appPref.getInstance().getString("credData_insta");
        appPrefAccountNo = appPref.getInstance().getString("SelectedAccountNo_upi");
        appPrefIFSC= appPref.getInstance().getString("SelectedIFSC_upi");
        System.out.println("cred_upi_tpin middleware"+CredNameinsta);
        System.out.println("Amountstr middleware"+Amountstr);
        System.out.println("keyCode_upi_tpin middleware"+CredCodeinsta);
        System.out.println("keyindex_upi_tpin middleware"+CredKeyIndexinsta);
        System.out.println("credData_tpin middleware"+CredDatainsta);
        //System.out.println("appPrefAccountNo middleware"+appPrefAccountNo);
        try {
            json = new JSONObject();
            json.put("action", "FundTransfer");
            json.put("subAction", "FundsTransfer");
            //json.put("subAction", "GetPSPList");
            // json.put("entityID", "mahb");
            json.put("entityID","infrapsp");
            jsonInner = new JSONObject();
            jsonInner.put("entityID", "infrapsp");
            jsonInner.put("mobileNo", MobileNo);
            jsonInner.put("payerName", "hemal");

            jsonInner.put("txnID", TransactionId_upi);
            jsonInner.put("txnType", "PAY");
            jsonInner.put("txnNote", "Req Pay");
            jsonInner.put("txnAmount", Amountstr);
            jsonInner.put("refID",TransactionId_upi);
            jsonInner.put("refUrl", "http://www.bankofmaharashtra.in/");

            jsonInner.put("payMode", "PAYMENTADDRESS");
            jsonInner.put("addressType", "ACCOUNT");
            jsonInner.put("payerAddr", PayerAddressStr);
            jsonInner.put("payeeAddr", PayeeAdd);
            jsonInner.put("accNum",appPrefAccountNo);
            jsonInner.put("ifsc", appPrefIFSC);

            jsonInner.put("accType","Savings");



            jsonInner.put("cardNum","");
            jsonInner.put("expDate", "");
            jsonInner.put("credType", "PIN");
            jsonInner.put("credSubType", CredNameinsta);
            jsonInner.put("credCode", CredCodeinsta);
            jsonInner.put("credKi", CredKeyIndexinsta);
            jsonInner.put("credData",CredDatainsta);




            json.put("inputParam", jsonInner);

            jsonDeviceInner = new JSONObject();


            jsonDeviceInner.put("mobileNo", MobileNo);

            jsonDeviceInner.put("lng", "12");
            jsonDeviceInner.put("lat", "32");
            jsonDeviceInner.put("location", "MUMBAI,MAHARASHTRA");
            jsonDeviceInner.put("ip", "10.10.10.10");
            jsonDeviceInner.put("deviceID", "12345666");
            jsonDeviceInner.put("os", "ANDROID");
            jsonDeviceInner.put("app", "NPCIAPP");
            jsonDeviceInner.put("capability", "10000000001");
            jsonInner.put("device", jsonDeviceInner);

           /* mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);*/
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getinstaPay() {

        System.out.println("inside getinstaPay ");

        String resp = "";
        JSONObject selectedbankaccountjson = JsonsetInstaPay();
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
            System.out.println("response from UPI for instapay "+resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
}

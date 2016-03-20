package com.infra.qrys_wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.infra.qrys_wallet.Models.VirtualAddressList;
import com.infra.qrys_wallet.Utils.Constants;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npci.upi.security.pinactivitycomponent.PinActivityComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstaPay_notification extends Activity implements View.OnClickListener{
    String SpinnerValue;
    ArrayList<VirtualAddressList> Addressitems;
    List<String> stockList = new ArrayList<String>();
    String[] AddressArr = new String[stockList.size()];
    JSONArray addresslist = null;
    public static String TransactionId_upi;
    private final String keyCode = "NPCI";
    EditText PayerAmount, PayerRemark, Toview;
    String strtext, Amount, To, Remark, ToRemarkView, resp;
    Button sendMoney, cancel;
    MWRequest mwRequest;
    SharedPreference appPref;
    JSONObject json, jsonInner, jsonDeviceInner;
    String MobileNo, cbsType, DeviceId, ToViewstr, Amountstr, PayeeAdd, CredDatainsta, CredKeyIndexinsta,AddresslistMW,
            appPrefAccountNo, appPrefIFSC, CredCodeinsta, c,CredNameinsta, PayerAddressStr,AddressList_Middleware,ResultMW;
    private String xmlPayloadString = "";
    private String credAllowedString = "";
    Spinner PayerAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insta_pay_notification);
        Toview = (EditText) findViewById(R.id.Toviewe);
        PayerAmount = (EditText) findViewById(R.id.Amountviewe);
        PayerRemark = (EditText) findViewById(R.id.Remarkviewe);
        sendMoney = (Button) findViewById(R.id.sendMoneyButton);
        cancel = (Button) findViewById(R.id.cancelpayment);
        PayerAddress = (Spinner) findViewById(R.id.sendmoneyfromspinner);
        TransactionId_upi = String.valueOf(new Random().nextInt(99999999));
        sendMoney.setOnClickListener(this);
        cancel.setOnClickListener(this);
        new getPayerAddress().execute();
        app_init();
        if(MessagesActivity.fromNotification) {
            Intent intent = getIntent();
            String OrginalString = intent.getStringExtra("PaymentData");
            OrginalString = OrginalString.substring(1,OrginalString.length()-1);
            /*from vinay*/
            // String msg ="Your standing instruction has been invoked. Pay Rs. 5000  to virtual address xyz@icici for Loan for Payment";
            String amountPattern = "standing instruction has been invoked. Pay Rs\\.\\s+(.*?)\\s+to"; //amnt
            String virtualAddressPattern = "to virtual address\\s+(.*?)\\s+for"; //address
            String remarksPattern = "for\\s+(.*)"; //address
            Pattern r = Pattern.compile(amountPattern);
            Matcher m = r.matcher(OrginalString);
            System.out.println("OrginalString: " + OrginalString);
            if(m.find()) {
                Amount = m.group(1);
                System.out.println("Amount: " + Amount);
            } else {
                System.out.println("NO MATCH");
            }
            r = Pattern.compile(virtualAddressPattern);
            m = r.matcher(OrginalString);
            if(m.find()) {
                To = m.group(1);
                System.out.println("Virtual Address: " + To);
            } else {
                System.out.println("NO MATCH");
            }
            r = Pattern.compile(remarksPattern);
            m = r.matcher(OrginalString);
            if(m.find()) {
                Remark = m.group(1);
                System.out.println("Remarks+++: " + Remark);
            } else {
                System.out.println("NO MATCH");
            }
        }
            /*vinay code ends*/

        // String OrginalString = getArguments().getString("PaymentData");
        //OrginalString= "Your standing instruction has been invoked. Pay INR 500  to virtual address xyz for Loan Payment"
        Toview.setText(To);
        PayerAmount.setText(Amount);
        PayerRemark.setText(Remark);
        MessagesActivity.fromNotification = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Result Code:", String.valueOf(resultCode));
        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            String errorMsgStr = data.getStringExtra("error");
            if(errorMsgStr != null && !errorMsgStr.isEmpty()) {
                Log.d("Error:", errorMsgStr);
                try {
                    JSONObject error = new JSONObject(errorMsgStr);
                    String errorCode = error.getString("errorCode");
                    String errorText = error.getString("errorText");
                    Toast.makeText(this.getApplicationContext(), errorCode + ":" + errorText, Toast.LENGTH_LONG).show();
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
            HashMap<String, String> credListHashMap = (HashMap<String, String>) data.getSerializableExtra("credBlocks");
            for(String cred : credListHashMap.keySet()) { // This will return the list of field name e.g mpin,otp etc...
                try {
                    JSONObject credBlock = new JSONObject(credListHashMap.get(cred));
                    Log.i("enc_msg", credBlock.toString());
                    // adapter.add("Control name : "+cred);
                    // adapter.add("keyId : " + credBlock.getJSONObject("data").getString("code"));
                    // adapter.add("key index : " + credBlock.getJSONObject("data").getString("ki"));
                    // adapter.add("Encrypted Message from Common Library: " + credBlock.getJSONObject("data").getString("encryptedBase64String"));
                    System.out.println("Control name : " + cred);
                    System.out.println("keyId : " + credBlock.getJSONObject("data").getString("code"));
                    System.out.println("key index : " + credBlock.getJSONObject("data").getString("ki"));
                    System.out.println("Encrypted Message from Common Library: : " + credBlock.getJSONObject("data").getString("encryptedBase64String"));
                    appPref.getInstance().setString("cred_upi_insta", cred);
                    appPref.getInstance().setString("keyCode_upi_insta", credBlock.getJSONObject("data").getString("code"));
                    appPref.getInstance().setString("keyindex_upi_insta", credBlock.getJSONObject("data").getString("ki"));
                    appPref.getInstance().setString("credData_insta", credBlock.getJSONObject("data").getString("encryptedBase64String"));
                    // new instaPayment().execute();
                    new instaPay().execute();
                    Intent callSuccessReceipt = new Intent(InstaPay_notification.this,By_Notification_Success.class);
                    callSuccessReceipt.putExtra("transactionRefId", TransactionId_upi);
                    startActivity(callSuccessReceipt);
                    finish();
                    // UPI_CommonLibraryCall();

                   /* UPI_SelectedBankAccount upi_selectedBankAccount=new UPI_SelectedBankAccount();
                    fragmentManager=getFragmentManager();
                    ft=fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container,upi_selectedBankAccount);
                    ft.commit();*/
                    Log.i("enc_msg", credBlock.getJSONObject("data").getString("encryptedBase64String"));
                    //Log.i("enc_msg", credBlock.getString("message"));
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void app_init() {
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
            System.out.println("response from UPI for INstaPay " + resp);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    public JSONObject JsonsetInstaPay() {
        json = new JSONObject();
        Amountstr = PayerAmount.getText().toString();
        PayeeAdd = Toview.getText().toString();
        PayerAddressStr = appPref.getInstance().getString("SpinnerValue_upi");
        CredNameinsta = appPref.getInstance().getString("cred_upi_insta");
        CredCodeinsta = appPref.getInstance().getString("keyCode_upi_insta");
        CredKeyIndexinsta = appPref.getInstance().getString("keyindex_upi_insta");
        CredDatainsta = appPref.getInstance().getString("credData_insta");
        appPrefAccountNo = appPref.getInstance().getString("SelectedAccountNo_upi");
        appPrefIFSC = appPref.getInstance().getString("SelectedIFSC_upi");
        System.out.println("cred_upi_tpin middleware" + CredNameinsta);
        System.out.println("Amountstr middleware" + Amountstr);
        System.out.println("keyCode_upi_tpin middleware" + CredCodeinsta);
        System.out.println("keyindex_upi_tpin middleware" + CredKeyIndexinsta);
        System.out.println("credData_tpin middleware" + CredDatainsta);
        //System.out.println("appPrefAccountNo middleware"+appPrefAccountNo);
        try {
            json = new JSONObject();
            json.put("action", "FundTransfer");
            json.put("subAction", "FundsTransfer");
            //json.put("subAction", "GetPSPList");
            // json.put("entityID", "mahb");
            json.put("entityID", "infrapsp");
            jsonInner = new JSONObject();
            jsonInner.put("entityID", "infrapsp");
            jsonInner.put("mobileNo", MobileNo);
            jsonInner.put("payerName", "hemal");
            jsonInner.put("txnID", TransactionId_upi);
            jsonInner.put("txnType", "PAY");
            jsonInner.put("txnNote", "Req Pay");
            jsonInner.put("txnAmount", Amountstr);
            jsonInner.put("refID", TransactionId_upi);
            jsonInner.put("refUrl", "http://www.bankofmaharashtra.in/");
            jsonInner.put("payMode", "PAYMENTADDRESS");
            jsonInner.put("addressType", "ACCOUNT");
            jsonInner.put("payerAddr", PayerAddressStr);
            jsonInner.put("payeeAddr", PayeeAdd);
            jsonInner.put("accNum", appPrefAccountNo);
            jsonInner.put("ifsc", appPrefIFSC);
            jsonInner.put("accType", "Savings");
            jsonInner.put("cardNum", "");
            jsonInner.put("expDate", "");
            jsonInner.put("credType", "PIN");
            jsonInner.put("credSubType", CredNameinsta);
            jsonInner.put("credCode", CredCodeinsta);
            jsonInner.put("credKi", CredKeyIndexinsta);
            jsonInner.put("credData", CredDatainsta);
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
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.sendMoneyButton:
                Amountstr = PayerAmount.getText().toString();
                ToViewstr = Toview.getText().toString();
                ToRemarkView = PayerRemark.getText().toString();
                TransactionId_upi = String.valueOf(new Random().nextInt(99999999));
                System.out.println("TransactionId_upi " + TransactionId_upi);//this is transaction ref ID.
                Intent intent = new Intent(this, PinActivityComponent.class);
                // Create Keycode
                intent.putExtra("keyCode", keyCode);
                // Create xml payload
                if(xmlPayloadString.isEmpty()) {
                    Toast.makeText(this, "XML List Key API is not loaded.", Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra("keyXmlPayload", xmlPayloadString);  // It will get the data from list keys API response
                // Create Controls
                if(credAllowedString.isEmpty()) {
                    Toast.makeText(this, "Required Credentials could not be loaded.", Toast.LENGTH_LONG).show();
                    return;
                }
                intent.putExtra("controls", credAllowedString);
                // Create Configuration
                try {
                    JSONObject configuration = new JSONObject();
                    configuration.put("payerBankName", "Bank Of maharashtra");
                    configuration.put("backgroundColor", "#FFFFFF");
                    Log.i("configuration", configuration.toString());
                    intent.putExtra("configuration", configuration.toString());
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                // Create Salt
                try {
                    JSONObject salt = new JSONObject();
                    salt.put("txnId", TransactionId_upi);
                    // Log.i("txnId: ", TransactionId_upi);
                    salt.put("txnAmount", "100");
                    salt.put("deviceId", "74235ae00124fab8");
                    salt.put("appId", this.getPackageName());
                    Log.i("salt", salt.toString());
                    intent.putExtra("salt", salt.toString());
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                // Create Pay Info
                JSONArray payInfoArray = new JSONArray();
                try {
                    JSONObject jsonPayeeName = new JSONObject();
                    jsonPayeeName.put("name", "payeeName");
                    jsonPayeeName.put("value", SpinnerValue + "@infrapsp");
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
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
                // Create Language Pref
                intent.putExtra("languagePref", "en_US");
                startActivityForResult(intent, 1);
                break;
            case R.id.cancelpayment:
                this.finish();
                break;
            default:
                break;
        }
    }

    private class instaPay extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            System.out.println("inside instaPay doInbbackground ");
            resp = getinstaPay();
            return resp;
        }
    }
    //payerAddress
    private class getPayerAddress extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            resp = getPayers();
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
            if(s.equals("") || s == null || s.startsWith("Error")) {
                Toast.makeText(InstaPay_notification.this, "No Data", Toast.LENGTH_LONG).show();
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
                    for(int i = 0; i < addresslist.length(); i++) {
                        c = (String) addresslist.get(i);
                        System.out.println("get jsonAddress " + c);
                        stockList.add(c);
                    }

                } catch(Exception e) {
                    String err = (e.getMessage() == null) ? "calling Dialog failed" : e.getMessage();
                }
                // adapter=new UPI_VirtualAddressListAdapter(SendMoney_UPI.this.getActivity(),Addressitems);
                AddressArr = stockList.toArray(AddressArr); // convert ArrayList to Array
                for(String es : AddressArr)
                    System.out.println("Address array " + es);
                try {
                    ArrayAdapter<String> ad = new ArrayAdapter<String>(InstaPay_notification.this, android.R.layout.simple_spinner_item, AddressArr);
                    ad.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    PayerAddress.setAdapter(ad);
                } catch(NullPointerException np) {
                    np.printStackTrace();
                }
                PayerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int Item = PayerAddress.getSelectedItemPosition();
                        SpinnerValue = PayerAddress.getSelectedItem().toString();
                        System.out.println("SpinnerValue selecteddd" + SpinnerValue + "Item Position " + Item);
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
            System.out.println("response from UPI getUPILIST " + resp);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return resp;
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
            jsonInner.put("mobileNo", "9867008241");
            // jsonInner.put("mobileNo", "9833214677");
            jsonInner.put("mpin", "1234");
            json.put("inputParam", jsonInner);
           /* mwRequest = new MWRequest();
            resp = mwRequest.middleWareReq(json);*/
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}

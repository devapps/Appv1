package com.infra.qrys_wallet.Fragments_UPI;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.npci.upi.security.pinactivitycomponent.PinActivityComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommanLibrary_UPI extends Fragment {


    private final String keyCode="NPCI";
    Button button;
    TextView textResult;
    EditText transactionID;
    EditText transactionAmount;
    Spinner spinner;
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    private ListView responseListView;
    private String xmlPayloadString="";
    private String credAllowedString = "";
    //HttpClient httpClient;
    View rootView;

    public CommanLibrary_UPI() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_comman_library__upi, container, false);
        app_init(rootView);
        return rootView;
    }

    public void app_init(View rootView){

        // Creating API Responses

        /* This will be obtained from the response of ListKeys API */
        /*xmlPayloadString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:RespListKeys xmlns:ns2=\"http://npci.org/upi/schema/\"><Head msgId=\"5skv5ptRj1ucFTehlU7\" orgId=\"NPCI\" ts=\"2015-12-21T16:18:24+05:30\" ver=\"1.0\"/><Resp reqMsgId=\"d0kSkoT7quU6DDYk\" result=\"SUCCESS\"/><Txn id=\"d0kSkoT7quU7RaAV\" ts=\"2015-12-21T16:18:13+05:30\"/><keyList><key code=\"NPCI\" ki=\"20150822\" owner=\"NPCI\" type=\"PKI\"><keyValue xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAzPA1EH+aCZv/rly4zgRtfgRNr+6Xtp4iTWHEA36WaSia55gyrOAT0UJOtX9nG/NZ77wFzUxhrHuczh3lVO8/ylXh1wpcRsBPLNfg1qXzaU8c7JLk7amD4cV4re1LkqZfOOrri21na9p7Ybw8v9mC/q7xfF3gzySczaq8SG1NCQIDAQAB</keyValue></key></keyList><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>fXZgHY4Zkf0WD1dtzfR1x40lcre2aV3m83+96s5e4Fw=</DigestValue></Reference></SignedInfo><SignatureValue>OF46FU9WLGKCWCUQwt0mKWBOt0diHvtkH2puvTjU3usgCc4dOYMtCQp5neE3wnM+e3UE/kwMvpSN\n"+
                "A8WFwYxr/geQW9BcL74cG1mXiI7qTL+jf6nk6lXpbJPyL8cd0o/Lyp6ckMPWnNOrdf4rAqAjKLTw\n"+
                //"A9WFwYxr/geQW9BcL74cG1nXiI7qTL+jf6nk6lXpbJPyL8cd0o/Lyp6ckMPWnNOrdf4rAqAjKLTw\n"+
                "o09kS2AHo3QxDpFwCBfoeNpxmmnfCIK+OeoEFUGj2IUDsjaF+3DNUg3B4it72B5Auu/uFkS7OIra\n"+
                "enJzrUacNy1aiw+OmMp4W7WyqPMFPYGBRd9Sa5rlytDB7D7Ku9mUEp4XpnvyNCZxRfyCqPEDcwW3\n"+
                "nuB6U+/10GswhKJRn6L+oXWuNFDHS/BsmD5JBQ==</SignatureValue><KeyInfo><KeyValue><RSAKeyValue><Modulus>01DqzBsJTyMHT2S9MK5AIyFXNU646kwiOK3uymXIy9EW0nRKNKRkeIRTlGwX4wEnymGtGgX5B/Ij\n"+
                "1elkLN4VJ9GplDV+wf0Lp2i2q4E6uRiWIzsqq42MCQgv8Fq/IPqjqPbeP9yh/8YPmBiMehBmhQd3\n"+
                "qzl77C03k6d0yBIO5q/zXneTK9uFBNEL5yNpukrLGBcf3b9VHsjXpEaQrxGSMHCgNWpQgXpEcBr5\n"+
                "OJ0/XxWbgMCZMlkYe1d6gswjuCRZ/xxJwEfbSO5AsnPtyqxSIjyhgEi9REtYnzaWwOBN4JCqt0pM\n"+
                "L0ja23lUwVJuNwkwNGKBXvkGoXUln8Sf7PIv7w==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue></KeyValue></KeyInfo></Signature></ns2:RespListKeys>";
        Log.i("xml_payload",xmlPayloadString);*/
        xmlPayloadString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns2:RespListKeys xmlns:ns2=\"http://npci.org/upi/schema/\"><Head msgId=\"1GRDBknspOwrBRjZpXT3\" orgId=\"NPCI\" ts=\"2016-02-18T15:51:40+05:30\" ver=\"1.0\"/><Resp reqMsgId=\"MAHB14VWPQY4OI\" result=\"SUCCESS\"/><Txn id=\"MAHB2V5GR9EHHF\" note=\"payment\" refId=\"testing\" refUrl=\"https://mahb.com\" ts=\"2016-02-18T16:01:51+05:30\" type=\"ListKeys\"/><keyList><key code=\"NPCI\" ki=\"20150822\" owner=\"NPCI\" type=\"PKI\"><keyValue xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4rIIEHkJ2TYgO/JUJQI/sxDgbDEAIuy9uTf4DItWeIMsG9AuilOj9R+dwAv8S6/9No/z0cwsw4UnsHQG1ALVIxFznLizMjaVJ7TJ+yTS9C9bYEFakRqH8b4jje7SC7rZ9/DtZGsaWaCaDTyuZ9dMHrgcmJjeklRKxl4YVmQJpzYLrK4zOpyY+lNPBqs+aiwJa53ZogcUGBhx/nIXfDDvVOtKzNb/08U7dZuXoiY0/McQ7xEiFcEtMpEJw5EB4o3RhE9j/IQOvc7l/BfD85+YQ5rJGk4HUb6GrQXHzfHvIOf53l1Yb0IX4v9q7HiAyOdggO+PVzXMSbrcFBrEjGZD7QIDAQAB</keyValue></key></keyList><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>4wahq5ABCdy9IeqHiq6wTWIf+hqlha+VP3ieAYDk+eM=</DigestValue></Reference></SignedInfo><SignatureValue>tVgv11X9c3G+kofnn912X6PHps6hDM13BABtLQQAG76tu+nOhKnoHGAxInalEKRFif2ep01aDLEZyxl8liydmg0mSjAAb4B/EezoxgJbX3aq+8za/dbS0rqubejZrjwZu3ioMrpDwFCXIcftsSjCz+QIpdf8a9jmavzBaEOLj/Tpol0JUQzASiPxnLtZp6YzF2DLF5G2cCaDtUvLOkE5yixXxLRy5C/FmKBLdhwxcqc0V0xbr/epdweSP9YL2CVXKfyhJgCdPS0rHusc7MQF8o4KlzSGga+421AWSWkQ9epizV1GESdhutgmybfFaFZ3vnl6I3nuvt9vTWwm00FJnA==</SignatureValue><KeyInfo><KeyValue><RSAKeyValue><Modulus>01DqzBsJTyMHT2S9MK5AIyFXNU646kwiOK3uymXIy9EW0nRKNKRkeIRTlGwX4wEnymGtGgX5B/Ij1elkLN4VJ9GplDV+wf0Lp2i2q4E6uRiWIzsqq42MCQgv8Fq/IPqjqPbeP9yh/8YPmBiMehBmhQd3qzl77C03k6d0yBIO5q/zXneTK9uFBNEL5yNpukrLGBcf3b9VHsjXpEaQrxGSMHCgNWpQgXpEcBr5OJ0/XxWbgMCZMlkYe1d6gswjuCRZ/xxJwEfbSO5AsnPtyqxSIjyhgEi9REtYnzaWwOBN4JCqt0pML0ja23lUwVJuNwkwNGKBXvkGoXUln8Sf7PIv7w==</Modulus><Exponent>AQAB</Exponent></RSAKeyValue></KeyValue></KeyInfo></Signature></ns2:RespListKeys>";

       // xmlPayloadString =""


        /* This will be obtained from the response of ListAccountResponse API */
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


        textResult = (TextView) rootView.findViewById(R.id.textView2);
        transactionID = (EditText) rootView.findViewById(R.id.editTransactionID);

        //Set a dynamic Transaction ID
        transactionID.setText(String.valueOf(new Random().nextInt(99999999)));

        transactionAmount =(EditText) rootView.findViewById(R.id.editTransactionAmount);

        // For response view
        responseListView =(ListView) rootView.findViewById(R.id.listitem_fragment);
        spinner = (Spinner) rootView.findViewById(R.id.fragment_spinner);
        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);
        responseListView.setAdapter(adapter);

       /* ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.controls_arrays, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        Log.i("Spinner", spinner.getSelectedItem().toString());*/

        //Pay button
        button = (Button) rootView.findViewById(R.id.button_fragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    salt.put("txnId", transactionID.getText().toString());
                    Log.i("txnId: ",transactionID.getText().toString() );
                    salt.put("txnAmount", transactionAmount.getText().toString());
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
                    jsonPayeeName.put("value", "John Smith");
                    payInfoArray.put(jsonPayeeName);

                    JSONObject jsonNote = new JSONObject();
                    jsonNote.put("name", "note");
                    jsonNote.put("value", "Pay restaurant bill");
                    payInfoArray.put(jsonNote);

                    JSONObject jsonRefId = new JSONObject();
                    jsonRefId.put("name", "refId");
                    jsonRefId.put("value", transactionID.getText().toString());
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
                    adapter.add("Control name : "+cred);
                    adapter.add("keyId : " + credBlock.getJSONObject("data").getString("code"));
                    adapter.add("key index : " + credBlock.getJSONObject("data").getString("ki"));
                    adapter.add("Encrypted Message from Common Library: " + credBlock.getJSONObject("data").getString("encryptedBase64String"));

                    Log.i("enc_msg", credBlock.getJSONObject("data").getString("encryptedBase64String"));
                    //Log.i("enc_msg", credBlock.getString("message"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}

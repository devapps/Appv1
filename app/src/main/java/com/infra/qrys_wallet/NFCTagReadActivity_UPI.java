package com.infra.qrys_wallet;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.infra.qrys_wallet.Utils.NdefMessageParser;
import com.infra.qrys_wallet.Utils.ParsedNdefRecord;
import com.infra.qrys_wallet.Utils.SharedPreference;
import com.infra.qrys_wallet.Utils.TextRecord;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

/**
 * Created by vishvendu.palawat on 24-11-2015.
 */
public class NFCTagReadActivity_UPI extends AppCompatActivity {

    SharedPreference appPref;
    String nfcAction;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo, fNAME, lNAME, custFullName, bankName;
    private RelativeLayout mTagContent;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    private AlertDialog mDialog;
    private FragmentTransaction ft;
    private FragmentManager fragmentManager;
    public static boolean CheckNFCScanrevertPosition=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfcsplashscreen);
        /*All Session Variable from Shared Preferences*/
        appPref.getInstance().Initialize(NFCTagReadActivity_UPI.this);
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
        System.out.println("SharedPreference Details In NFC Tag Read Fragment \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode + " " + fNAME + " " + lNAME + " \n " + custFullName + " " + bankName);

        resolveIntent(getIntent());
        mDialog = new AlertDialog.Builder(NFCTagReadActivity_UPI.this).setNeutralButton("Ok", null).create();
        mAdapter = NfcAdapter.getDefaultAdapter(NFCTagReadActivity_UPI.this);
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            NFCTagReadActivity_UPI.this.finish();
        }
        mPendingIntent = PendingIntent.getActivity(NFCTagReadActivity_UPI.this, 0,
                new Intent(NFCTagReadActivity_UPI.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[]{
                newTextRecord(
                        "Message from NFC Reader :-)", Locale.ENGLISH, true)});
    }

    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
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
                Toast.makeText(NFCTagReadActivity_UPI.this, "payload" + payload.toString(), Toast.LENGTH_SHORT).show();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                Log.d("record log", record.toString());
                Toast.makeText(NFCTagReadActivity_UPI.this, "records" + record.toString(), Toast.LENGTH_SHORT).show();
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            }
            // Setup the views
            buildTagViews(msgs);
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

    void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        // LinearLayout content = mTagContent;
        RelativeLayout content = mTagContent;
        // Parse the first message in the list
        // Build views for all of the sub records
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        Log.d("size", "" + size);
        if (TextRecord.text != null) {
            System.out.println("TextRecord.text " + TextRecord.text);
              //  String[] respFieldDet = TextRecord.text.trim().split("~");
                Log.e("Tag Read Result", " ");
            appPref.getInstance().setString("nfcdata", TextRecord.text);
                /*Intent i = new Intent(NFCTagReadActivity_UPI.this,SendMoney_UPI.class);
                startActivity(i);*/
            CheckNFCScanrevertPosition=true;
            System.out.println("Inside CheckNFCScanrevertPosition " +CheckNFCScanrevertPosition);
            this.finish();
         /*   Bundle bundle = new Bundle();
            bundle.putString("nfcResult", TextRecord.text);
            // set Fragmentclass Arguments
            SendMoney_UPI fragobj = new SendMoney_UPI();
            fragobj.setArguments(bundle);
            fragmentManager = getSupportFragmentManager();
            ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frame_container, fragobj);
            // ft.addToBackStack("fragback");
            ft.commit();*/
           /* Intent result = new Intent();
            result.putExtra("nfcResult",TextRecord.text);
            setResult(Activity.RESULT_OK,result);
*/
          /*  CheckNFCScanrevertPosition=true;
            SendMoney_UPI sendMoney_upi= new SendMoney_UPI();
            fragmentManager = getSupportFragmentManager();
            // getFragmentManager().popBackStack();
            ft = fragmentManager.beginTransaction();
            ft.replace(R.id.frame_container, sendMoney_upi);
            // ft.addToBackStack("fragback");
            ft.commit();*/

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
            Toast.makeText(NFCTagReadActivity_UPI.this.getApplicationContext(), "Sorry your NFC TAG is Empty", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
            mAdapter.disableForegroundNdefPush(this);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
        }
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.create().show();
        return;
    }








}

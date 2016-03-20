package com.infra.qrys_wallet.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.MWRequest;
import com.infra.qrys_wallet.Utils.NdefMessageParser;
import com.infra.qrys_wallet.Utils.ParsedNdefRecord;
import com.infra.qrys_wallet.Utils.SharedPreference;
import com.infra.qrys_wallet.Utils.TextRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
/**
 * Created by sandeep.devhare on 16-11-2015.
 */
public class NfcTagRead extends Fragment implements View.OnClickListener, View.OnTouchListener {
    /*Variable Declarations*/
    private RelativeLayout mTagContent;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    private NdefMessage mNdefPushMessage;
    EditText edtamount, edtremark, edtbeneficiaryname, edtproductname;
    String amount, remark, beneficiaryname, productname, remetresMobileNO, userAccountDetails;
    Button cnfrm, rescan;
    private AlertDialog mDialog;
    ImageView back;
    ProgressDialog prgDialog;
    JSONObject json, jsonInner;
    String requestUrl, strLine, opStatus, result;
    MWRequest mwRequest;
    private FragmentManager fragmentManager;
    private WeakReference<NFCTransaction> asyncTaskWeakRef;
    SharedPreference appPref;
    String nfcAction;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo,fNAME, lNAME, custFullName,bankName;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null && args.containsKey("nfcAction") ) {
            nfcAction = args.getString("nfcAction");

            System.out.println("nfcAction " + nfcAction);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        View rootView = inflater.inflate(R.layout.nfc_tag_read, container, false);
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
        System.out.println("SharedPreference Details In NFC Tag Read Fragment \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode+" "+fNAME+" "+lNAME+" \n "+custFullName+" "+bankName);

        cnfrm = (Button) rootView.findViewById(R.id.confirm);
        rescan = (Button) rootView.findViewById(R.id.rescan);
        back = (ImageView) rootView.findViewById(R.id.imgback);

        edtamount = (EditText) rootView.findViewById(R.id.edtamount);
        edtremark = (EditText) rootView.findViewById(R.id.edtremark);
        edtbeneficiaryname = (EditText) rootView.findViewById(R.id.edtbeneficiaryname);
        edtproductname = (EditText) rootView.findViewById(R.id.edtproduct);

        appPref.getInstance().Initialize(NfcTagRead.this.getActivity().getApplicationContext());

        cnfrm.setOnClickListener(this);
        rescan.setOnClickListener(this);
        back.setOnClickListener(this);
        Log.d(NfcTagRead.class.getName(), "Calling Method test 0");
       resolveIntent(NfcTagRead.this.getActivity().getIntent());
       // resolveIntent();


        mDialog = new AlertDialog.Builder(NfcTagRead.this.getActivity()).setNeutralButton("Ok", null).create();
        mAdapter = NfcAdapter.getDefaultAdapter(NfcTagRead.this.getActivity());
        if (mAdapter == null) {
            showMessage(R.string.error, R.string.no_nfc);
            NfcTagRead.this.getActivity().finish();
        }

        mPendingIntent = PendingIntent.getActivity(NfcTagRead.this.getActivity(), 0,
                new Intent(NfcTagRead.this.getActivity(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mNdefPushMessage = new NdefMessage(new NdefRecord[]{
                newTextRecord(
                        "Message from NFC Reader :-)", Locale.ENGLISH, true)});
        return rootView;
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

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null) {
            if (!mAdapter.isEnabled()) {
                showWirelessSettingsDialog();
            }
            mAdapter.enableForegroundDispatch(NfcTagRead.this.getActivity(), mPendingIntent, null, null);
            mAdapter.enableForegroundNdefPush(NfcTagRead.this.getActivity(), mNdefPushMessage);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(NfcTagRead.this.getActivity());
            mAdapter.disableForegroundNdefPush(NfcTagRead.this.getActivity());
        }
    }

    private void showWirelessSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NfcTagRead.this.getActivity());
        builder.setMessage(R.string.nfc_disabled);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                NfcTagRead.this.getActivity().finish();
            }
        });
        builder.create().show();
        return;
    }


    public void  resolveIntent(Intent intent) {
       // intent = this.getActivity().getIntent();

        String action = intent.getAction();
        Log.d("msgs log",""+action);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(NfcTagRead.this.getActivity().getIntent().getAction())
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(NfcTagRead.this.getActivity().getIntent().getAction())
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(NfcTagRead.this.getActivity().getIntent().getAction())) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            Log.d("rawMsgsmsgs log",""+rawMsgs);
            NdefMessage[] msgs;
            if (rawMsgs != null) {
                Log.d("msgs log","in if");
                msgs = new NdefMessage[rawMsgs.length];
                //Log.d("msgs log", msgs.toString());
                //Toast.makeText(TagViewer.this, "msgs "+msgs, Toast.LENGTH_SHORT).show();
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                Log.d("msgs log","in else");
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                Log.d("payload log", payload.toString());
                Toast.makeText(NfcTagRead.this.getActivity(), "payload"+payload.toString(), Toast.LENGTH_SHORT).show();

                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                Log.d("record log", record.toString());
                Toast.makeText(NfcTagRead.this.getActivity(), "records"+record.toString(), Toast.LENGTH_SHORT).show();
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
            }
            // Setup the views
            buildTagViews(msgs);
        }
    }

    private String dumpTagData(Parcelable p) {
        StringBuilder sb = new StringBuilder();
        Tag tag = (Tag) p;
        if(tag!=null)
        {
            byte[] id = tag.getId();

            sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
            sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
            sb.append("ID (reversed): ").append(getReversed(id)).append("\n");


            sb.append("Technologies: ");
            System.out.println("Append  "+sb);
        }

        String prefix = "android.nfc.tech.";
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
        LayoutInflater inflater = LayoutInflater.from(NfcTagRead.this.getActivity());
        // LinearLayout content = mTagContent;
        RelativeLayout content = mTagContent;

        // Parse the first message in the list
        // Build views for all of the sub records
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        Log.d("size of a tag", "" + size);

        if (TextRecord.text.trim().split("~").length > 0 && TextRecord.text.trim().split("~").length == 6) {
            String[] respFieldDet = TextRecord.text.trim().split("~");
            Log.e("Tag Read Result"," ");
            /*iCSPfFWC9+rptPgfZatH8j8duzGD95qIW2wLS0FtdWcxDUNzhxMvah4BgpNBrP0IgYEfeFPmSCx9d7nQPwNuUxJArheqQfRnzy6Jh9nyvrz+CvK7HHjkhfewlzCw6xoYGpJVKGFj9Mwa1HASPHxCxCKYEa57kPnFaQPLzKOfR7U=
            ~Mahendra
            ~Cookies
            ~2*/
            userAccountDetails = respFieldDet[0];
            beneficiaryname = respFieldDet[1];
            edtbeneficiaryname.setText(respFieldDet[1]);
            productname = respFieldDet[2];
            edtproductname.setText(respFieldDet[2]);

            edtamount.setText(respFieldDet[3]);
        } else {
            Toast.makeText(NfcTagRead.this.getActivity().getApplicationContext(), "Please read again the Tag details", Toast.LENGTH_SHORT).show();
            return;
        }
    }


//    @Override
//    public void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//      setIntent(intent);
//        resolveIntent(intent);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                amount = edtamount.getText().toString();
                remark = edtremark.getText().toString();
                Dialog();
               /* NFCTransaction NFCTrancationCall = new NFCTransaction(NfcTagRead.this);
                asyncTaskWeakRef = new WeakReference<NFCTransaction>(NFCTrancationCall);
                NFCTrancationCall.execute();*/

                break;
            case R.id.rescan:
                NFCsplashScreen nfcTag = new NFCsplashScreen();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, nfcTag);
                ft.commit();
                break;
            case R.id.imgback:
                Wallet homeFragment = new Wallet();
                fragmentManager = getFragmentManager();
                FragmentTransaction ftt = fragmentManager.beginTransaction();
                ftt.replace(R.id.frame_container, homeFragment);
                ftt.addToBackStack("fragback");
                ftt.commit();
                break;
            default:
                return;

        }
    }

    private boolean isAsyncTaskPendingOrRunning() {
        return this.asyncTaskWeakRef != null &&
                this.asyncTaskWeakRef.get() != null &&
                !this.asyncTaskWeakRef.get().getStatus().equals(AsyncTask.Status.FINISHED);
    }

    public class NFCTransaction extends AsyncTask<String, Void, String> {
        private WeakReference<NfcTagRead> fragmentWeakRef;

        private NFCTransaction(NfcTagRead fragment) {
            this.fragmentWeakRef = new WeakReference<NfcTagRead>(fragment);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog = new ProgressDialog(NfcTagRead.this.getActivity());
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
                Toast.makeText(NfcTagRead.this.getActivity(), "Unable to reach server", Toast.LENGTH_LONG).show();
                return;
            } else {
                try {
                    Log.d("after calling async ", "afte asynctask5");
                    //sendJsondAta(s);
                    JSONObject mainObj = new JSONObject(s);
                    String respParams = mainObj.getString("responseParameter");
                    JSONObject respObj = new JSONObject(respParams);
                    opStatus = respObj.getString("opstatus");
                    Log.d("opstatus", opStatus);
                    result = respObj.getString("Result");
                    Log.d("result", result);
                    Dialog();
                    //Toast.makeText(SendMoneyFragment.this.getActivity(), " " + result, Toast.LENGTH_LONG).show();
                    prgDialog.dismiss();
                } catch (Exception e) {
                    Log.d("adapter", e.getMessage());
                    prgDialog.dismiss();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String resp = doTransaction();
            return resp;
        }

        private String doTransaction() {
            URL url = null;
            String resp = "";
            appPref.getInstance().Initialize(getActivity());
            String DEVICE_ID = appPref.getInstance().getString("Device_Id");
            remetresMobileNO = appPref.getInstance().getString("user_mobile");
            try {
                json = new JSONObject();
                json.put("ACTION", "");
                json.put("subActionId", "QRYSIMPSP2AFUNDTRANSFER");
                json.put("entityId", "QRYS");
                jsonInner = new JSONObject();
                jsonInner.put("entityId", "QRYS");
                jsonInner.put("cbsType", cbsType);


                jsonInner.put("MobileNo", remetresMobileNO);
                jsonInner.put("DeviceId", DEVICE_ID);
                jsonInner.put("txn_amount", amount);
                jsonInner.put("remarks", remark);
                jsonInner.put("benefName", beneficiaryname);
                jsonInner.put("Product", productname);
                jsonInner.put("QR_scan_Val", userAccountDetails);


                // jsonInner.put("beneficiaryMobileNo", strmobileno);
                json.put("map", jsonInner);
                System.out.println("json----------->" + json.toString());
                mwRequest = new MWRequest();
                resp = mwRequest.middleWareReq(json);
                System.out.println("response b4 return " + resp);
            }/* catch (MalformedURLException e) {
                e.printStackTrace();
            }*/ catch (JSONException e) {
                e.printStackTrace();
            }
            return resp;
        }
    }

    private void Dialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_paymentdone);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setAttributes(lp);
        TextView tvSelectedBank = (TextView) dialog.findViewById(R.id.tvbankselected);
        tvSelectedBank.setText(" Do you want to confirm ? ");
        dialog.findViewById(R.id.btncancel).setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
        dialog.findViewById(R.id.otpok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent i = new Intent(getActivity(), DashBoardWallet.class);
                startActivity(i);

            }
        });
        dialog.show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}

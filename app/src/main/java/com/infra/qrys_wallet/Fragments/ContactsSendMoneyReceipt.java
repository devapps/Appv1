package com.infra.qrys_wallet.Fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.SharedPreference;

/**
 * Created by sandeep.devhare on 18-11-2015.
 */
public class ContactsSendMoneyReceipt extends Fragment implements View.OnClickListener {
    TextView txtStatus,txtTxnIdNo,txtSendDetails;
    Button resendMoney;
    ImageView paid,fail;
    SharedPreference appPref;
    String userAccountNo, shortBankName, cbsType, ifscCode, mmidCode, DeviceId, userMobileNo,fNAME, lNAME, custFullName,bankName;
  String StatusMsg,txnIDNo,amountSend,beneficiaryName,Status;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();

        if (args != null && args.containsKey("statusMsg") && args.containsKey("Status") ) {
            StatusMsg = args.getString("statusMsg");
            Status = args.getString("Status");
            System.out.println("Status Msg "+StatusMsg);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        View rootView = inflater.inflate(R.layout.contactsendmoney_receipt, container, false);
        paid = (ImageView)rootView.findViewById(R.id.imgnfctag);
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
        System.out.println("SharedPreference Details In SendMoney Receipt Fragment \n" + userMobileNo + " " + DeviceId + " " + shortBankName + " \n" + cbsType + " " + userAccountNo + " " + mmidCode + "\n " + ifscCode + " " + fNAME + " " + lNAME + " \n " + custFullName + " " + bankName);

        txtStatus = (TextView)rootView.findViewById(R.id.tvstatus);
        txtTxnIdNo = (TextView)rootView.findViewById(R.id.tvtxnid);
        txtSendDetails = (TextView)rootView.findViewById(R.id.tvtxndetailsmsg);

        resendMoney = (Button) rootView.findViewById(R.id.btnresend);
        resendMoney.setOnClickListener(this);
        txtStatus.setText(Status);
        if(Status.equals("Payment Successful"))
        {
            paid.setImageResource(R.drawable.success);
            resendMoney.setVisibility(View.GONE);
        }else if(Status.equals("Payment Failed")) {
            paid.setImageResource(R.drawable.ic_faild);
            resendMoney.setVisibility(View.VISIBLE);
        }
        //txtTxnIdNo.setText(txnIDNo);
        txtSendDetails.setText(StatusMsg);


    return rootView;
    }
        @Override
    public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btnresend:
                    Contacts phoneBook = new Contacts();
                    fragmentManager = getFragmentManager();
                    ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container, phoneBook);
                    ft.commit();
                    break;
                default:
                    return;
            }
    }
}

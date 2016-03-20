package com.infra.qrys_wallet.Utils;

/**
 * Created by sandeep.devhare on 27-11-2015.
 */
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.infra.qrys_wallet.Fragments.Contacts;
import com.infra.qrys_wallet.Fragments.NFCsplashScreen;
import com.infra.qrys_wallet.R;

public class SendDialogFragment extends DialogFragment implements View.OnClickListener{
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppDialogTheme);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_forwalletsend, container,
                false);
        getDialog().setTitle("Send Money");

        // Do something else
        ImageView contactSend = (ImageView) rootView.findViewById(R.id.imgSendContact);
        ImageView nfcSend = (ImageView) rootView.findViewById(R.id.imgSendNFC);
        ImageView scanQrSend = (ImageView) rootView.findViewById(R.id.imgSendScanQR);
        contactSend.setOnClickListener(this);
        nfcSend.setOnClickListener(this);
        scanQrSend.setOnClickListener(this);
        return rootView;
    }

    /*Send Option Click Handling*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imgSendContact:

                Contacts phoneBook = new Contacts();
                fragmentManager = getFragmentManager();
                getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, phoneBook);
                ft.addToBackStack("fragback");
                ft.commit();
                getDialog().dismiss();
                break;
            case R.id.imgSendNFC:

                NFCsplashScreen nfcTag = new NFCsplashScreen();
                getFragmentManager().popBackStack();
                fragmentManager = getFragmentManager();
                ft = fragmentManager.beginTransaction();
                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, nfcTag);
                ft.addToBackStack("fragback");
                ft.commit();
                getDialog().dismiss();
                break;
            case R.id.imgSendScanQR:
                /*this is just for testing the UI of SendMoney Screen, plz see below commented code for actual*/
                ScanQRMain qrMainScan = new ScanQRMain();
                getFragmentManager().popBackStack();
                fragmentManager = getFragmentManager();
                ft = fragmentManager.beginTransaction();
                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                    fragmentManager.popBackStack();
                }
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, qrMainScan);
                ft.addToBackStack("fragback");
                ft.commit();
                getDialog().dismiss();
                break;
        }
    }
}
package com.infra.qrys_wallet.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.Fragments_UPI.SendMoney_UPI;
import com.infra.qrys_wallet.NFCTagReadActivity;
import com.infra.qrys_wallet.NFCTagReadActivity_UPI;
import com.infra.qrys_wallet.R;

/**
 * Created by sandeep.devhare on 30-10-2015.
 */
public class NFCsplashScreen extends Fragment {

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DashBoardWallet.fragmentID = 7;
        View rootView = inflater.inflate(R.layout.nfcsplashscreen, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                   /*NfcTagRead nfcTag = new NfcTagRead();
                    fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.frame_container, nfcTag);
                    ft.addToBackStack("fragback");
                    ft.commitAllowingStateLoss();*/
                    System.out.println("CheckNFCScanPosition "+ SendMoney_UPI.CheckNFCScanPosition);
                    if(SendMoney_UPI.CheckNFCScanPosition){

                        System.out.println("inside nfc fragment ");
                        Intent nfcCall = new Intent(getActivity(), NFCTagReadActivity_UPI.class);
                        getActivity().startActivity(nfcCall);
                        getActivity().finish();

                    }
                    else{
                        Intent nfcCall = new Intent(getActivity(), NFCTagReadActivity.class);
                        getActivity().startActivity(nfcCall);
                        getActivity().finish();
                    }



                }
            }
        };
        timerThread.start();

        return rootView;
    }




}

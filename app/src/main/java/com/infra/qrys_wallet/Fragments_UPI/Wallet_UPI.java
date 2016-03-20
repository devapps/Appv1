package com.infra.qrys_wallet.Fragments_UPI;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wallet_UPI extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;
    CardView PaymentAddress,ChequeRequest,SendMoney,ReceiveMoney,upiTesting;
    Button UPI_Testing;
    public Wallet_UPI() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Finfinity");
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        DashBoardWallet.fragmentID=0;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_wallet__upi, container, false);

        PaymentAddress=(CardView)rootView.findViewById(R.id.card_view1);
        ChequeRequest=(CardView)rootView.findViewById(R.id.card_view2);
        ReceiveMoney=(CardView)rootView.findViewById(R.id.card_view3);
        SendMoney=(CardView)rootView.findViewById(R.id.card_view4);

        upiTesting = (CardView)rootView.findViewById(R.id.card_view3);




        PaymentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetVirtualAddresslist getVirtualAddresslist= new GetVirtualAddresslist();
                fragmentManager=getFragmentManager();
                // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, getVirtualAddresslist);
                // ft.addToBackStack("fragback");
                ft.commit();

                /*SetPaymentAddressFragment PayAdd = new SetPaymentAddressFragment();
                fragmentManager =getFragmentManager();
                ft=fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container,PayAdd);
                ft.commit();*/
            }
        });

        upiTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChequeServicesDashBoard chequeServicesDashBoard = new ChequeServicesDashBoard();
                fragmentManager = getFragmentManager();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, chequeServicesDashBoard);
                ft.commit();
            }
        });

        ChequeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*ChequeServicesDashBoard chequeServicesDashBoard = new ChequeServicesDashBoard();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, chequeServicesDashBoard);
                // ft.addToBackStack("fragBack");
                ft.commit();*/

                SendMoney_UPI sendMoney_upi= new SendMoney_UPI();
                fragmentManager=getFragmentManager();
                // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, sendMoney_upi);
                // ft.addToBackStack("fragback");
                ft.commit();
            }
        });

        SendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Insta_Pay insta_pay= new Insta_Pay();
                fragmentManager=getFragmentManager();
                // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, insta_pay);
                // ft.addToBackStack("fragback");
                ft.commit();
            }
        });



        return rootView;
    }


}

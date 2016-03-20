package com.infra.qrys_wallet.Fragments_UPI;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;
import com.infra.qrys_wallet.Utils.SharedPreference;


public class ChequeServicesDashBoard extends Fragment implements View.OnClickListener{


    Fragment fragment;
    FragmentManager fragmentManager;
    FragmentTransaction ft;
    CardView CreateCheque,Stopcheque,chequeStatus,ReceiveMoneyviaCheq;

    SharedPreference appPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Cheque Services");
        DashBoardWallet.fragmentID=22;
        View rootView = inflater.inflate(R.layout.fragment_check_services_dash_board,container,false);


        CreateCheque=(CardView)rootView.findViewById(R.id.card_view1);
        Stopcheque=(CardView)rootView.findViewById(R.id.card_view2);
        chequeStatus=(CardView)rootView.findViewById(R.id.card_view3);
        ReceiveMoneyviaCheq=(CardView)rootView.findViewById(R.id.card_view4);
        // bundle = new Bundle();


        CreateCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Edited by Vaibhav...

                appPref.getInstance().setString("flagCreateCheque", "CreateCheque");
                appPref.getInstance().setString("flagReceiveMoneyviaCheq", "1");

                CreateVirtualAddress_UPI Micr = new CreateVirtualAddress_UPI();

                fragmentManager=getFragmentManager();
                // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, Micr);
                // ft.addToBackStack("fragback");
                ft.commit();

            }
        });

        ReceiveMoneyviaCheq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Edited by Vaibhav...
                //  Bundle bundle = new Bundle();
                // bundle.putString("flagReceiveMoneyviaCheq","ReceiveMoneyviaCheq");
                // bundle.putString("flagCreateCheque","1");

                appPref.getInstance().setString("flagCreateCheque", "1");
                appPref.getInstance().setString("flagReceiveMoneyviaCheq", "ReceiveMoneyviaCheq");


                CreateVirtualAddress_UPI Micr = new CreateVirtualAddress_UPI();
                // Micr.setArguments(bundle);
                fragmentManager=getFragmentManager();
                // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, Micr);
                // ft.addToBackStack("fragback");
                ft.commit();
            }
        });
        return rootView;
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()) {

        /*    case R.id.buttonCreateCheq: {

                fragment = new CreateVirtualAddress_UPI();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.replace(R.id.frame_container,fragment);
                ft.commit();
                break;
            }

            case R.id.buttonStopCheq: {

                fragment = new StopCheque();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.replace(R.id.frame_container,fragment);
                ft.commit();
                break;
            }

            case R.id.buttonCheqStatus: {

                fragment = new ChequeStatus();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.replace(R.id.frame_container,fragment);
                ft.commit();
                break;
            }

            case R.id.buttonReceiveMoneyviaCheq: {

                *//*fragment = new ReceiveMoneyviaCheque();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();

                ft.replace(R.id.frame_container,fragment);
                ft.commit();
                break;*//*
            }*/
        }
    }
}

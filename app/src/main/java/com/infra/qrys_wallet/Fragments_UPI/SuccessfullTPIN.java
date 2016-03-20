package com.infra.qrys_wallet.Fragments_UPI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.infra.qrys_wallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuccessfullTPIN extends Fragment {

    Button home;
    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    public SuccessfullTPIN() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_successfull_tpin, container, false);

        home=(Button)rootView.findViewById(R.id.home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Wallet_UPI callWallet = new Wallet_UPI();
                fragmentManager = getFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, callWallet);
                // ft.addToBackStack("fragBack");
                ft.commit();
            }
        });

        return rootView;
    }


}

package com.infra.qrys_wallet.Fragments_UPI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.infra.qrys_wallet.DashBoardWallet;
import com.infra.qrys_wallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateVirtualAddress_UPI extends Fragment {

    Button btnSacnMicr;
    FragmentManager fragmentManager;
    FragmentTransaction ft;

    public CreateVirtualAddress_UPI() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DashBoardWallet.fragmentID=21;
        View rootView = inflater.inflate(R.layout.fragment_create_virtual_address__upi,container,false);

        btnSacnMicr = (Button)rootView.findViewById(R.id.buttonScanReadMicr);
        btnSacnMicr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MICR_Scanner micr_scanner = new MICR_Scanner();
                fragmentManager=getFragmentManager();
                // getFragmentManager().popBackStack();
                ft = fragmentManager.beginTransaction();
                ft.replace(R.id.frame_container, micr_scanner);
                // ft.addToBackStack("fragback");
                ft.commit();
            }
        });

        return rootView;
    }


}

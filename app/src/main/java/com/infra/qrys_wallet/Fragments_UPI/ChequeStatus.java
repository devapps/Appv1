package com.infra.qrys_wallet.Fragments_UPI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.infra.qrys_wallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChequeStatus extends Fragment {


    public ChequeStatus() {
        // Required empty public constructor
    }


    Spinner spinnerVirtualAddress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cheque_status,container,false);
        spinnerVirtualAddress = (Spinner)rootView.findViewById(R.id.spinnerVirtualAddress);


        return rootView;
    }


}
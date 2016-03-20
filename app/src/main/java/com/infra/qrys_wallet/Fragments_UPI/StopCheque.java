package com.infra.qrys_wallet.Fragments_UPI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.infra.qrys_wallet.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StopCheque extends Fragment {


    public StopCheque() {
        // Required empty public constructor
    }


    EditText editTextVirtualAddress;
    Button btnStopCheque;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_stop_cheque,container,false);
       // btnStopCheque = (Button)rootView.findViewById(R.id.buttonStopCheq);
        //editTextVirtualAddress = (EditText)rootView.findViewById(R.id.editTextVirtualAddress);

     /*   btnStopCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/



        return rootView;
    }


}
